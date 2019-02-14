package com.example.openparking;

import android.app.Dialog;
import android.content.Intent;
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



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private EditText username;
    private EditText password;
    private TextView Info;
    private Button Login;
    private int counter = 3;

    private void validate(String userName, String password){
        if((userName.equals("admin"))&& (password.equals("12345"))){
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        }
        else{
            counter--;
            Info.setText("Number of attempts remaining: " + String.valueOf(counter));
            if(counter == 0){
                Login.setEnabled(false);
            }
        }
    }

    // Test Buttons
    Button btnMap;
    Button btnFileWrite;
    Button btnFileRead;

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

    private void init()
    {
        // Login Interface
        username = (EditText)findViewById(R.id.etUsername);
        password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView)findViewById(R.id.tvInfo);
        Login = (Button)findViewById(R.id.button_login);
        Info.setText("Number of attempts remaining: 5");
        Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                validate(username.getText().toString(), password.getText().toString());
            }
        });

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
                generateNoteOnSD( "Hello", "World");
            }
        });

        // Read from File Button
        btnFileRead  = findViewById(R.id.btnFileRead);
        btnFileRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked Read Button");

                //Testing Writing to file
                generateNoteOnSD( "Read", "World");
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
            Toast.makeText(this, "You cant make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
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
