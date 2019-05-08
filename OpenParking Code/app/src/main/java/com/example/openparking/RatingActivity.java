package com.example.openparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.widget.Toast;

public class RatingActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    double contributorRatingTotal;
    private FirebaseDatabase mDatabase;
    private DatabaseReference usersRef;
    private static final String TAG = "RatingActivity";


    /*public RatingActivity(FirebaseDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }*/
    public RatingActivity() {
        usersRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        final RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        final TextView mRatingScale = (TextView) findViewById(R.id.tvRatingScale);

        Button submitButton = (Button) findViewById(R.id.submit_button);
        final TextView ratingDisplayTextView = (TextView) findViewById(R.id.rating_display_text_View);


        //Use this template to get data from Firebase about classes we want to use
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        final FirebaseUser fuser = firebaseAuth.getCurrentUser();
        final User user = new User();
        user.setId(fuser.getUid());//USER ID

        final String uID = user.getId();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usersRef.child("users").addChildEventListener(
                        new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                                User mUser = new User();
                                mUser = dataSnapshot.getValue(User.class);
                                if(mUser.getId().equals(uID)) {
                                    mUser.setTimesContributorRated(mUser.getTimesContributorRated());
                                    if(mUser.getTimesContributorRated() < 1){
                                        contributorRatingTotal = mRatingBar.getRating();
                                    } else {
                                        contributorRatingTotal = mUser.getContributorRating() * mUser.getTimesContributorRated() + mRatingBar.getRating();
                                    }
                                    mUser.setContributorRating(contributorRatingTotal / (mUser.getTimesContributorRated()+1));
                                    mUser.setTimesContributorRated(mUser.getTimesContributorRated()+1);
                                    usersRef.child("users").child(uID).child("contributorRating").setValue(mUser.getContributorRating());
                                    usersRef.child("users").child(uID).child("timesContributorRated").setValue(mUser.getTimesContributorRated());
                                    ratingDisplayTextView.setText("Your average rating is: " + mUser.getContributorRating());
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                                // A comment has changed, use the key to determine if we are displaying this
                                // comment and if so displayed the changed comment.
                                //Comment newComment = dataSnapshot.getValue(Comment.class);
                                //String commentKey = dataSnapshot.getKey();

                                // ...
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                                // A comment has changed, use the key to determine if we are displaying this
                                // comment and if so remove it.
                                // commentKey = dataSnapshot.getKey();

                                // ...
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                                // A comment has changed position, use the key to determine if we are
                                // displaying this comment and if so move it.
                                //Comment movedComment = dataSnapshot.getValue(Comment.class);
                                //String commentKey = dataSnapshot.getKey();

                                // ...
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                                //Toast.makeText(mContext, "Failed to load comments.",
                                //Toast.LENGTH_SHORT).show();
                            }

                        });
                Toast.makeText(RatingActivity.this, "Thank you for your feedback", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Very bad");
                        break;
                    case 2:
                        mRatingScale.setText("Need some improvement");
                        break;
                    case 3:
                        mRatingScale.setText("Good");
                        break;
                    case 4:
                        mRatingScale.setText("Great");
                        break;
                    case 5:
                        mRatingScale.setText("Awesome. I love it");
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });
    }
}
