package com.example.openparking;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyRatingActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fUser;
    private User mUser;

    private RatingBar mRatingBar;
    private Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rating);

        mRatingBar = (RatingBar) findViewById(R.id.myRatingBar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        fUser = firebaseAuth.getCurrentUser();
        mUser = new User();
        retrieveUser();




        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayRating()
    {
        double rating = mUser.getContributorRating();

        if(rating >= 4.5)
        {
            mRatingBar.setNumStars(5);
        }
        else if(rating >= 3.5)
        {
            mRatingBar.setNumStars(4);
        }
        else if(rating >= 2.5)
        {
            mRatingBar.setNumStars(3);
        }
        else if(rating >= 1.5)
        {
            mRatingBar.setNumStars(2);
        }
        else if(rating >= 0.5)
        {
            mRatingBar.setNumStars(1);
        }
        else
        {
            mRatingBar.setNumStars(0);
        }
    }

    private void retrieveUser()
    {
        DatabaseReference ref = mDatabase.child("users").child(fUser.getUid());

        //Retrieve seller information using ps.getOwnerID()
        readData(ref, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                Log.d("TAG", "Read successful, Owner: " + mUser.toString());
                displayRating();
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
}
