package com.example.dvgc22_ui;

public class CovidVaccineReportWorld {

    private String YearWeekISO;
    private int FirstDose;
    private String FirstDoseRefused;
    private int SecondDose;
    private int UnknownDose;
    private int NumberDosesReceived;
    private String Region;
    private String Population;
    private String ReportingCountry;
    private String TargetGroup;
    private String Vaccine;
    private int Denominator;

    @Override
    public String toString(){
        return "\"YearWeekISO\" : " + "\"" +  YearWeekISO + "\",\n" +
                "\"FirstDose\" : " + Integer.toString(FirstDose) + ",\n" +
                "\"FirstDoseRefused\" : " + "\"" +  FirstDoseRefused + "\",\n" +
                "\"SecondDose\" : " + Integer.toString(SecondDose) + ",\n" +
                "\"UnknownDose\" : " + Integer.toString(UnknownDose) + ",\n" +
                "\"NumberDosesReceived\" : " + Integer.toString(NumberDosesReceived) + ",\n" +
                "\"Region\" : " + "\"" +  Region + "\",\n" +
                "\"Population\" : " + "\"" +  Population + "\",\n" +
                "\"ReportingCountry\" : " + "\"" +  ReportingCountry + "\",\n" +
                "\"TargetGroup\" : " + "\"" +  TargetGroup + "\",\n" +
                "\"Vaccine\" : " + "\"" +  Vaccine + "\",\n" +
                "\"Denominator\" : " + Integer.toString(Denominator);
    }

    public String getYearWeekISO() {
        return YearWeekISO;
    }

    public void setYearWeekISO(String yearWeekISO) {
        YearWeekISO = yearWeekISO;
    }

    public int getFirstDose() {
        return FirstDose;
    }

    public void setFirstDose(int firstDose) {
        FirstDose = firstDose;
    }

    public String getFirstDoseRefused() {
        return FirstDoseRefused;
    }

    public void setFirstDoseRefused(String firstDoseRefused) {
        FirstDoseRefused = firstDoseRefused;
    }

    public int getSecondDose() {
        return SecondDose;
    }

    public void setSecondDose(int secondDose) {
        SecondDose = secondDose;
    }

    public int getUnknownDose() {
        return UnknownDose;
    }

    public void setUnknownDose(int unknownDose) {
        UnknownDose = unknownDose;
    }

    public int getNumberDosesReceived() {
        return NumberDosesReceived;
    }

    public void setNumberDosesReceived(int numberDosesReceived) {
        NumberDosesReceived = numberDosesReceived;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getPopulation() {
        return Population;
    }

    public void setPopulation(String population) {
        Population = population;
    }

    public String getReportingCountry() {
        return ReportingCountry;
    }

    public void setReportingCountry(String reportingCountry) {
        ReportingCountry = reportingCountry;
    }

    public String getTargetGroup() {
        return TargetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        TargetGroup = targetGroup;
    }

    public String getVaccine() {
        return Vaccine;
    }

    public void setVaccine(String vaccine) {
        Vaccine = vaccine;
    }

    public int getDenominator() {
        return Denominator;
    }

    public void setDenominator(int denominator) {
        Denominator = denominator;
    }
}
