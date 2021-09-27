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

    public Booking(String username, int cliniqueID, Timestamp date){
        id = -1;
        this.username = username;
        this.cliniqueID = cliniqueID;
        this.date = date;
    }

    public Booking(int id, String username, int cliniqueID, Timestamp date){
        this.id = id;
        this.username = username;
        this.cliniqueID = cliniqueID;
        this.date = date;
    }

    @Override
    public String toString(){
        return "ID: " + id +
                "\nUsername: " + username +
                "\nCliniqueID: " + cliniqueID +
                "\nDate and time: " + date.toString();
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
}
