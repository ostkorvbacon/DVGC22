package com.example.test3.DataExtraction;

import java.util.ArrayList;
import java.util.List;

public class CovidCasesSweden {

    String region;
    private List<CovidCasesSweden.AgeGroupReport> ageGroupReports = new ArrayList<CovidCasesSweden.AgeGroupReport>();

    public AgeGroupReport getAgeGroupReport(String ageGroup){
        for(AgeGroupReport rep : ageGroupReports){
            if(rep.getAgeGroup().equals(ageGroup)){
                return rep;
            }
        }
        return null;
    }

    public void addAgeGroupReport(String group, int cases, int deaths){
        ageGroupReports.add(new CovidCasesSweden.AgeGroupReport(group, cases, deaths));
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<CovidCasesSweden.AgeGroupReport> getAgeGroupReports() {
        return ageGroupReports;
    }

    public class AgeGroupReport {
        private String ageGroup;
        private int cases;
        private int deaths;

        public AgeGroupReport(String g, int c, int d){
            ageGroup = g;
            cases = c;
            deaths = d;
        }

        public String getAgeGroup() {
            return ageGroup;
        }

        public int getCases() {
            return cases;
        }

        public int getDeaths() {
            return deaths;
        }
    }
}
