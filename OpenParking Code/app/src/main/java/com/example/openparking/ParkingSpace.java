package com.example.openparking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class ParkingSpace implements Parcelable{

    private String address;
    private String zipcode;
    private Double latitude;
    private Double longitude;
    private Double cost;
    private String openTime;
    private String closeTime;

    //CSULB Latitude and Longitude
    //private final double latitude = 33.782896;
    //private final double longitude = -118.110230;

    public ParkingSpace( )
    {
        //Used by FireBase
    }

    public ParkingSpace( String address, String zipcode, Double latitude, Double longitude, Double cost, String openTime, String closeTime)
    {
        this.address = address;
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cost = cost;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LatLng getLatLng()
    {
        return new LatLng(latitude, longitude);
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    //---Parcelable Functions---
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(address);
        out.writeString(zipcode);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeDouble(cost);
        out.writeString(openTime);
        out.writeString(closeTime);
    }

    public ParkingSpace(Parcel in) {
        this.address    = in.readString();
        this.zipcode    = in.readString();
        this.latitude   = in.readDouble();
        this.longitude  = in.readDouble();
        this.cost       = in.readDouble();
        this.openTime   = in.readString();
        this.openTime   = in.readString();
    }

    public static final Parcelable.Creator<ParkingSpace> CREATOR
            = new Parcelable.Creator<ParkingSpace>() {

        public ParkingSpace createFromParcel(Parcel in)
        {
            return new ParkingSpace(in);
        }

        public ParkingSpace[] newArray(int size)
        {
            return new ParkingSpace[size];
        }
    };
    //---END Parcelable Functions---

}
