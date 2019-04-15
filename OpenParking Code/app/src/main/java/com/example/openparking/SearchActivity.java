package com.example.openparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import android.location.Address;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;

public class SearchActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    /*
        A list view of ParkingInstances
        Should display a scrolling view with up to 6 instancesvisible at a time.
        Tile data per Instance:
            Primary image
            Location
            Availability
            Price
            Rating
            Preferred Payment Method (icon)

        SHOULD BE SORTABLE BY:
            Price
            Rating
            Preferred Payment Method (icon)

        When a tile is selected, should call ViewParkingInstance activity like how we should do for map view

        HYPOTHETICALLY Firebase will be able to give us data that is already sorted, so we will go with that.

        All we need to do then is only get data of instances close to user's latlongitude

        So then:
            1) get sorted firebase data
            2) build list of relatively close parking instances
            3) show list to user
     */

    //Don't think we actually need ParkingInstance arraylists, can get data as strings and ints...
    ArrayList<ParkingInstance> pList = new ArrayList<ParkingInstance>(); //data from firebase, most likely unsorted
    ArrayList<ParkingInstance> vList = new ArrayList<ParkingInstance>(); //data we've shown to the user
    int tilesToDisplay = 6; //6-8?
    int currentIndex = 1;

    /*
        1) get sorted data from firebase and store in plist
        2) build vlist of parkinginstances that are relatively close to user
        3) when next is called, repeat 1 and 2 as needed
     */

    void loadResults(int arrow) {

            /*When the user presses the arrow buttons, load tile data (add to plist?)
              if the right arrow is pressed, this function will be called with a positive integer.
                then get more data and update our viewable list
              if the left arrow is pressed, this function will be called with a negative integer.
                then go back into the data we previously had and update our viewable list to reflect it
            */
    }

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

    }

    //more info should load ViewParkingInstanceActivity
    //will getting new data from firebase conflict with our already sorted instances?
}
