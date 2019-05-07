package com.example.openparking;

//TODO
// Get owner ID and save it as a property of ParkingSpace Class
// Translate from address to latitude longitude coordinates

//DONE:
// USE THIS ACTIVITY TO TEST ADDING PARKING INSTANCES TO DATABASE

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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

import java.util.List;

public class AddParkingSpaceActivity extends AppCompatActivity {
    private static final String TAG = "AddParkingInstanceAct";

    Button btnSend;
    Button btnCoordinate;

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

        //FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
         //   @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
         //               .setAction("Action", null).show();
         //   }
        //});
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextAddress     = findViewById(R.id.editTextAddress);
        editTextZipCode     = findViewById(R.id.editTextZipCode);
        editTextLatitude    = findViewById(R.id.editTextLatitude);
        editTextLongitude   = findViewById(R.id.editTextLongitude);
        editTextCost        = findViewById(R.id.editTextCost);
        editTextOpenTime    = findViewById(R.id.editTextOpenTime);
        editTextCloseTime   = findViewById(R.id.editTextCloseTime);

        btnSend = findViewById(R.id.btnPicture);
        btnCoordinate = findViewById(R.id.btnCoords);

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
        //btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked Send Button");


                //DONE: SEND DATA TO FIREBASE

                final String address = editTextAddress.getText().toString().trim();
                final String zipcode = editTextZipCode.getText().toString().trim();
                final Double lat = Double.parseDouble(editTextLatitude.getText().toString());
                final Double lon = Double.parseDouble(editTextLongitude.getText().toString());
                final Double cost = Double.parseDouble(editTextCost.getText().toString());
                final String open = editTextOpenTime.getText().toString().trim();
                final String close = editTextCloseTime.getText().toString().trim();

                writeNewParkingSpace(userID, address, zipcode, lat, lon, cost, open, close);

                //TODO Clear all edit text
                editTextAddress.setText("Address");
                editTextAddress.setText("Zipcode");
                editTextLatitude.setText("Latitude");
                editTextLongitude.setText("Longitude");
                editTextCost.setText("Price per hour");
                editTextOpenTime.setText("Open Time");
                editTextCloseTime.setText("Close Time");
            }

        });

        //btnCoordinate = findViewById(R.id.btnCoords);
        btnCoordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //TODO: Get Coordinates from maps api

                Geocoder geocoder = new Geocoder(v.getContext());
                List<Address> addresses;
                try{
                    addresses = geocoder.getFromLocationName(editTextAddress.getText().toString().trim(), 1);

                    if(addresses.size() > 0) {
                        double latitude= addresses.get(0).getLatitude();
                        double longitude= addresses.get(0).getLongitude();
                        editTextLatitude.setText(Double.toString(latitude));
                        editTextLongitude.setText(Double.toString(longitude));
                    }
                    else{
                        //Not a valid address
                        editTextAddress.setText("Not Valid Address");
                    }
                }catch(Exception e)
                {
                    Log.v(TAG, ":error getting coordinates");
                }




            }
        });
    }

    private void writeNewParkingSpace(String ownerID, String Address,String zipCode, Double latitude, Double longitude, Double cost, String openTime, String closeTime )
    {
        ParkingSpace ps = new ParkingSpace(ownerID, Address,zipCode, latitude, longitude, cost, openTime, closeTime);
        Log.v(TAG, "Sending to mDatabase");
        mDatabase.child("ParkingSpaces").child(zipCode).push().setValue(ps);// OLD TABLE NAME = ZipCodes
    }
}
