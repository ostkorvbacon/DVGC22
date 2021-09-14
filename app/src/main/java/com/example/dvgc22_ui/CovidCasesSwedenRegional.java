package com.example.dvgc22_ui;

public class CovidCasesSwedenRegional {

    String region;
    int cases;
    double casesPer100000;
    int deaths;

    @Override
    public String toString(){
        return "\"regionName\" : " + "\"" + region + "\",\n" +
                "\"cases\" : " + Integer.toString(cases) + ",\n" +
                "\"casesPer100000\" : " + Double.toString(casesPer100000) + ",\n" +
                "\"deaths\" : " + Integer.toString(deaths);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public double getCasesPer100000() {
        return casesPer100000;
    }

    public void setCasesPer100000(double casesPer100000) {
        this.casesPer100000 = casesPer100000;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

}
