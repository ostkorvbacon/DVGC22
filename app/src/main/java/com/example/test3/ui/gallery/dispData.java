package com.example.test3.ui.gallery;

public class dispData {
    private String group;
    private String cases;
    private String deaths;

    public dispData(String group, String cases, String deaths) {
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

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }
}


