package com.example.openparking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.w3c.dom.Text;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {


    private Button buttonRegister;

    private EditText editTextEmail;
    private String email;

    private EditText editTextPassword;
    private String password;

    private EditText editTextName;
    private String name;


    private TextView textViewSignin;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }


        progressDialog = new ProgressDialog(this);



        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);

        textViewSignin = (TextView) findViewById(R.id.textViewSignin);


        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }


    private void registerUser() {
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        name = editTextName.getText().toString().trim();

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
        if(TextUtils.isEmpty(name)) {
            //name is empty
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        //if validations are passed,
        //we will first show a progress bar
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //store first and last name in user instance!!
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()

                                    .setDisplayName(name)
                                    .setPhotoUri(Uri.parse("gs://openparking-491.appspot.com/default-profile.gif"))
                                    .build();

                            user.updateProfile(profileUpdates);

                            sendEmailVerification();

                            FirebaseAuth.getInstance().signOut();
                            System.out.println("Signing out");

                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Please verify your email then sign in again.", Toast.LENGTH_SHORT).show();

                            finish();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Registration Failed, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void sendEmailVerification() {
        // [START send_email_verification]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Email sent.");
                        }
                    }
                });
        // [END send_email_verification]
    }

    @Override
    public void onClick(View view) {
        if(view == buttonRegister)
        {
            registerUser();
        }
        if(view == textViewSignin)
        {
            //will open login activity here
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
