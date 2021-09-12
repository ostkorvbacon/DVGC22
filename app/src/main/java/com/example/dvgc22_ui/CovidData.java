package com.example.dvgc22_ui;

import java.util.ArrayList;
import java.util.List;

public class CovidData {
    private List<CovidVaccineReportWorld> worldVaccineData;
    private List<CovidCasesSwedenRegional> swedenRegionalCases;
    private List<CovidCasesSwedenAge> swedenAgeCases;

    public CovidData(){
        worldVaccineData = new ArrayList<CovidVaccineReportWorld>();
        swedenRegionalCases = new ArrayList<CovidCasesSwedenRegional>();
        swedenAgeCases = new ArrayList<CovidCasesSwedenAge>();
    }

    public List<CovidVaccineReportWorld> getWorldVaccineData() {
        return worldVaccineData;
    }

    public List<CovidCasesSwedenRegional> getSwedenRegionalCases() {
        return swedenRegionalCases;
    }

    public List<CovidCasesSwedenAge> getSwedenAgeCases() {
        return swedenAgeCases;
    }
}
