package com.example.test3.DatabaseHandler;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Booking {
    private String username;
    private int cliniqueID;
    private Timestamp date;
    private int id;
    private String type;

    public Booking(String username, int cliniqueID, Timestamp date, String type){
        id = -1;
        this.username = username;
        this.cliniqueID = cliniqueID;
        this.date = date;
        this.type = type;
    }

    public Booking(int id, String username, int cliniqueID, Timestamp date, String type){
        this.id = id;
        this.username = username;
        this.cliniqueID = cliniqueID;
        this.date = date;
        this.type = type;
    }

    @Override
    public String toString(){
        return "ID: " + id +
                "\nUsername: " + username +
                "\nCliniqueID: " + cliniqueID +
                "\nDate and time: " + date.toString() +
                "\nType: " + type;
    }

    public String getUsername() {
        return username;
    }

    public int getCliniqueID() {
        return cliniqueID;
    }

    public Timestamp getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public String getType(){return type;}
}
