package com.example.openparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;

public class RatingActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    double userRatingTotal;
    private FirebaseDatabase mDatabase;
    private DatabaseReference usersRef;

    public RatingActivity(FirebaseDatabase mDatabase) {
        this.mDatabase = mDatabase;
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
        FirebaseUser fuser = firebaseAuth.getCurrentUser();
        final User user = new User();
        user.setId(fuser.getUid());//USER ID


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ratingDisplayTextView.setText("Your rating is: " + mRatingBar.getRating());
                userRatingTotal = user.getUserRating() * user.getTimesUserRated() + mRatingBar.getRating();
                user.setTimesUserRated(user.getTimesUserRated() + 1);
                user.setUserRating(userRatingTotal / user.getTimesUserRated());
                ratingDisplayTextView.setText("Your average rating is: " + user.getUserRating());

                User mUser = new User();
                mUser.setUserRating(userRatingTotal);
                String uID = user.getId();
                mUser.setId(uID);
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
