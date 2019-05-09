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

    //Latitude and Longitude
    private EditText editTextLatitude;    // To Be Hidden
    private EditText editTextLongitude;   // To Be Hidden

    //Price and Times
    private EditText editTextCost;
    private Spinner  openTimeSpinner;
    private Spinner  closeTimeSpinner;
    private Spinner  openTimeAMPMSpinner;
    private Spinner  closeTimeAMPMSpinner;

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
    String lat_Str;
    String lng_Str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking_space);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         street = "";
         city = "";
         state = "";
         address = "";
         zipcode = "";
         lat_Str = "";
         lng_Str = "";

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Street, City, State and ZipCode
        editTextStreet      = findViewById(R.id.editTextStreet);
        editTextCity        = findViewById(R.id.editTextCity);
        stateSpinner        = findViewById(R.id.spinner_states);
        editTextZipCode     = findViewById(R.id.editTextZipCode);

        //Latitude and Longitude
        editTextLatitude    = findViewById(R.id.editTextLatitude); // To Be  Hidden
        editTextLongitude   = findViewById(R.id.editTextLongitude); // To Be Hidden

        //Price and Times
        editTextCost        = findViewById(R.id.editTextCost);
        openTimeSpinner     = findViewById(R.id.spinner_open_time);
        closeTimeSpinner    = findViewById(R.id.spinner_close_time);
        openTimeAMPMSpinner = findViewById(R.id.spinner_open_time_ampm);
        closeTimeAMPMSpinner= findViewById(R.id.spinner_close_time_ampm);

        //Buttons
        btnAddPicture = findViewById(R.id.bt_addpicture);
        btnCoordinate = findViewById(R.id.btnGetCoords);//
        btnSend = findViewById(R.id.btnSend);


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


        // Get userID
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        FirebaseUser fuser = firebaseAuth.getCurrentUser();
        userID = fuser.getUid();

        init();//Set OnClickListeners
    }

    private void init() {

        // Send Button
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked Send Button");

                // Toast message
                //toast("Clicked Button Address" );

                //Check for empty fields
                if(checkForEmptyFields())
                {
                    //Get Coordinates



                }
                else
                {
                    return;
                }

                // Check Street Address Validity
                 Log.v(TAG, "Verifying(" + address + ")");

                Boolean receivedCoordinates = false;
                Geocoder geocoder = new Geocoder(v.getContext(), Locale.getDefault());

                List<Address> addresses;
                try{
                    int maxResults = 5;
                    Log.v(TAG, "trying : geocoder.getFromLocationName(" + address + ", " + maxResults + ")" );
                    addresses = geocoder.getFromLocationName(address, maxResults);

                    if(addresses.size() > 0) {
                        Log.v(TAG, "address verified");
                        lat_Str = Double.toString(addresses.get(0).getLatitude());
                        lng_Str = Double.toString(addresses.get(0).getLongitude());

                        editTextLatitude.setText(lat_Str);   // To Be Hidden
                        editTextLongitude.setText(lng_Str); // To Be Hidden
                        //return true;
                        receivedCoordinates = true;
                    }
                    else{
                        //Not a valid address
                        Log.v(TAG, "address not valid");
                        editTextStreet.setText("Not Valid");
                        editTextCity.setText("Not Valid");
                        //return false;
                        receivedCoordinates = true;

                    }
                }catch(Exception e)
                {
                    Log.e(TAG, ":error geocoder failed");
                    //return false;
                    receivedCoordinates = false;
                }

                if(receivedCoordinates)
                {
                    Log.v(TAG, "Received Coordinates");

                    //Get Cost and Times
                    Log.v(TAG, "Get cost and Time");
                    final Double costDouble = Double.parseDouble(cost);
                    final String open = openTimeSpinner.getSelectedItem().toString() + openTimeAMPMSpinner.getSelectedItem().toString();
                    final String close = closeTimeSpinner.getSelectedItem().toString() + closeTimeAMPMSpinner.getSelectedItem().toString();

                    // Send to Fire Base
                    Log.v(TAG, "Sending to Database");
                    writeNewParkingSpace(userID, address, zipcode, latitude, longitude, costDouble, open, close);

                    // Reset inputs
                    Log.v(TAG, "Resetting Inputs");
                    resetInputs();

                    // Show result message
                    toast("Success: Parking Space Added" );
                }
                else
                {
                    Log.v(TAG, "Invalid Address");

                    // Reset input boxes
                    resetInputs();

                    // Show result message
                    toast("Invalid Address" );
                }
            }
        });

        //Coordinates Button // Confirm Button
        btnCoordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    private Boolean getCoords(String address, Geocoder geocoder)
    {
        //Geocoder geocoder = new Geocoder(v.getContext());


        List<Address> addresses;
        try{
            Log.v(TAG, "trying : geocoder.getFromLocationName(" + address + ", 1)");
            addresses = geocoder.getFromLocationName(address, 5);

            if(addresses.size() > 0) {
                Log.v(TAG, "address verified");
                latitude= addresses.get(0).getLatitude();
                longitude= addresses.get(0).getLongitude();
                //editTextLatitude.setText(Double.toString(latitude));   // Hidden
                //editTextLongitude.setText(Double.toString(longitude)); // Hidden
                return true;
            }
            else{
                //Not a valid address
                Log.v(TAG, "address not valid");
                editTextStreet.setText("Not Valid");
                editTextCity.setText("Not Valid");
                return false;
            }
        }catch(Exception e)
        {
            Log.e(TAG, ":error geocoder failed");
            return false;
        }
    }

    private void writeNewParkingSpace(String ownerID, String Address,String zipCode, Double latitude, Double longitude, Double cost, String openTime, String closeTime )
    {
        Log.v(TAG, "writeNewParkingSpace()");


        // Send Parking Space to FireBase
        ParkingSpace ps = new ParkingSpace(ownerID, Address,zipCode, latitude, longitude, cost, openTime, closeTime);
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
        //editTextLatitude.setText("");  //Hidden
        //editTextLongitude.setText(""); //Hidden
        editTextCost.setText("");
        openTimeSpinner.setSelection(0);
        closeTimeSpinner.setSelection(1);
        openTimeAMPMSpinner.setSelection(0);
        closeTimeAMPMSpinner.setSelection(1);
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

        //Get address from user Input
        Log.v(TAG, "Getting input from editText");
        String street   = editTextStreet.getText().toString().trim();
        String city     = editTextCity.getText().toString().trim();
        String state    = stateSpinner.getSelectedItem().toString();
        String address = street + ", " + city + ", " + state;

        String zipcode = editTextZipCode.getText().toString().trim();
        String cost = editTextCost.getText().toString().trim();

        //Check Street
        if (street.equals(""))
        {
            Log.v(TAG, "Invalid Street");
            toast("Enter Street" );
            return;
        }

        if (city.equals(""))
        {
            Log.v(TAG, "Invalid City");
            toast("Enter City" );
            return;
        }

        if (zipcode.equals(""))
        {
            Log.v(TAG, "Invalid Zipcode");
            toast("Enter Zipcode" );
            return;
        }

        if (cost.equals(""))
        {
            Log.v(TAG, "Invalid Cost");
            toast("Enter Price" );
            return;
        }

    }
}
