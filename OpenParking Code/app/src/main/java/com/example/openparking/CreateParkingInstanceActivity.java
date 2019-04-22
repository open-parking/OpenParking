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

public class CreateParkingInstanceActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;
    private DatabaseReference testRef;

    private ParkingSpace parkingSpace;
    private ParkingInstance parkingInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parking_instance);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference();
        testRef = ref.child("ZipCodes").child("90815").child("-Lb0SP_Qw2HzEbnDVCxm");

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


        FirebaseUser mUser = firebaseAuth.getCurrentUser();
        parkingInstance = new ParkingInstance(mUser.getUid(), parkingSpace);
        if(parkingInstance.getSellerID() == mUser.getUid())
        {
            Log.d("TAG", "Parking instance creation successful! " + parkingInstance.toString());
        }
        else
        {
            Log.d("TAG", "Parking instance creation failed. ");
        }

        //DISPLAY NEWLY CREATED PARKING INSTANCE IN THE UI
    }
}
