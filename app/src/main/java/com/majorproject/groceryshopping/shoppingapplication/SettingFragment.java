package com.majorproject.groceryshopping.shoppingapplication;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private static boolean ON_CREATE_CALLED = false;

    private View mView;
    private AlertDialog alertDialog;

    private TextView textViewMyAccountName;
    private TextView textViewMyAccountContact;
    private TextView textViewMyAccountAddress;
    private TextView textViewMyAccountEmail;
    private TextView textViewMyAccountDob;

    private Button buttonUpdateProfile;
    private Button buttonChangePassword;

    private UserInformation userInformation = new UserInformation();

    public SettingFragment() {
        // Required empty public constructor
    }
    public static SettingFragment getInstance(UserInformation userInformation)
    {
        SettingFragment settingFragment=new SettingFragment();
        settingFragment.setUserInformation(userInformation);

        return settingFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ON_CREATE_CALLED = true;
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        mView = inflater.inflate(R.layout.dialog_change_password, null);

        textViewMyAccountName = (TextView) view.findViewById(R.id.my_account_name);
        textViewMyAccountAddress = (TextView) view.findViewById(R.id.my_account_address);
        textViewMyAccountContact = (TextView) view.findViewById(R.id.my_account_contact);
        textViewMyAccountEmail = (TextView) view.findViewById(R.id.my_account_email);
        textViewMyAccountDob = (TextView) view.findViewById(R.id.my_account_dob);

        buttonUpdateProfile = (Button) view.findViewById(R.id.my_account_button_update_profile);
        buttonChangePassword = (Button) view.findViewById(R.id.my_account_button_change_password);


        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonUpdateProfileOnClick();
            }
        });

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChangePasswordOnClick();
            }
        });

        setComponents(getUserInformation());
        return view;
    }

    public void setComponents(UserInformation information)
    {
       /* System.out.println("from my account fragment");
        sellerDetails.printAll();*/
        textViewMyAccountEmail.setText(information.getUserEmail());
        textViewMyAccountContact.setText(information.getUserNumber());
        textViewMyAccountAddress.setText(information.getUserAdderss());
        textViewMyAccountName.setText(information.getUserName());
        textViewMyAccountDob.setText(information.getUserDob());

    }


    private void buttonUpdateProfileOnClick()
    {
        ProfileUpdateFragment account = ProfileUpdateFragment.getInstance(userInformation);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.main_store_relative_layout, account)
                .addToBackStack(null)
                .commit();
        setComponents( getUserInformation() );
    }

    private void buttonChangePasswordOnClick()
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        //mView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        final Button buttonUpdatePassword = (Button) mView.findViewById(R.id.save_button_change_password);


        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonUpdatePasswordOnClick();
            }
        });

         mBuilder.setView(mView);
        alertDialog = mBuilder.create();
        alertDialog.show();

    }//end

    private void buttonUpdatePasswordOnClick()
    {
        final String oldpass, newpass1, newpass2;

        final EditText oldPassword  = (EditText) mView.findViewById(R.id.old_pass);
        final EditText newPassword1 = (EditText) mView.findViewById(R.id.new_pass1);
        final EditText newPassword2 = (EditText) mView.findViewById(R.id.new_pass2);

        oldpass = oldPassword.getText().toString().trim();
        newpass1 = newPassword1.getText().toString().trim();
        newpass2 = newPassword2.getText().toString().trim();

        System.out.println(oldpass +" "+newpass1+" "+newpass2);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            makeTextToast("Not logged in, Please login again");
            getActivity().finish();
            startActivity(new Intent(getActivity(), SelectLoginSignupActivity.class));
        }

        if(TextUtils.isEmpty(oldpass))
        {
            makeTextToast("Enter your old Password");
            return;
        }
        if(TextUtils.isEmpty(newpass1))
        {
            Toast.makeText(getActivity(),"Enter your new Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(newpass2))
        {
            makeTextToast("Enter your new Password");
            return;
        }
        if(!TextUtils.equals(newpass1,newpass2))
        {
            makeTextToast("New Password not same");
            return;
        }
        else if(TextUtils.equals(oldpass,newpass1))
        {
            makeTextToast("New Password cannot be same as old password");
            return;
        }


        final String email = user.getEmail();
        AuthCredential credential;
        credential = EmailAuthProvider.getCredential(email,oldpass);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override

                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user.updatePassword(newpass1)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(!task.isSuccessful()){
                                                makeTextToast("Something went wrong. Please try again later");
                                                oldPassword.setText("");
                                                newPassword1.setText("");
                                                newPassword2.setText("");

                                            }else {
                                                makeTextToast("Password Successfully Modified");
                                                hideAlertDialog();
                                            }
                                        }
                                    });
                        }else {
                            makeTextToast("Authentication Failed");
                            oldPassword.setText("");
                            newPassword1.setText("");
                            newPassword2.setText("");
                        }
                    }
                });
    }


    private void hideAlertDialog()
    {
        alertDialog.dismiss();
    }

    private void makeTextToast(String text)
    {
        System.out.println("  "+ text);
        Toast.makeText(getActivity(), text , Toast.LENGTH_SHORT).show();
    }






    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }


}
