package com.majorproject.groceryshopping.shoppingapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileDetailsActivity extends AppCompatActivity {

    private TextView userName;
    private TextView userNumber;
    private TextView userDob;
    private TextView userAddress;
    private Button saveButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;
    // private Firebase customerRootRef;

    private String email;
    private String pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_profile_details);


        // initialization
        userName = (TextView) findViewById(R.id.user_name);
        userNumber = (TextView) findViewById(R.id.user_number);
        userDob = (TextView) findViewById(R.id.user_dob);
        userAddress = (TextView) findViewById(R.id.user_address);
        saveButton = (Button) findViewById(R.id.saveDetailsButton);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override //check user already logedin
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    // user loged in


                }
                else
                {
                    // user not loged in
                    //start Login/signup activity
                    finish();
                    startActivity(new Intent(ProfileDetailsActivity.this, SelectLoginSignupActivity.class));

                }
            }
        };


        // on click listner of save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonOnClick();
            }
        });

    }


    private boolean checkNumber(String string)
    {
        boolean b = false;

        if(string.length() == 10)
            for(int i=0;i<10;i++) {
                if ((int)string.charAt(i) >= 48 && (int)string.charAt(i) <= 57 && string.charAt(0)!='0')
                    b = true;
                else
                    return false;
            }
        return b;
    }

    private boolean checkDate(String value)
    {

        String format = "dd/MM/yyyy";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {

            ex.printStackTrace();
        }

//        if(date != null && date < Date.currt)
        return date != null;

    }

    private void openNext()
    {
        finish();
        startActivity(new Intent(this, MainStoreActivity.class));
    }




    private void saveButtonOnClick()
    {
        // declaration
        String name,number,dob,address;
        System.out.println("Button click");

        // initialization
        name    = userName.getText().toString().trim();
        number  = userNumber.getText().toString().trim();
        dob     = userDob.getText().toString().trim();
        address = userAddress.getText().toString().trim();


        //show progress dialog (loading...)
        progressDialog.setMessage("Saving user data...");
        progressDialog.show();

        //check if any input detain is invalid
        if(TextUtils.isEmpty(name))
        {
            //userName Empty
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter your Name",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(number))
        {
            //userNumber Empty
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter your Number",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(dob))
        {
            //userDob Empty
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter your D.O.B.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(address))
        {
            //userAddress Empty
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter your Adderss",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!checkDate(dob))
        {
            // check date format
            progressDialog.dismiss();
            Toast.makeText(this,"Enter D.O.B. in dd/MM/yyyy format",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkNumber(number))
        {
            // check number format
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter your 10 digit Mobile Number",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            saveUserData(name,number,dob,address);
        }
        // all fields ok


    }

    private  void saveUserData(String name,String number,String dob,String address)
    {


        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        UserInformation userInformation = new UserInformation(name, number, dob, address, firebaseUser.getEmail().toString());

        databaseReference.child("customer").child(firebaseUser.getUid()).setValue(userInformation)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileDetailsActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                        openNext();
                    }
                });
    }




    @Override
    public void onBackPressed() {
        Toast.makeText(ProfileDetailsActivity.this, "Please complete the form", Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
