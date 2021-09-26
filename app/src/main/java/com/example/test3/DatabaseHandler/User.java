package com.example.test3.DatabaseHandler;

import java.io.Serializable;

public class User implements Serializable {
    String username;
    String name;
    String phoneNr;
    String dateOfBirth;
    String city;
    String address;
    String role;

    public User(){

    }

    public User(String username){
        this.username = username;
    }

    public User(String username, String name, String phoneNr, String dateOfBirth, String city, String address, String role){
        this.username = username;
        this.name = name;
        this.phoneNr = phoneNr;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.address = address;
        this.role = role;
    }

    @Override
    public String toString(){
        return "Username: " + username +
                "\nName: " + name +
                "\nPhone number: " + phoneNr +
                "\nDate of birth: " + dateOfBirth +
                "\nCity: " + city +
                "\nAddress: " + address +
                "\nRole: " + role;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getRole() {
        return role;
    }
}
