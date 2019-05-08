package com.example.openparking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.openparking.OnGetDataListener;

import java.util.HashMap;

public class CreateParkingInstanceActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseUser buyer;

    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;
    private DatabaseReference testRef;

    private ParkingSpace parkingSpace;
    private ParkingInstance parkingInstance;

    private String parkingSpaceID;
    private String sellerID;
    private String buyerID;

    //hashmap to be used to pass parking space object to confirmation activity
    private HashMap<String, ParkingSpace> parkingSpaceHashMap;
    private Intent complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parking_instance);

        parkingSpace = new ParkingSpace();

        Intent intent = getIntent();
        parkingSpace = intent.getParcelableExtra("parkingSpace");

        firebaseAuth = FirebaseAuth.getInstance();
        buyer = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference();


        parkingSpaceID = parkingSpace.getID();
        sellerID = parkingSpace.getOwnerID();
        buyerID = buyer.getUid();

        parkingInstance = new ParkingInstance();
        parkingInstance.setParkingSpaceID(parkingSpaceID);
        parkingInstance.setSellerID(sellerID);
        parkingInstance.setBuyerID(buyerID);

        writeData();



        complete = new Intent(CreateParkingInstanceActivity.this, PurchaseCompleteActivity.class);
        complete.putExtra("parkingSpace", parkingSpace);
        // After a parking instance object is created and pushed to the database,
        // start confirmation activity
        startActivity(complete);

        finish();
        /*
        // READING from database and retrieving a parking space object
        testRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parkingSpace = dataSnapshot.getValue(ParkingSpace.class);

                Log.d("TAG", "Database read successful! " + parkingSpace.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "Database read failed");
            }
        });
        */

        /*
        testRef = ref.child("ParkingSpaces").child("90815").child("-Lb0SP_Qw2HzEbnDVCxm");
        String temp = testRef.getKey();
        readData(testRef, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                parkingInstance.setParkingSpaceID(dataSnapshot.getKey());
                Log.d("TAG", "Database read successful! " + parkingInstance.getParkingSpaceID());
                //parkingInstance = new ParkingInstance(mUser.getUid(), parkingSpace);

                writeData();
                readUser();
            }

            @Override
            public void onStart() {
                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Log.d("ONFAILURE", "Failed");
            }
        });







        //DISPLAY NEWLY CREATED PARKING INSTANCE IN THE UI
    }



        /*
    public void readData(DatabaseReference ref, final OnGetDataListener listener){
        System.out.println("Reached READDATA function");
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError);
            }
        });
    }

    */
    }

    public void writeData()
    {
        // WRITING to database after creating a parking instance object
        testRef = ref.child("ParkingInstances").child(parkingInstance.getParkingSpaceID());
        testRef.setValue(parkingInstance);
        testRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", "Database write successful! Instance: " + parkingInstance.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "Database write failed");
            }
        });
    }

    /*
    public void readUser()
    {
        //READING from firebase by using the sellerID from parkingInstance to retrieve a user
        testRef = ref.child("users").child(parkingInstance.getSellerID());
        testRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                seller = dataSnapshot.getValue(User.class);
                Log.d("TAG", "Read Successful! The seller is " + seller.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "Read Failed, seller unknown...");
            }
        });








    }
    */
}
