package com.example.dvgc22_ui;

public class CovidCasesSwedenAge {

    private String ageGroup;
    private int cases;
    private int deaths;

    @Override
    public String toString(){
        return "\"ageGroup\" : " + "\"" +  ageGroup + "\",\n" +
                "\"cases\" : " + Integer.toString(cases) + ",\n" +
                "\"deaths\" : " + Integer.toString(deaths);
    }
    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

}
