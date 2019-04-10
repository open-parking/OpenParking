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
        Categories: Price, Distance, Rating, New, ... (time of availability)
        Should display 6-8 instances at a time with a scrolling view
        Tile data per Instance:
            Primary image
            Location
            Availability
            Price
            Distance from user
            Rating
            Preferred Payment Method (icon)

        When a tile is selected, should show expanded view like how we do for map view

        Questions: How will we get ParkingInstance data from Firebase and show a sorted list to the user?
                       (a) Keep multiple versions of list already sorted in Firebase, can just call it
                       (b) Get data from Firebase and sort and show to user (can new data conflict with this? have a refresh button?)
                       (c) Precomputation or data delivery before calling views to load data even faster? (start getting nearby ParkingInstance data at login?)
                   Can views be done faster? (Precomputation or algorithmic implementations)
     */

    //2 lists one for data storage, one for viewed instances, maybe another for sort?
    ArrayList<ParkingInstance> pList = new ArrayList<ParkingInstance>();
    ArrayList<ParkingInstance> vList = new ArrayList<ParkingInstance>();
    int tilesToDisplay = 6; //6-8?
    int currentTile = 0;

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

        /*
            We might need to keep track of where the user is at in terms of scrolling tiles, so we can
            see if we need to load more tiles? Do we want a tile class or something?
        */
        //int loadMoreTiles(int currentTile) {

            /*Did the user scroll so that there are only n left loaded tiles?
                    Then we should get more tile data, load it, and update currentTile and arraylists so
            we can all this automatically.*/

            //return currentTile + 20;
        //}


    }

    //more info should load ViewParkingInstanceActivity
    //get current location, to sort instanced by closest
    //maybe option to show in map view??
    //need to constant refresh with new instances...
}
