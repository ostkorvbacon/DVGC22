package com.example.dvgc22_ui;

import java.util.ArrayList;
import java.util.List;

public class CovidData {
    private List<CovidVaccineReportWorld> worldVaccineData;
    private List<CovidCasesSweden> swedenCasesAndDeaths;
    private List<CovidVaccineSweden> swedenVaccine;

    public CovidData(){
        worldVaccineData = new ArrayList<CovidVaccineReportWorld>();
        swedenCasesAndDeaths = new ArrayList<CovidCasesSweden>();
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

    public int findSwedenCasesAndDeathsRegion(String region){
        int i = 0;
        for(CovidCasesSweden reg : swedenCasesAndDeaths){
            if (reg.getRegion().equals(region)){
                return i;
            }
            i++;
        }
        return -1;
    }

    public List<CovidCasesSweden> getSwedenCasesAndDeaths() {
        return swedenCasesAndDeaths;
    }

    public List<CovidVaccineReportWorld> getWorldVaccineData() {
        return worldVaccineData;
    }

    public List<CovidVaccineSweden> getSwedenVaccine() {
        return swedenVaccine;
    }
}
