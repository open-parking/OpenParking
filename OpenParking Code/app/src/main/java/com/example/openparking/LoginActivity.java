package com.example.openparking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView Signup;
    private TextView textViewForgotPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public User user;
    public Vehicle vehicle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        Signup = (TextView) findViewById(R.id.textViewSignup);
        textViewForgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);
        progressDialog = new ProgressDialog(this);

        user = new User();
        vehicle = new Vehicle();

        buttonSignIn.setOnClickListener(this);
        Signup.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordRecovery.class));

            }
        });

    }


    private void userLogin()
    {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        //if validations are passed,
        //we will first show a progress bar
        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Start the profile activity
                            progressDialog.dismiss();


                            if(checkIfEmailVerified()){
                                Toast.makeText(LoginActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                                finish();
                                user.setName("");
                                user.setEmail(email);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Sign in failed, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private Boolean checkIfEmailVerified()
    {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user.isEmailVerified()) {
            finish();
            return true;
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Verify Email")
                    .setMessage("Please verify your email then try again.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            firebaseAuth.signOut();
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSignIn)
        {
            userLogin();
        }

        if(v == Signup){
            finish();
            startActivity(new Intent(this, SignupActivity.class));
        }


    }
}
