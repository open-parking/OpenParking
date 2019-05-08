package com.example.openparking;

import android.app.Dialog;
import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

// Writing to file
import java.io.*;
import java.io.File;
import android.os.Environment;
import java.io.FileWriter;

//Buttons and Text
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// Google API  
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.FirebaseApp;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private Button buy;
    private Button sell;
    private Button account;
    private Button myListings;
    private Button myRating;


    // Test Buttons
    Button btnTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Check google services
        if(isServicesOK())
        {
            init();
        }

    }

    public void signUp(View view) {
        Intent registration = new Intent(this, SignupActivity.class);
        startActivity(registration);
    }

    private void init()
    {

        // Login Interface

        buy = (Button) findViewById(R.id.btnBuy);
        sell = (Button) findViewById(R.id.btnSell);
        account = (Button) findViewById(R.id.btnAccount);
        myListings = (Button) findViewById(R.id.btnMyListings);
        myRating = (Button) findViewById(R.id.btnMyRating);

        /// Buy Button
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // REPLACE WITH MAP/LIST VIEW SELECTION ACTIVITY
                startActivity(new Intent(MainActivity.this, BuyActivity.class));
            }
        });
        /// Sell Button
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddParkingSpaceActivity.class));
            }
        });
        /// Account Button
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
        /// My Listings Button
        myListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyParkingList.class));
            }
        });
        // My Rating Button
        myRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyRatingActivity.class));
            }
        });

    }

    // Check if we have a connection to google services
    public boolean isServicesOK()
    {
        Log.d(TAG , "isServicesOK() checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // an error occurred but we can resolve it.
            Log.d(TAG, "isServicesOK: am error occurred but we can fix it ");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}
