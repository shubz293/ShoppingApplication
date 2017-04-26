package com.majorproject.groceryshopping.shoppingapplication;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileUpdateFragment extends Fragment {

    private UserInformation userInformation = new UserInformation();

    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextContact;
    private EditText editTextDob;

    private Button buttonSave;

    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public ProfileUpdateFragment() {
        // Required empty public constructor
    }


    public static ProfileUpdateFragment getInstance(UserInformation details)
    {
        ProfileUpdateFragment fragment = new ProfileUpdateFragment();
        fragment.setUserInformation(details);


        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_update, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(getActivity());

        editTextName = (EditText) view.findViewById(R.id.editTextUpdateName);
        editTextContact = (EditText) view.findViewById(R.id.editTextUpdateContact);
        editTextAddress = (EditText) view.findViewById(R.id.editTextUpdateAddress);
        editTextDob = (EditText) view.findViewById(R.id.editTextUpdateDob);

        buttonSave = (Button) view.findViewById(R.id.button_saveUpdate);

        setComponents(getUserInformation());
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSaveOnClick();
            }
        });


        return view;
    }

    public void setComponents(UserInformation information)
    {
       /* System.out.println("from my account fragment");
        sellerDetails.printAll();*/
        editTextContact.setText(information.getUserNumber());
        editTextAddress.setText(information.getUserAdderss());
        editTextName.setText(information.getUserName());
        editTextDob.setText(information.getUserDob());

    }


/*

    private void buttonSaveOnClick()
    {
        String sellerName;
        String contact;
        String address;
        int itemCount;


        sellerName = editTextName.getText().toString().trim();
        contact = editTextContact.getText().toString().trim();
        address = editTextAddress.getText().toString().trim();
        itemCount = sellerDetails.getItemCount();

        progressDialog.setMessage("Saving Data..");
        progressDialog.show();

        if(TextUtils.isEmpty(sellerName))
        {
            //userName Empty
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Please enter your Store Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(contact))
        {
            //userName Empty
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Please enter your Phone Number",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(address))
        {
            //userName Empty
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Please enter your Address",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkNumber(contact))
        {
            // check number format
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Please enter your 10 digit Mobile Number",Toast.LENGTH_SHORT).show();
            return;
        }

        SellerDetails details = new SellerDetails(sellerName,contact,address,itemCount,firebaseUser.getEmail().toString());
        sellerDetails = details;
        databaseReference.child(getString(R.string.firebase_seller_subclass)).child(firebaseUser.getUid()).setValue(details)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                        openNext();
                    }
                });



    }

*/



    private void buttonSaveOnClick()
    {
        // declaration
        String name,number,dob,address;
        System.out.println("Button click");

        // initialization
        name    = editTextName.getText().toString().trim();
        number  = editTextContact.getText().toString().trim();
        dob     = editTextDob.getText().toString().trim();
        address = editTextAddress.getText().toString().trim();


        //show progress dialog (loading...)
        progressDialog.setMessage("Saving user data...");
        progressDialog.show();

        //check if any input detain is invalid
        if(TextUtils.isEmpty(name))
        {
            //userName Empty
            progressDialog.dismiss();
            makeTextToast("Please enter your Name");
            return;
        }

        if(TextUtils.isEmpty(number))
        {
            //userNumber Empty
            progressDialog.dismiss();
            makeTextToast("Please enter your Number");
            return;
        }

        if(TextUtils.isEmpty(dob))
        {
            //userDob Empty
            progressDialog.dismiss();
            makeTextToast("Please enter your D.O.B.");
            return;
        }

        if(TextUtils.isEmpty(address))
        {
            //userAddress Empty
            progressDialog.dismiss();
            makeTextToast("Please enter your Adderss");
            return;
        }

        if(!checkDate(dob))
        {
            // check date format
            progressDialog.dismiss();
            makeTextToast("Enter D.O.B. in dd/MM/yyyy format");
            return;
        }
        if(!checkNumber(number))
        {
            // check number format
            progressDialog.dismiss();
            makeTextToast("Please enter your 10 digit Mobile Number");
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
        assert firebaseUser != null;
        UserInformation userInformation = new UserInformation(name, number, dob, address, firebaseUser.getEmail());

        databaseReference.child("customer").child(firebaseUser.getUid()).setValue(userInformation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dataSavedSuccessfully();
                    }
                });

    }


    private void dataSavedSuccessfully()
    {
        progressDialog.dismiss();
        makeTextToast("Data saved successfully");
        openNext();
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

    private void makeTextToast(String text)
    {
        System.out.println("  "+ text);
        Toast.makeText(getActivity(), text , Toast.LENGTH_SHORT).show();
    }

    private void openNext()
    {
        SettingFragment home = SettingFragment.getInstance(getUserInformation());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_store_relative_layout , home)
                .commit();
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

}

