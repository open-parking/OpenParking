package com.example.openparking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class ParkingSpace implements Parcelable{

    private String id;
    private String ownerID;
    private String address;
    private String zipcode;
    private Double latitude;
    private Double longitude;
    private Double cost;
    private String openTime;
    private String closeTime;
    private Boolean reserved;

    //CSULB Latitude and Longitude
    //private final double latitude = 33.782896;
    //private final double longitude = -118.110230;

    public ParkingSpace( )
    {
        //Used by FireBase
    }

    public ParkingSpace( String id, String owner, String address, String zipcode, Double latitude, Double longitude, Double cost, String openTime, String closeTime)
    {
        this.id = id;
        this.ownerID = owner;
        this.address = address;
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.latlng = new LatLng(latitude, longitude); //LatLng not compatible with firebase
        this.cost = cost;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.reserved = false;
    }

    public ParkingSpace( String owner, String address, String zipcode, Double latitude, Double longitude, Double cost, String openTime, String closeTime)
    {
        this.id = "NOT SET";
        this.ownerID = owner;
        this.address = address;
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.latlng = new LatLng(latitude, longitude); //LatLng not compatible with firebase
        this.cost = cost;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.reserved = false;
    }

    public String getID()
    {
        return this.id;
    }

    public void setID(String ID)
    {
        this.id = ID;
    }

    public String getOwnerID()
    {
        return ownerID;
    }

    public void setOwnerID(String ID)
    {
        this.ownerID = ID;
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

    //LatLng not Compatible with firebase
    //public LatLng getLatlng()
    //{
        //return new LatLng(latitude, longitude);
        //return latlng;
        // using google's LatLng class gives problems when using firebase
        // due to no public constructor with no arguments.
    //}

    //LatLng not Compatible with firebase
    //public void setLatlng(LatLng latlng)
    //{
        //this.latlng = latlng;
        //this.latitude = latlng.latitude;
        //this.longitude = latlng.longitude;
   // }

    public void setLatlng(Double latitude, Double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getOpentime() {
        return openTime;
    }

    public void setOpentime(String openTime) {
        this.openTime = openTime;
    }

    public String getClosetime() {
        return closeTime;
    }

    public void setClosetime(String closeTime) {
        this.closeTime = closeTime;
    }

    public Boolean getReservedStatus()
    {
        return this.reserved;
    }

    public  void setReservedStatus(Boolean status)
    {
        this.reserved = status;
    }

    //---Parcelable Functions---
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(id);
        out.writeString(ownerID);
        out.writeString(address);
        out.writeString(zipcode);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        //out.writeDouble(latlng.latitude);
        //out.writeDouble(latlng.longitude);
        out.writeDouble(cost);
        out.writeString(openTime);
        out.writeString(closeTime);
        out.writeByte((byte) (reserved ? 1 : 0));     //if reserved == true, byte == 1
    }

    public ParkingSpace(Parcel in) {
        this.id         = in.readString();
        this.ownerID    = in.readString();
        this.address    = in.readString();
        this.zipcode    = in.readString();
        this.latitude   = in.readDouble();
        this.longitude  = in.readDouble();
        //Double latitude   = in.readDouble();
        //Double longitude  = in.readDouble();
        //this.latlng = new LatLng(latitude, longitude);
        this.cost       = in.readDouble();
        this.openTime   = in.readString();
        this.closeTime   = in.readString();
        this.reserved = in.readByte() != 0;     //myBoolean == true if byte != 0
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
