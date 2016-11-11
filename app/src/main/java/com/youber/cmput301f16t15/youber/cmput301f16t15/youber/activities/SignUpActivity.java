package com.youber.cmput301f16t15.youber.cmput301f16t15.youber.activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.youber.cmput301f16t15.youber.cmput301f16t15.youber.elasticsearch.ElasticSearchUser;
import com.youber.cmput301f16t15.youber.cmput301f16t15.youber.dialog.NoticeDialogFragment;
import com.youber.cmput301f16t15.youber.R;
import com.youber.cmput301f16t15.youber.cmput301f16t15.youber.user.User;
import com.youber.cmput301f16t15.youber.cmput301f16t15.youber.controllers.UserController;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * The type Sign up activity.
 */
public class SignUpActivity extends AppCompatActivity implements NoticeDialogFragment.NoticeDialogListener {

    EditText username;
    EditText email;
    EditText phoneNum;
    EditText dateOfBirth;
    EditText firstName;
    EditText lastName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = (EditText) findViewById(R.id.usernameInput);
        email = (EditText) findViewById(R.id.emailInput);
        phoneNum = (EditText) findViewById(R.id.phoneInput);
        dateOfBirth = (EditText) findViewById(R.id.dateofBirthInput);
        firstName = (EditText) findViewById(R.id.firstnameInput);
        lastName = (EditText) findViewById(R.id.lastNameInput);

        Button createNewUser = (Button) findViewById(R.id.createNewUser);

        createNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String usernameText = username.getText().toString();
                String emailText = email.getText().toString();
                String phoneNumText = phoneNum.getText().toString();
                String dateOfBirthText = dateOfBirth.getText().toString();
                String firstNameText = firstName.getText().toString();
                String lastNameText = lastName.getText().toString();


                try
                {

                    ElasticSearchUser.getObjects getter = new ElasticSearchUser.getObjects();
                    getter.execute(usernameText);
                    ArrayList<User> users = getter.get();

                    if (users.size()==0)
                    {
                        User user = new User(usernameText, firstNameText, lastNameText, dateOfBirthText, phoneNumText, emailText);

                        UserController.setContext(SignUpActivity.this);
                        UserController.saveUser(user);

                        ElasticSearchUser.add adder = new ElasticSearchUser.add();
                        adder.execute(user);
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString(getResources().getString(R.string.message), getResources().getString(R.string.usernameExitsMessage));
                        bundle.putString(getResources().getString(R.string.positiveInput), getResources().getString(R.string.ok));
                        bundle.putString(getResources().getString(R.string.negativeInput), getResources().getString(R.string.login));

                        DialogFragment dialog = new NoticeDialogFragment();
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }



            }
        });


    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}