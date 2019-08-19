/**
 *
 *  File Name: Signup.js (path: app/src/main/java/com.example.navigator/utils/Signup.js)
 *  Version: 1.0
 *  Author: Brute Force - Database Management
 *  Project: Indoor Mall Navigation
 *  Organisation: DVT
 *  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *
 *  Date        Author           Changes
 *  --------------------------------------------
 *  11/07/2019  Mpho Mashaba    Original
 *
 *  Functional Description: This program file registers a user
 *  Error Messages: none
 *  Constraints: field types
 *  Assumptions: It is assumed that user wants to register
 *
 */
package com.example.navigator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSignUp;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextUsername;
    private TextView textViewLogIn;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;   //progress loading
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        /*if(firebaseAuth.getCurrentUser()!= null){
            finish();
            //profile activity
            startActivity(new Intent(this, Scan.class));
        }*/

        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        textViewLogIn = (TextView) findViewById(R.id.textViewLogIn);
        buttonSignUp.setOnClickListener(this);
        textViewLogIn.setOnClickListener(this);


    }

    public void toastWrapper(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean validateEmail(String email)
    {
        if(email.isEmpty()){
            //email is empty
            toastWrapper("Please Enter Email");
            //stop the function
            return false;
        }

        if(email.indexOf('@') == -1)
        {
            toastWrapper("That email is not valid");
            //stop the function
            return false;
        }
        if(email.lastIndexOf('.') < email.indexOf('@'))
        {
            toastWrapper("That email is not valid");
            //stop the function
            return false;
        }

        return true;

    }

    public boolean validatePassword(String password)
    {
        int length = password.length();

        if(password.isEmpty()){
            //password is empty
            toastWrapper("Please enter password");
            //stop the function
            return false;
        }

        if(length<6){
            toastWrapper("Password must be at least 6 characters long");
            return false;
        }
        if(!password.matches(".*\\d.*"))
        {
            toastWrapper("Password must contain at least 1 number");
            return false;
        }
        if(!password.matches(".*[A-Z].*"))
        {
            toastWrapper("Password must contain at least 1 uppercase letter");
            return false;
        }
        if(!password.matches(".*[a-z].*"))
        {
            toastWrapper("Password must contain at least 1 lowercase letter");
            return false;
        }

        return true;
    }


    private void registerUser(){

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        //int length = password.length();
        if(!validateEmail(email))
        {
            return;
        }
        if(!validatePassword(password))
        {
            return;
        }



        //if validations are ok
        //show progress bar

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //user is registerd
                    //start profile activity
                    progressDialog.hide();
                    Toast.makeText(Signup.this,"Registerd Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                    //start profile activity
                    onAuthSuccess(task.getResult().getUser());
                    startActivity(new Intent(getApplicationContext(),Login.class));
                }

                else{
                    progressDialog.hide();
                    Toast.makeText(Signup.this,"Could'nt Register, Email already exist",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void onAuthSuccess(FirebaseUser user) {
        // Write new user
        String username = editTextUsername.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            //password is empty
            Toast.makeText(this,"Please Enter Username",Toast.LENGTH_SHORT).show();
            //stop the function
            return;
        }
        writeNewUser(user.getUid(),username, user.getEmail());
        finish();
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(userId, name, email);

        mDatabase.child("Users").child(userId).setValue(user);
    }

    @Override
    public void onClick(View v) {
        if(v==buttonSignUp){
            registerUser();
        }


        if(v==textViewLogIn){
            //open signin activity
            finish();
            startActivity(new Intent(this, Login.class));
        }

    }
}


