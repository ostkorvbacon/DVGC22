package com.example.test3.DatabaseHandler;


public class AgeGroupSchedule {

    private String date;
    private int id;
    private int minAge;
    private int maxAge;

    public AgeGroupSchedule(int minAge, int maxAge, String date){
        id = -1;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.date = date;
    }

    public AgeGroupSchedule(int id, int minAge, int maxAge, String date){
        this.id = id;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.date = date;
    }

    @Override
    public String toString(){
        return "ID: " + id +
                "\nminAge: " + minAge +
                "\nmaxAge: " + maxAge +
                "\nDate: " + date;
    }

    public boolean isAgeBetween(int age){
        if(age >= minAge && age <= maxAge){
            return true;
        }
        return false;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }
}
