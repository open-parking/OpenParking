package com.example.openparking;

//TODO
// Get owner ID and save it as a property of ParkingSpace Class
// Translate from address to latitude longitude coordinates

//DONE:
// USE THIS ACTIVITY TO TEST ADDING PARKING INSTANCES TO DATABASE

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddParkingSpaceActivity extends AppCompatActivity {
    private static final String TAG = "AddParkingInstanceAct";

    //Buttons
    Button btnAddPicture;
    Button btnCoordinate; // To Be Hidden // Change to  btnConfirm
    Button btnSend;

    //Street, City, State and ZipCode
    private EditText editTextStreet;
    private EditText editTextCity;
    private Spinner  stateSpinner;
    private EditText editTextZipCode;

    //Price and Times
    private EditText editTextPrice;
    private Spinner  openTimeSpinner;
    private Spinner  closeTimeSpinner;
    private Spinner  openTimeAMPMSpinner;
    private Spinner  closeTimeAMPMSpinner;

    //Latitude and Longitude
    private EditText editTextLatitude;    // To Be Hidden
    private EditText editTextLongitude;   // To Be Hidden

    //Database
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private String userID;                  //To be stored with the new Parking Space

    //Temp Vars
    String street;
    String city;
    String state;
    String address;
    String zipcode;
    String price;
    String openTime;
    String closeTime;
    String lat_Str;
    String lng_Str;
    Boolean coordinatesReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking_space);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //---Temp Variables---

        //Street, City, State and ZipCode
        street = "";
         city = "";
         state = "";
         address = ""; //combination of street + city + state.
         zipcode = "";

        //Price and Times
        price = "";
         openTime = "";
         closeTime = "";

        //Latitude and Longitude
        lat_Str = "";
         lng_Str = "";
         coordinatesReceived = false;


        //---Edit Text and Spinners

        //Street, City, State and ZipCode
        editTextStreet      = findViewById(R.id.editTextStreet);
        editTextCity        = findViewById(R.id.editTextCity);
        stateSpinner        = findViewById(R.id.spinner_states);
        editTextZipCode     = findViewById(R.id.editTextZipCode);

        //Price and Times
        editTextPrice        = findViewById(R.id.editTextPrice);
        openTimeSpinner     = findViewById(R.id.spinner_open_time);
        closeTimeSpinner    = findViewById(R.id.spinner_close_time);
        openTimeAMPMSpinner = findViewById(R.id.spinner_open_time_ampm);
        closeTimeAMPMSpinner= findViewById(R.id.spinner_close_time_ampm);

        //Latitude and Longitude
        editTextLatitude    = findViewById(R.id.editTextLatitude); // To Be  Hidden
        editTextLongitude   = findViewById(R.id.editTextLongitude); // To Be Hidden

        //---Buttons---
        btnAddPicture = findViewById(R.id.bt_addpicture);
        btnCoordinate = findViewById(R.id.btnGetCoords);//
        btnSend = findViewById(R.id.btnSend);

        //---Spinners---

        //Populate States Spinner
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(AddParkingSpaceActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.state_abbreviations));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(myAdapter);


        //Populate Times Spinners
        ArrayAdapter<String> myTimeAdapter = new ArrayAdapter<String>(AddParkingSpaceActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.times));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        openTimeSpinner.setAdapter(myTimeAdapter);
        closeTimeSpinner.setAdapter(myTimeAdapter);


        //Populate AM PM Spinners
        ArrayAdapter<String> myAMPM_Adapter = new ArrayAdapter<String>(AddParkingSpaceActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.am_pm));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        openTimeAMPMSpinner.setAdapter(myAMPM_Adapter);
        closeTimeAMPMSpinner.setAdapter(myAMPM_Adapter);
        closeTimeAMPMSpinner.setSelection(1);

        //---DataBase---
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        FirebaseUser fuser = firebaseAuth.getCurrentUser();
        userID = fuser.getUid(); // Get userID

        init();//Set OnClickListeners
    }

    private void init() {

        // Send Button
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked Send Button");

                //Check for empty fields
                checkForEmptyFields();

                if(!coordinatesReceived)
                {
                    toast("Press Confirm Button" );
                    return;
                }

                //Send Parking Space
                Double lat = Double.parseDouble(lat_Str);
                Double lon = Double.parseDouble(lng_Str);
                Double priceDouble = Double.parseDouble(price);
                writeNewParkingSpace(userID, address, zipcode, lat, lon, priceDouble, openTime, closeTime);

                // Send to Fire Base
                Log.v(TAG, "Sending to Database");

                // Reset inputs
                Log.v(TAG, "Resetting Inputs");
                resetInputs();

                // Show result message
                toast("Success: Parking Space Added" );

            }
        });

        //Coordinates Button // Confirm Button
        btnCoordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for empty fields
                if(!checkForEmptyFields())
                {
                    return;
                }

                Geocoder geocoder = new Geocoder(v.getContext());
                List<Address> addresses;

                try{
                    address =                   editTextStreet.getText().toString().trim();
                    address = address + ", " +  editTextCity.getText().toString().trim();
                    address = address + ", " +  stateSpinner.getSelectedItem().toString();

                    addresses = geocoder.getFromLocationName(address, 1);

                    if(addresses.size() > 0) {
                        //Get Strings
                        lat_Str = Double.toString(addresses.get(0).getLatitude());
                        lng_Str = Double.toString(addresses.get(0).getLongitude());

                        //Set Text
                        editTextLatitude.setText(lat_Str);
                        editTextLongitude.setText(lng_Str);

                        //Set Boolean
                        coordinatesReceived = true;
                    }
                    else{
                        //Set Text //Not a valid address
                        editTextStreet.setText("Not Valid");
                        editTextCity.setText("Not Valid");
                        editTextLatitude.setText("?");
                        editTextLongitude.setText("?");
                    }
                }catch(Exception e)
                {
                    Log.v(TAG, ":error getting coordinates");
                }
            }
        });
    }


    private void writeNewParkingSpace(String ownerID, String Address,String zipCode, Double latitude, Double longitude, Double price, String openTime, String closeTime )
    {
        Log.v(TAG, "writeNewParkingSpace()");


        // Send Parking Space to FireBase
        ParkingSpace ps = new ParkingSpace(ownerID, Address,zipCode, latitude, longitude, price, openTime, closeTime);
        Log.v(TAG, "Sending to mDatabase");


        //Send To Firebase
        DatabaseReference zipRef  = mDatabase.child("ParkingSpaces").child(zipCode);
        DatabaseReference newPS_Ref = zipRef.push();
        newPS_Ref.setValue(ps);


        // Get the unique ID generated by a push()
        String newPS_Id = newPS_Ref.getKey();
        Log.v(TAG, "newPS_Id: " + newPS_Id);


        //Store ID in parking space
        mDatabase.child("ParkingSpaces").child(zipCode).child(newPS_Id).child("id").setValue(newPS_Id);
    }

    private void resetInputs()
    {
        Log.v(TAG, "resetInputs()");
        editTextStreet.setText("");
        editTextCity.setText("");
        stateSpinner.setSelection(0);
        editTextZipCode.setText("");
        editTextPrice.setText("");
        openTimeSpinner.setSelection(0);
        closeTimeSpinner.setSelection(1);
        openTimeAMPMSpinner.setSelection(0);
        closeTimeAMPMSpinner.setSelection(1);
        editTextLatitude.setText("");  //To Be Hidden
        editTextLongitude.setText(""); //To Be Hidden
        coordinatesReceived = false;
    }

    private void toast(CharSequence cs)
    {
        Context context = getApplicationContext();
        CharSequence text = cs;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private Boolean checkForEmptyFields()
    {
        Log.v(TAG, "checkForEmptyFields()");

        //Get User Input from text fields
        street   = editTextStreet.getText().toString().trim();
        city     = editTextCity.getText().toString().trim();
        state    = stateSpinner.getSelectedItem().toString();
        address = street + ", " + city + ", " + state;

        zipcode = editTextZipCode.getText().toString().trim();
        price = editTextPrice.getText().toString().trim();

        openTime = openTimeSpinner.getSelectedItem().toString()+ openTimeAMPMSpinner.getSelectedItem().toString();
        closeTime = closeTimeSpinner.getSelectedItem().toString()+ closeTimeAMPMSpinner.getSelectedItem().toString();

        //Check Street
        if (street.equals(""))
        {
            Log.v(TAG, "Invalid Street");
            toast("Enter Street" );
            return false;
        }

        if (city.equals(""))
        {
            Log.v(TAG, "Invalid City");
            toast("Enter City" );
            return false;
        }

        if (zipcode.equals(""))
        {
            Log.v(TAG, "Invalid Zipcode");
            toast("Enter Zipcode" );
            return false;
        }

        if (price.equals(""))
        {
            Log.v(TAG, "Invalid Price");
            toast("Enter Price" );
            return false;
        }

        return true;
    }
}
