package com.example.openparking;

import android.location.Address;

public class Contributor {
    User user;
    String acceptedPayment;
    Address address;
    double contributorRating;
    int timesContributorRated;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "Contributor{" +
                "user=" + user +
                ", acceptedPayment='" + acceptedPayment + '\'' +
                ", address=" + address +
                ", contributorRating=" + contributorRating + '\'' +
                ", timesContributorRated=" + timesContributorRated + '\'' +
                '}';
    }
}
