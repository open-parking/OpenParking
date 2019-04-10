package com.example.openparking;


import android.location.Address;
import com.google.android.gms.maps.model.LatLng;
import java.util.Date;


public class ParkingInstance {

    private User user;
    private Vehicle vehicle;
    private Address address;
    private LatLng latlng; //Possibly redundant, could be derived from address variable

    private int price;
    private String desc;
    private Date availabilityStart; //Time when it is available
    private Date availabilityEnd; //Time when availability ends

    //TODO: scheduling throughout the week?

    private String preferredPaymentMethod;

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


        this.latlng = new LatLng(latitude, longitude);
        this.starttime = new  Date();
        endtime = new  Date();
    }

    // This constructor only initializes the Latitude and Longitude.
    // Used for testing purposes in MapsActivity
    public ParkingInstance(LatLng coords )
    {
        this.user = null;
        this.vehicle = null;
        this.address = null;


        this.latlng = coords;
        this.starttime = new  Date();
        this.endtime = new  Date();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getAvailabilityStart() {
        return availabilityStart;
    }

    public void setAvailabilityStart(Date availabilityStart) {
        this.availabilityStart = availabilityStart;
    }

    public Date getAvailabilityEnd() {
        return availabilityEnd;
    }

    public void setAvailabilityEnd(Date availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }

    public String getPreferredPaymentMethod() {
        return preferredPaymentMethod;
    }

    public void setPreferredPaymentMethod(String preferredPaymentMethod) {
        this.preferredPaymentMethod = preferredPaymentMethod;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
