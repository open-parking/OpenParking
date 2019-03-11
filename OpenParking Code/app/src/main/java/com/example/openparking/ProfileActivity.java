package com.example.openparking;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user == null)
        {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        }


        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        textViewUserEmail.setText("Welcome " + user.getEmail());

        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if(view == buttonMainMenu){
            finish();
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        }
    }
}
