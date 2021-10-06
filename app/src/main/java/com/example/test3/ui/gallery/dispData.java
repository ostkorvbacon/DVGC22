package com.example.test3.ui.gallery;

public class dispData {
    private String group;
    private int cases;
    private int deaths;

    public dispData(String group, int cases, int deaths) {
        this.group = group;
        this.cases = cases;
        this.deaths = deaths;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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



