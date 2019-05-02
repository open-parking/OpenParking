package com.example.openparking;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openparking.Config.Config;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

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
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);


    private DatabaseReference mDatabase;

    ParkingSpace ps;
    TextView address;
    TextView zip;
    TextView latitude;
    TextView longitude;
    TextView cost;
    TextView openTime;
    TextView closeTime;
    TextView ownerName;

    String ownerID;
    String ownerName_str;

    Button btnBook;

    String costStr; //var passed to PaymentDetail Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewparkinginstance);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ps = new ParkingSpace();

        address = (TextView) findViewById(R.id.viewAddress);
        zip = (TextView) findViewById(R.id.viewZipCode);
        latitude = (TextView) findViewById(R.id.viewLatitude);
        longitude = (TextView) findViewById(R.id.viewLongitude);

        cost = (TextView) findViewById(R.id.viewCost);
        openTime = (TextView) findViewById(R.id.viewOpenTime);
        closeTime = (TextView) findViewById(R.id.viewCloseTime);

        ownerName = (TextView) findViewById(R.id.viewOwner);
        ownerID = "NOT SET";
        ownerName_str = "NOT SET";
        costStr = "NOT SET";

        getParkingSpaceData();
        showParkingSpaceData();

        //Start PayPal Service
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        btnBook = (Button)findViewById(R.id.btn_book);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
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
        ownerID = ps.getOwnerID();

        //---------------------------------

        //Request owner info from data base
        //ownerName_str =
        // Query userQuery = mDatabase.child("users").child(ownerID).child("name");
        ///User owner = userQuery.

        getOwnerDataFromFireBase();
    }

    private void getOwnerDataFromFireBase()
    {
        mDatabase.child("users").child(ownerID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ...
                Log.v(TAG, "onDataChange()");

                User owner = dataSnapshot.getValue(User.class);
                ownerName_str = owner.name;
                ownerName.setText(ownerName_str);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
                Log.v(TAG, "onCancelled()");
                ownerName_str = "DataBase error";
            }
        });
    }

    private void showParkingSpaceData()
    {
        Log.v(TAG, "showParkingSpaceData: ");
        Log.v(TAG, "address: " + ps.getAddress());

        if(!ps.equals(null))
        {
            address.setText(ps.getAddress());
            zip.setText(ps.getZipcode());

            latitude.setText("" + ps.getLatitude());
            longitude.setText("" + ps.getLongitude());

            cost.setText("" + ps.getCost());
            openTime.setText(ps.getOpentime());
            closeTime.setText(ps.getClosetime());

            ownerName.setText(ownerName_str);
        }
    }

    private void processPayment()
    {
        costStr = ps.getCost().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(ps.getCost()),"USD",
                "Pay For Parking", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", costStr));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }
}
