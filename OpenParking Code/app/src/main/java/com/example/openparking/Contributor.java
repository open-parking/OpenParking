package com.example.openparking;

import android.location.Address;

public class Contributor {
    User user;
    String acceptedPayment;
    Address address;

    public Contributor(User user, String acceptedPayment, Address address) {
        this.user = user;
        this.acceptedPayment = acceptedPayment;
        this.address = address;
    }

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

    @Override
    public String toString() {
        return "Contributor{" +
                "user=" + user +
                ", acceptedPayment='" + acceptedPayment + '\'' +
                ", address=" + address +
                '}';
    }
}
