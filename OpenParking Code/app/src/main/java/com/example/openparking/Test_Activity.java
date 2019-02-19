package com.example.openparking;

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
import android.widget.TextView;

public class Test_Activity extends AppCompatActivity {

    // Test Buttons
    Button btnMap;
    Button btnFileWrite;
    Button btnFileRead;
    Button btnPayPal;


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

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
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

/*
        btnPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked PayPal Button");

                Intent intent = new Intent(MainActivity.this, PayPal.class);
                startActivity(intent);

            }

        });
        */



    }

}
