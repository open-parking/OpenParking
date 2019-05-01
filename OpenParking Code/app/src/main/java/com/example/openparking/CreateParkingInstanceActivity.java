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
import com.google.firebase.database.ValueEventListener;

import com.example.openparking.OnGetDataListener;

public class CreateParkingInstanceActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseUser mUser;

    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;
    private DatabaseReference testRef;

    private ParkingSpace parkingSpace;
    private ParkingInstance parkingInstance;
    private User seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parking_instance);

        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference();
        testRef = ref.child("ZipCodes").child("90815").child("-Lb0SP_Qw2HzEbnDVCxm");

        parkingSpace = new ParkingSpace();
        parkingInstance = new ParkingInstance();

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


        readData(testRef, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                parkingSpace = dataSnapshot.getValue(String.class);
                Log.d("TAG", "Database read successful! " + parkingSpace.toString());
                parkingInstance = new ParkingInstance(mUser.getUid(), parkingSpace);

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

    public void writeData()
    {
        // WRITING to database after creating a parking instance object
        testRef = ref.child("parkingInstances");
        testRef.child(parkingInstance.getParkingSpace().getAddress()).setValue(parkingInstance);
        testRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", "Database write successful! Address = " + parkingSpace.getAddress() + ", instance: " + parkingInstance.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "Database write failed");
            }
        });
    }

    public void readUser()
    {
        //READING from firebase by using the sellerID from parkingInstance to retrieve a user
        ref.child("users").child(parkingInstance.getSellerID());
        ref.addValueEventListener(new ValueEventListener() {
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

}
