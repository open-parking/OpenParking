package com.example.openparking;

public class User {
    String name;
    String email;
    double userRating;
    int timesUserRated;
    String id;
    String acceptedPayment;
    double contributorRating;
    int timesContributorRated;

    Vehicle vehicle;

    public User() {
        this.name = "";
        this.email = "";
        this.userRating = 0;
        this.timesUserRated = 0;
        this.id = "";
        this.acceptedPayment = "";
        this.contributorRating = 0;
        this.timesContributorRated = 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "User{" +
                "name='" + name +
                ", email='" + email + '\'' +
                ", userRating=" + userRating +
                ", timesUserRated=" + timesUserRated +
                ", id='" + id + '\'' +
                ", acceptedPayment='" + acceptedPayment + '\'' +
                ", contributorRating=" + contributorRating +
                ", timesContributorRated=" + timesContributorRated +
                '}';
    }

    public String getAcceptedPayment() {
        return acceptedPayment;
    }

    public void setAcceptedPayment(String acceptedPayment) {
        this.acceptedPayment = acceptedPayment;
    }

    public double getContributorRating() {
        return contributorRating;
    }

    public void setContributorRating(double contributorRating) {
        this.contributorRating = contributorRating;
    }

    public int getTimesContributorRated() {
        return timesContributorRated;
    }

    public void setTimesContributorRated(int timesContributorRated) {
        this.timesContributorRated = timesContributorRated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTimesUserRated() {
        return timesUserRated;
    }

    public void setTimesUserRated(int timesUserRated) {
        this.timesUserRated = timesUserRated;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Vehicle getVehicle()
    {
        return vehicle;
    }

    public void setVehicle(String brand, String model, String year, String color)
    {
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setYear(year);
        vehicle.setColor(color);
    }
    public void setVehicle(Vehicle vehicle)
    {
        this.vehicle = vehicle;
    }
}
