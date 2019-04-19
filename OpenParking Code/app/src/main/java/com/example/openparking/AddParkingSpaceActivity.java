package com.example.openparking;

//TODO
// Get owner ID and save it as a property of ParkingSpace Class
// Translate from address to latitude longitude coordinates

//DONE:
// USE THIS ACTIVITY TO TEST ADDING PARKING INSTANCES TO DATABASE

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddParkingSpaceActivity extends AppCompatActivity {
    private static final String TAG = "AddParkingInstanceAct";

    Button btnSend;

    private EditText editTextAddress;
    private EditText editTextZipCode;
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private EditText editTextCost;
    private EditText editTextOpenTime;
    private EditText editTextCloseTime;

    private DatabaseReference mDatabase;
    private ParkingSpace test;

    private FirebaseAuth firebaseAuth;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking_space);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextAddress     = findViewById(R.id.editTextAddress);
        editTextZipCode     = findViewById(R.id.editTextZipCode);
        editTextLatitude    = findViewById(R.id.editTextLatitude);
        editTextLongitude   = findViewById(R.id.editTextLongitude);
        editTextCost        = findViewById(R.id.editTextCost);
        editTextOpenTime    = findViewById(R.id.editTextOpenTime);
        editTextCloseTime   = findViewById(R.id.editTextCloseTime);

        // Get userID
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        FirebaseUser fuser = firebaseAuth.getCurrentUser();
        userID = fuser.getUid();

        init();

    }

    private void init() {
        /* Test Buttons */

        // Map Button
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked Send Button");

                //TODO Send New Version of ParkingSpace to firebase
                //DONE: SEND DATA TO FIREBASE

                final String address= editTextAddress.getText().toString().trim();
                final String zipcode= editTextZipCode.getText().toString().trim();
                final Double lat    = Double.parseDouble(editTextLatitude.getText().toString());
                final Double lon    = Double.parseDouble(editTextLongitude.getText().toString());
                final Double cost   = Double.parseDouble(editTextCost.getText().toString());
                final String open   = editTextOpenTime.getText().toString().trim();
                final String close  = editTextCloseTime.getText().toString().trim();

                writeNewParkingSpace(userID, address, zipcode, lat, lon, cost, open, close);
            }

        });
    }

    private void writeNewParkingSpace(String ownerID, String Address,String zipCode, Double latitude, Double longitude, Double cost, String openTime, String closeTime )
    {

        ParkingSpace ps = new ParkingSpace(ownerID, Address,zipCode, latitude, longitude, cost, openTime, closeTime);
        Log.v(TAG, "Sending to mDatabase");
        mDatabase.child("ZipCodes").child(zipCode).push().setValue(ps);
    }
}
