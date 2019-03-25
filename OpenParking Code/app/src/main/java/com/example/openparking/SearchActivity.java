package com.example.openparking;
import java.util.ArrayList;

public class SearchActivity {
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
    ArrayList<ParkingInstance> pList = new ArrayList<ParkingInstance>();
    //get current location, to sort instanced by closest
    //maybe option to show in map view??
    //need to constant refresh with new instances...
}
