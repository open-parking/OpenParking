package com.example.openparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.Address;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import java.util.Locale;

import android.location.Geocoder;

public class CreateParkingInstanceActivity extends AppCompatActivity {
    Address address;
    User user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.TODO);

        //Use this template to get data from Firebase about classes we want to use
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        FirebaseUser fuser = firebaseAuth.getCurrentUser();
        final User user = new User();
        user.setId(fuser.getUid());

        // asking them for info
        //ok great, what's your address?
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        double latitude = 1.531;
        double longitude = 1.5234;
        //later on do some fancy stuff to get latlong for here, for now this is a dummy variable(s)
        try{
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        }catch (Exception e)
        {

        }


        //lets save the address and the lat long of it

        //upload a picture?

        //availablity dates?

        //description?

        //preferred payment, have one? if not, set one?

        //price?

        //confirm?

        //upload to firebase and show them how an expanded view would look like


    }


}
