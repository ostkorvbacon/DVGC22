package com.example.test3.DataExtraction;

import java.util.ArrayList;
import java.util.List;

public class CovidVaccineSweden {

    String region;
    private List<WeeklyReport> weeklyReports = new ArrayList<WeeklyReport>();
    private List<AgeGroupReport> ageGroupReports = new ArrayList<AgeGroupReport>();
    private List<VaccineDistributedWeekly> distributedWeekly = new ArrayList<VaccineDistributedWeekly>();

    public void addWeeklyReport(int w, int y, int d1, double d1Quota){
        weeklyReports.add(new WeeklyReport(w, y, d1, d1Quota));
    }

    public void addAgeGroupReport(String group, int d1, double d1Quota, int dose1Pfizer, int dose1Moderna, int dose1AstraZeneca){
        ageGroupReports.add(new AgeGroupReport(group, d1, d1Quota, dose1Pfizer, dose1Moderna, dose1AstraZeneca));
    }

    public void addDistributedWeek(int week, int year){
        distributedWeekly.add(new VaccineDistributedWeekly(week, year));
    }

    public boolean weeklyReportsHasWeek(int week, int year){
        for(WeeklyReport w : weeklyReports){
            if(w.getYear() == year && w.getWeek() == week){
                return true;
            }
        }
        return false;
    }

    public int weeklyReportsFindWeek(int week, int year){
        int i = 0;
        for(WeeklyReport w : weeklyReports){
            if(w.getYear() == year && w.getWeek() == week){
                return i;
            }
            i++;
        }
        return -1;
    }

    public boolean distributedWeeklyHasWeek(int week, int year){
        for(VaccineDistributedWeekly w : distributedWeekly){
            if(w.getYear() == year && w.getWeek() == week){
                return true;
            }
        }
        return false;
    }

    public int distributedWeeklyFindWeek(int week, int year){
        int i = 0;
        for(VaccineDistributedWeekly w : distributedWeekly){
            if(w.getYear() == year && w.getWeek() == week){
                return i;
            }
            i++;
        }
        return -1;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<WeeklyReport> getWeeklyReports() {
        return weeklyReports;
    }

    public List<AgeGroupReport> getAgeGroupReports() {
        return ageGroupReports;
    }

    public List<VaccineDistributedWeekly> getDistributedWeekly() {
        return distributedWeekly;
    }

    protected class WeeklyReport{
        int week;
        int year;
        int dose1;
        double dose1Quota;
        int dose2;
        double dose2Quota;

        public WeeklyReport(int w, int y, int d1, double d1Quota){
            week = w;
            year = y;
            dose1 = d1;
            dose1Quota = d1Quota;
        }

        public int getWeek() {
            return week;
        }

        public int getYear() {
            return year;
        }

        public int getDose1() {
            return dose1;
        }

        public double getDose1Quota() {
            return dose1Quota;
        }

        public int getDose2() {
            return dose2;
        }

        public double getDose2Quota() {
            return dose2Quota;
        }

        public void setDose2(int dose2) {
            this.dose2 = dose2;
        }

        public void setDose2Quota(double dose2Quota) {
            this.dose2Quota = dose2Quota;
        }
    }

    public class AgeGroupReport {
        String group;
        int dose1;
        double dose1Quota;
        int dose2;
        double dose2Quota;
        int dose1Pfizer;
        int dose1Moderna;
        int dose1AstraZeneca;
        int dose2Pfizer;
        int dose2Moderna;
        int dose2AstraZeneca;


        public AgeGroupReport(String g, int d1, double d1Quota, int dose1Pfizer, int dose1Moderna, int dose1AstraZeneca){
            group = g;
            dose1 = d1;
            dose1Quota = d1Quota;
            this.dose1Pfizer = dose1Pfizer;
            this.dose1Moderna = dose1Moderna;
            this.dose1AstraZeneca = dose1AstraZeneca;
        }

        public String getGroup() {
            return group;
        }

        public int getDose1() {
            return dose1;
        }

        public double getDose1Quota() {
            return dose1Quota;
        }

        public int getDose2() {
            return dose2;
        }

        public double getDose2Quota() {
            return dose2Quota;
        }

        public int getDose1Pfizer() {
            return dose1Pfizer;
        }

        public int getDose1Moderna() {
            return dose1Moderna;
        }

        public int getDose1AstraZeneca() {
            return dose1AstraZeneca;
        }

        public int getDose2Pfizer() {
            return dose2Pfizer;
        }

        public int getDose2Moderna() {
            return dose2Moderna;
        }

        public int getDose2AstraZeneca() {
            return dose2AstraZeneca;
        }

        public void setDose2(int dose2) {
            this.dose2 = dose2;
        }

        public void setDose2Quota(double dose2Quota) {
            this.dose2Quota = dose2Quota;
        }

        public void setDose2Pfizer(int dose2Pfizer) {
            this.dose2Pfizer = dose2Pfizer;
        }

        public void setDose2Moderna(int dose2Moderna) {
            this.dose2Moderna = dose2Moderna;
        }

        public void setDose2AstraZeneca(int dose2AstraZeneca) {
            this.dose2AstraZeneca = dose2AstraZeneca;
        }


    }

    public class VaccineDistributedWeekly{
        int pfizer;
        int moderna;
        int astraZeneca;
        int week;
        int year;

        public VaccineDistributedWeekly(int week, int year){
            this.week = week;
            this.year = year;
        }

        public int getPfizer() {
            return pfizer;
        }

        public void setPfizer(int pfizer) {
            this.pfizer = pfizer;
        }

        public int getModerna() {
            return moderna;
        }

        public void setModerna(int moderna) {
            this.moderna = moderna;
        }

        public int getAstraZeneca() {
            return astraZeneca;
        }

        public void setAstraZeneca(int astraZeneca) {
            this.astraZeneca = astraZeneca;
        }

        public int getWeek() {
            return week;
        }

        public int getYear() {
            return year;
        }
    }
}
