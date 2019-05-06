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

public class RatingActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    double userRatingTotal;
    private FirebaseDatabase mDatabase;
    private DatabaseReference usersRef;

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


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child("users").child(userRatingTotal).addChildEventListener(
                        new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                                User mUser = new User();
                                mUser.setUserRating(userRatingTotal);
                                String uID = user.getId();
                                mUser.setId(uID);
                                mUser= dataSnapshot.getValue(User.class);

                                    Log.d(TAG, "onChildAdded: " +  "ps is good");

                                    parkingSpaceList.add(ps);

                                    String address = ps.getAddress();
                                    String hours = "Hours: " + ps.getOpentime() + " to " + ps.getClosetime();

                                    MarkerOptions mo = new MarkerOptions();
                                    mo.position(new LatLng(ps.getLatitude(), ps.getLongitude() ) );
                                    mo.title(address);
                                    mo.snippet(hours);
                                    mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                    mo.flat(true);

                                    //Add marker to map AND get its ID
                                    String markerID = mMap.addMarker(mo).getId();

                                    parkingSpaceHashMap.put(markerID, ps);
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

                //ratingDisplayTextView.setText("Your rating is: " + mRatingBar.getRating());
                userRatingTotal = user.getUserRating() * user.getTimesUserRated() + mRatingBar.getRating();
                user.setTimesUserRated(user.getTimesUserRated() + 1);
                user.setUserRating(userRatingTotal / user.getTimesUserRated());
                ratingDisplayTextView.setText("Your average rating is: " + user.getUserRating());
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
