package com.example.test3.DatabaseHandler;

public class Vaccination {
    private String username;
    private int cliniqueID;
    private String date;
    private int dose;
    private String type;
    private int id;

    public Vaccination(String username, String date, int dose, String type, int cliniqueID){
        id = -1;
        this.username = username;
        this.cliniqueID = cliniqueID;
        this.date = date;
        this.dose = dose;
        this.type = type;
    }

    public Vaccination(int id, String username, String date, int dose, String type, int cliniqueID){
        this.id = id;
        this.username = username;
        this.cliniqueID = cliniqueID;
        this.date = date;
        this.dose = dose;
        this.type = type;
    }

    @Override
    public String toString(){
        return "ID: " + id +
                "\nUsername: " + username +
                "\nCliniqueID: " + cliniqueID +
                "\nDate: " + date +
                "\nDose: " + dose +
                "\nType: " + type;
    }

    public String getDate() {
        return date;
    }

    public int getDose() {
        return dose;
    }

    public String getType() {
        return type;
    }

    public int getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public int getCliniqueID(){
        return cliniqueID;
    }

}
