package com.example.navigator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_LONG;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private DatabaseReference ref;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef,demoRef;
    private Product objProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        /*if(firebaseAuth.getCurrentUser()!= null){
            finish();
            //profile activity
            startActivity(new Intent(this, Scan.class));
        }*/

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewSignup = (TextView) findViewById(R.id.textViewSignUp);


        buttonLogin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);


    }
    private void userLogin(){

        String email= editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            //stop the function
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            //stop the function
            return;
        }
        //if validations are ok
        //show progress bar

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        rootRef = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to demo node
        demoRef = rootRef.child("Product");
        final String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");


        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            //start profile activity
                           // startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            //Fragment fragment = CustomFragment.newInstance();

                            //demoRef.push().setValue(sessionId);

                            ref = FirebaseDatabase.getInstance().getReference().child("Cart");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(sessionId.equals("5060466519077")){
                                        objProduct = new Product("5060466519077","Power Play",19.50);
                                        ref.push().setValue(objProduct);
                                    }
                                    else if(sessionId.equals("8718114642871")){
                                        objProduct = new Product("5060466519077","Power Play",19.50);
                                        ref.push().setValue(objProduct);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Toast.makeText(getApplicationContext(),"Item added to cart", Toast.LENGTH_LONG).show();

                           //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Login Failed...Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {

        if(v == buttonLogin){
            userLogin();
        }
        if(v == textViewSignup){
            finish();
            startActivity(new Intent(this, Signup.class));
        }


    }
}
