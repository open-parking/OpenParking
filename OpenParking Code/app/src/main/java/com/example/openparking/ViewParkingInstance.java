package com.example.openparking;


import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ViewParkingInstance extends AppCompatActivity {
    /*
        A full page view of the ParkingInstance selected

        This should contain:
            1. Address (should be able to link to MapView of this instance [load MapView with search parameters?])
            2. Image, if any
            3. Author (link to their profile)
            4. Time of availability (schedule and dates)
            5. Author's preferred payment method details
            6. Price
            7. Author's description of the ParkingInstance
            8. Author's contributor rating
            9. Distance from user (directions?)
            10. Big green "Reserve Parking" button
            11. Back arrow on upper left corner which takes them back to whatever they were doing before
     */
    private static final String TAG = "ViewParkingInstance";
    ParkingSpace ps;
    TextView address;
    TextView zip;
    TextView openTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Toast.makeText(this, "View Started", Toast.LENGTH_SHORT).show();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewparkinginstance);


        ps = new ParkingSpace();
        address = (TextView) findViewById(R.id.viewAddress);
        zip = (TextView) findViewById(R.id.viewZipCode);
        openTime = (TextView) findViewById(R.id.viewOpenTime);

        getParkingSpaceData();
        showParkingSpaceData();

    }

    private void getParkingSpaceData()
    {
        Log.v(TAG, "getParkingSpaceData: ");


        //In new Activity
        // 4. getIntent()
        Intent intent = getIntent();

        // 5. intent.getStringExtra(String key)
        //intent.getParcelableExtra()
        ps = intent.getParcelableExtra("parkingInstance");

    }

    private void showParkingSpaceData()
    {
        Log.v(TAG, "showParkingSpaceData: ");
        Log.v(TAG, "address: " + ps.getAddress());

        if(!ps.equals(null))
        {
            address.setText(ps.getAddress());
            zip.setText(ps.getZipcode());
            openTime.setText(ps.getOpenTime());
        }


    }


}
