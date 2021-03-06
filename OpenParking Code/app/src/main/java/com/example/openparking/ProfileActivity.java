package com.example.openparking;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private ImageView profileImage;
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

        profileImage = (ImageView) findViewById(R.id.profileImage);

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);


        textViewUserEmail.setText("Welcome " + user.getDisplayName() + "!");

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);

        buttonMainMenu = (Button) findViewById(R.id.buttonMainMenu);
        buttonMainMenu.setOnClickListener(this);
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
        }
    }
}
