package com.example.openparking;

import android.location.Address;
import com.google.android.gms.maps.model.LatLng;
import java.util.Date;

public class ParkingInstance {

    private String sellerID;
    private String buyerID;

    private Vehicle vehicle;

    private String parkingSpaceID;

    //private String desc;

    private String preferredPaymentMethod;

    private Date startTime; //Time when vehicle parked at the address;
    private Date endTime;   //Time when vehicle left the parking spot.

    public ParkingInstance()
    {
        sellerID = "No Seller";
        buyerID = "No Buyer";
        vehicle = new Vehicle();
        parkingSpaceID = "No Parking Space";
        preferredPaymentMethod = "No preferred payment method";
        startTime = new Date();
        endTime = new Date();
    }



    public ParkingInstance(String sellerID, String parkingSpaceID)
    {
        this.sellerID = sellerID;
        this.parkingSpaceID = parkingSpaceID;
    }

    public ParkingInstance(String sellerID, String parkingSpaceID, String preferredPaymentMethod)
    {
        this.sellerID = sellerID;
        this.parkingSpaceID = parkingSpaceID;
        this.preferredPaymentMethod = preferredPaymentMethod;
    }

    public ParkingInstance(String sellerID, String parkingSpaceID, String preferredPaymentMethod, Date startTime, Date endTime) {
        this.sellerID = sellerID;
        this.parkingSpaceID = parkingSpaceID;
        this.preferredPaymentMethod = preferredPaymentMethod;
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


    public Vehicle getVehicle()
    {
        return vehicle;
    }
    public void setVehicle(Vehicle vehicle)
    {
        this.vehicle = vehicle;
    }


    public String getParkingSpace()
    {
        return parkingSpaceID;
    }
    public void setParkingSpace(String parkingSpace)
    {
        this.parkingSpaceID = parkingSpaceID;
    }


    public String getPreferredPaymentMethod()
    {
        return preferredPaymentMethod;
    }
    public void setPreferredPaymentMethod(String preferredPaymentMethod)
    {
        this.preferredPaymentMethod = preferredPaymentMethod;
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
}
