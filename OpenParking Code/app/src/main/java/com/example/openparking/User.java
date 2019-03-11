package com.example.openparking;

import android.location.Address;

public class User {
    String firstName;
    String lastName;
    String email;
    double userRating;
    int timesUserRated;
    String id;
    boolean isContributor;
    String acceptedPayment;
    Address address;
    double contributorRating;
    int timesContributorRated;

    public User() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.userRating = 0;
        this.timesUserRated = 0;
        this.id = "";
        this.isContributor = false;
        this.acceptedPayment = "";
        //this.address = address;
        this.contributorRating = 0;
        this.timesContributorRated = 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", userRating=" + userRating +
                ", timesUserRated=" + timesUserRated +
                ", id='" + id + '\'' +
                ", isContributor=" + isContributor +
                ", acceptedPayment='" + acceptedPayment + '\'' +
                ", address=" + address +
                ", contributorRating=" + contributorRating +
                ", timesContributorRated=" + timesContributorRated +
                '}';
    }

    public boolean isContributor() {
        return isContributor;
    }

    public void setContributor(boolean contributor) {
        isContributor = contributor;
    }

    public String getAcceptedPayment() {
        return acceptedPayment;
    }

    public void setAcceptedPayment(String acceptedPayment) {
        this.acceptedPayment = acceptedPayment;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
