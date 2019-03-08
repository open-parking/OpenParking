package com.example.openparking;


import android.location.Address;
import com.google.android.gms.maps.model.LatLng;
import java.util.Date;


public class ParkingInstance {

    private User user;
    private Vehicle vehicle;
    private Address address;
    private LatLng latlng; //Possibly redundant, could be derived from address variable

    private Date starttime; //Time when vehicle parked at the address;
    private Date endtime;   //Time when vehicle left the parking spot.

    //CSULB Latitude and Longitude
    private final double latitude = 33.782896;
    private final double longitude = -118.110230;


/*
    public ParkingInstance( Address address )
    {
        //this.contributor = contributor;
        //this.user = user;
        //this.vehicle = vehicle;
        this.address = address;


        latlng = new LatLng(latitude, longitude);
        this.starttime = new  Date();
        endtime = new  Date();
    }
    */


    public ParkingInstance( User user, Vehicle vehicle, Address address )
    {
        this.user = user;
        this.vehicle = vehicle;
        this.address = address;


        latlng = new LatLng(latitude, longitude);
        this.starttime = new  Date();
        endtime = new  Date();
    }

}
