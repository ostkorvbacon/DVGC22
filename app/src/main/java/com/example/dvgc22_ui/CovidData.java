package com.example.dvgc22_ui;

import java.util.ArrayList;
import java.util.List;

public class CovidData {
    private List<CovidVaccineReportWorld> worldVaccineData;
    private List<CovidCasesSwedenRegional> swedenRegionalCases;
    private List<CovidCasesSwedenAge> swedenAgeCases;

    private List<CovidVaccineSweden> swedenVaccine;

    public CovidData(){
        worldVaccineData = new ArrayList<CovidVaccineReportWorld>();
        swedenRegionalCases = new ArrayList<CovidCasesSwedenRegional>();
        swedenAgeCases = new ArrayList<CovidCasesSwedenAge>();
        swedenVaccine = new ArrayList<CovidVaccineSweden>();
    }

    public int findSwedenVaccineRegion(String region){
        int i = 0;
        for(CovidVaccineSweden reg : swedenVaccine){
            if (reg.getRegion().equals(region)){
                return i;
            }
            i++;
        }
        return -1;
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

    public List<CovidVaccineSweden> getSwedenVaccine() {
        return swedenVaccine;
    }
}
