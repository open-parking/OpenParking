package com.example.openparking;

import android.app.ProgressDialog;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView Signup;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), SignupActivity.class));
        }


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        Signup = (TextView) findViewById(R.id.textViewSignup);

        progressDialog = new ProgressDialog(this);

        buttonSignIn.setOnClickListener(this);
        Signup.setOnClickListener(this);

    }


    private void userLogin()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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
                            //Start the profile acivity
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();

                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Sign in failed, please try again", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
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
