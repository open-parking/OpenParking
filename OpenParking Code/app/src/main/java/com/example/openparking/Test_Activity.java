package com.example.openparking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Test_Activity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    // Test Buttons
    Button btnMap;
    Button btnAddParkingSpace;
    Button btnFileWrite;
    Button btnFileRead;
    Button btnPayPal;
    Button btnRating;
    Button btnParkingList;
    Button btnCreateParkingInstance;
    Button btnPurchaseComplete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_);
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

        init();
    }

    private void init()
    {
        /* Test Buttons */

        // Map Button
        btnMap  =  findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked Map Button");

                Intent intent = new Intent(Test_Activity.this, MapsActivity.class);
                startActivity(intent);

            }

        });

        //Add Parking Instance Button
        btnAddParkingSpace  =  findViewById(R.id.btnAddParkingSpace);
        btnAddParkingSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked Parking Space Button");

                Intent intent = new Intent(Test_Activity.this, AddParkingSpaceActivity.class);
                startActivity(intent);
            }

        });

        // Write to File Button
        btnFileWrite  = (Button) findViewById(R.id.btnFileWrite);
        btnFileWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked Write Button");

                //Testing Writing to file
                //generateNoteOnSD( "Hello", "World");
            }
        });

        // Read from File Button
        btnFileRead  = findViewById(R.id.btnFileRead);
        btnFileRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked Read Button");

                //Testing Writing to file
                //generateNoteOnSD("Read", "World");
            }
        });



        // PayPal Button
        btnPayPal  = findViewById(R.id.btnPayPal);
        btnPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked PayPal Button");

                Intent intent = new Intent(Test_Activity.this, PayPalActivity.class);
                startActivity(intent);

            }

        });

        // Rating Button
        btnRating  = findViewById(R.id.btnRating);
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked Rating Button");

                Intent intent = new Intent(Test_Activity.this, RatingActivity.class);
                startActivity(intent);

            }

        });

        btnParkingList = findViewById(R.id.btnParkingList);
        btnParkingList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v(TAG, "Clicked Parking List Button");
                Intent intent = new Intent(Test_Activity.this, ParkingListActivity.class);
                startActivity(intent);
            }
        });

        btnCreateParkingInstance = findViewById(R.id.btnCreateParkingInstance);
        btnCreateParkingInstance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked Create Parking Instance Button");
                Intent intent = new Intent(Test_Activity.this, CreateParkingInstanceActivity.class);
                startActivity(intent);
            }
        });


        btnPurchaseComplete = findViewById(R.id.btnPurchaseComplete);
        btnPurchaseComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.v(TAG, "Clicked Purchase Complete Button");
                Intent intent = new Intent(Test_Activity.this, PurchaseCompleteActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // Testing Writing external storage
    public void generateNoteOnSD( String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
