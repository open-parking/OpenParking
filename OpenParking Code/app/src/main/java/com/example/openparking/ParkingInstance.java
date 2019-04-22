package com.example.openparking;

import android.location.Address;
import com.google.android.gms.maps.model.LatLng;
import java.util.Date;

public class ParkingInstance {

    private String sellerID;
    private String buyerID;

    private ParkingSpace parkingSpace;

    //private String desc;


    private Date startTime; //Time when vehicle parked at the address;
    private Date endTime;   //Time when vehicle left the parking spot.


    public ParkingInstance(String sellerID, ParkingSpace parkingSpace)
    {
        this.sellerID = sellerID;
        this.parkingSpace = parkingSpace;
    }


    public ParkingInstance(String sellerID, ParkingSpace parkingSpace, String preferredPaymentMethod, Date startTime, Date endTime) {
        this.sellerID = sellerID;
        this.parkingSpace = parkingSpace;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public String getSellerID()
    {
        return sellerID;
    }
    public void setSellerID(String sellerID)
    {
        this.sellerID = sellerID;
    }


    public String getBuyerID()
    {
        return buyerID;
    }
    public void setBuyerID(String buyerID)
    {
        this.buyerID = buyerID;
    }


    public ParkingSpace getParkingSpace()
    {
        return parkingSpace;
    }
    public void setParkingSpace(ParkingSpace parkingSpace)
    {
        this.parkingSpace = parkingSpace;
    }


    public Date getStartTime()
    {
        return startTime;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    public Date getEndTime()
    {
        return endTime;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String toString()
    {
        return "Seller ID: " + sellerID + ", Address: " + parkingSpace;
    }
}
