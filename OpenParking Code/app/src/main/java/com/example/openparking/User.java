package com.example.openparking;

public class User {
    String firstName;
    String lastName;
    String email;
    double userRating;
    int timesUserRated;

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", userRating='" + userRating + '\'' +
                ", timesUserRated='" + timesUserRated + '\'' +
                '}';
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
