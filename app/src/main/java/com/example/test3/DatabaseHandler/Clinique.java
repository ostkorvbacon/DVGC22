package com.example.test3.DatabaseHandler;

public class Clinique {
    private int id;
    private String name;
    private String phoneNr;
    private String city;
    private String address;

    public Clinique(){
        id = -1;
    }

    public Clinique(String name, String phoneNr, String city, String address){
        id = -1;
        this.name = name;
        this.phoneNr = phoneNr;
        this.city = city;
        this.address = address;
    }

    public Clinique(int id, String name, String phoneNr, String city, String address){
        this.id = id;
        this.name = name;
        this.phoneNr = phoneNr;
        this.city = city;
        this.address = address;
    }

    @Override
    public String toString(){
        return "Name: " + name +
                "\nPhone number: " + phoneNr +
                "\nCity: " + city +
                "\nAddress: " + address;
    }

    public String toString2(){
        return  name +
                "\nPhone number: " + phoneNr +
                "\nCity: " + city +
                "\nAddress: " + address;
    }


    public String getName() {
        return name;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }
}
