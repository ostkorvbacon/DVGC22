package com.example.test3.DataExtraction;

import android.util.Log;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;




/*
HOW TO USE:
    Global:
        private CovidData covidData;
    In MainActivity:
        DataExtractor data = new DataExtractor();
        Thread downloadCovidDataThread = new Thread(data);
        downloadCovidDataThread.start();
        try {
            downloadCovidDataThread.join();
            covidData = data.getCovidData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/


// Class to Extract the information from the JSON file from ECDC and output it into objects
public class DataExtractor implements Runnable {
    CovidData covidData;

    @Override
    public void run(){
        covidData = new CovidData();
        String jsonString = "";
        String worldVaccineURL = "https://opendata.ecdc.europa.eu/covid19/vaccine_tracker/json/";
        String swedenCasesURL = "http://83.254.68.246:3003/cases";
        String swedenVaccineURL = "http://83.254.68.246:3003/administered";
        String swedenVaccineDistributionURL = "http://83.254.68.246:3003/delivered";
        String worldCasesURL = "https://covid19.who.int/WHO-COVID-19-global-table-data.csv";
        HttpURLConnection connection;
        // (bad) world vaccine data = https://opendata.ecdc.europa.eu/covid19/vaccine_tracker/json/ (json)
        // world cases and deaths data = https://covid19.who.int/WHO-COVID-19-global-table-data.csv (csv)
        // sweden vaccine data = http://83.254.68.246:3003/administered (xlsx)
        // sweden cases and deaths data = http://83.254.68.246:3003/cases (xlsx)
        // sweden vaccine type per region = http://83.254.68.246:3003/delivered (xlsx)

        // how to read xlsx: https://www.baeldung.com/java-microsoft-excel
        /*
        TODO:
            - world cases and deaths data
                - en entry för varje land
            - Callback
        */

        /*
        // get the cases and deaths data for Sweden
        try{
            connection = getConnection(swedenCasesURL);
            if(connection == null){
                return;
            }
            InputStream is = connection.getInputStream();
            Workbook workbook = new XSSFWorkbook(is);
            // get cases and deaths regional sweden
            Sheet sheet = workbook.getSheetAt(3); // 3 for cases and deaths by county.

            for (Row row : sheet) {
                if(row.getRowNum() != 0) {
                    if (row.getCell(0) != null && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
                        String region = row.getCell(0).getRichStringCellValue().getString();
                        region = region.trim();
                        String group = row.getCell(1).getRichStringCellValue().getString();
                        int cases = (int) row.getCell(2).getNumericCellValue();
                        int deaths = (int) row.getCell(4).getNumericCellValue();
                        // find region in list, if there is one
                        int regionIndex = covidData.findSwedenCasesAndDeathsRegion(region);
                        if(regionIndex == -1){
                            CovidCasesSweden entry = new CovidCasesSweden();
                            entry.setRegion(region);
                            entry.addAgeGroupReport(group, cases, deaths);
                            covidData.getSwedenCasesAndDeaths().add(entry);
                        }
                        else{
                            covidData.getSwedenCasesAndDeaths().get(regionIndex).addAgeGroupReport(group, cases, deaths);
                        }
                        Log.i("Read", "Reading cases and deaths sheet 3...");
                        System.gc();
                        //Log.d("Read", "cell 0: " + Double.toString(row.getCell(1).getNumericCellValue()));
                    }

                }
            }
            Log.d("Write", "Finished writing to Sweden cases list.");
            Log.d("Read", "Reading first entry from dowmloaded data to see if correct: " + sheet.getRow(1).getCell(4).getNumericCellValue());
            Log.d("Read", "Reading first entry from list to see if correct: \n" +
                    covidData.getSwedenCasesAndDeaths().get(
                    covidData.findSwedenCasesAndDeathsRegion("Sverige")
                    ).getAgeGroupReport("Total").getDeaths()
            );
            is.close();
            connection.disconnect();

        } catch(IOException e){
            e.printStackTrace();

        }
        System.gc();
        */

        // get the vaccine data for sweden
        try {
            connection = getConnection(swedenVaccineURL);
            if(connection == null){
                return;
            }
            InputStream is = connection.getInputStream();
            Workbook workbook = new XSSFWorkbook(is);
            // get weekly administrated
            Sheet sheet = workbook.getSheetAt(1); // 1 for weekly reports

            for (Row row : sheet) {
                if(row.getRowNum() != 0) {
                    if (row.getCell(0) != null && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
                        String region = row.getCell(2).getRichStringCellValue().getString();
                        region = region.replace("|", "");
                        region = region.trim();
                        int year = Integer.parseInt(row.getCell(1).getRichStringCellValue().getString());
                        int week = Integer.parseInt(row.getCell(0).getRichStringCellValue().getString());

                        boolean foundRegion = false;
                        // find region in list, if there is one
                        for(CovidVaccineSweden reg : covidData.getSwedenVaccine()){
                            if (reg.getRegion().equals(region)){ // region exists in data
                                foundRegion = true;
                                int i = reg.weeklyReportsFindWeek(week, year);
                                if(i != -1){ // week exists in data
                                    reg.getWeeklyReports().get(i).setDose2((int) row.getCell(3).getNumericCellValue());
                                    reg.getWeeklyReports().get(i).setDose2Quota(row.getCell(4).getNumericCellValue());
                                }else{
                                    reg.addWeeklyReport(
                                            week,
                                            year,
                                            (int) row.getCell(3).getNumericCellValue(),
                                            row.getCell(3).getNumericCellValue()
                                    );
                                }
                            }
                        }
                        if(!foundRegion){
                            CovidVaccineSweden entry = new CovidVaccineSweden();
                            entry.setRegion(region);
                            entry.addWeeklyReport(
                                    week,
                                    year,
                                    (int) row.getCell(3).getNumericCellValue(),
                                    row.getCell(3).getNumericCellValue()
                            );
                            covidData.getSwedenVaccine().add(entry);
                        }
                        System.gc();
                        Log.i("Read", "Reading vaccine sheet 1...");
                    }

                }
            }

            // get vaccine age reports
            sheet = workbook.getSheetAt(2); // 2 for age reports
            for (Row row : sheet) {
                if(row.getRowNum() != 0) {
                    if (row.getCell(0) != null && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
                        String region = row.getCell(0).getRichStringCellValue().getString();
                        region = region.replace("|", "");
                        region = region.trim();
                        // find region in list
                        for(CovidVaccineSweden reg : covidData.getSwedenVaccine()){
                            if (reg.getRegion().equals(region)){ // region found
                                if(row.getCell(4).getRichStringCellValue().getString().equals("Minst 1 dos")){
                                    reg.addAgeGroupReport(
                                            row.getCell(1).getRichStringCellValue().getString(),
                                            (int) row.getCell(2).getNumericCellValue(),
                                            row.getCell(3).getNumericCellValue(),
                                            (int) row.getCell(5).getNumericCellValue(),
                                            (int) row.getCell(6).getNumericCellValue(),
                                            (int) row.getCell(7).getNumericCellValue()
                                    );
                                }else{
                                    for(CovidVaccineSweden.AgeGroupReport rep : reg.getAgeGroupReports()){
                                        if (rep.getGroup().equals(row.getCell(1).getRichStringCellValue().getString())){
                                            rep.setDose2((int) row.getCell(2).getNumericCellValue());
                                            rep.setDose2Quota(row.getCell(3).getNumericCellValue());
                                            rep.setDose2Pfizer((int) row.getCell(5).getNumericCellValue());
                                            rep.setDose2Moderna((int) row.getCell(6).getNumericCellValue());
                                            rep.setDose2AstraZeneca((int) row.getCell(7).getNumericCellValue());
                                        }
                                    }
                                }
                            }
                        }
                        System.gc();
                        Log.i("Read", "Reading vaccine sheet 2...");
                    }

                }
            }
            Log.d("Write", "Finished writing to Sweden vaccine list.");
            Log.d("Read", "Reading first entry from downloaded data to see if correct: " + sheet.getRow(1).getCell(2).getNumericCellValue());
            Log.d("Read", "Reading first entry from list to see if correct: \n" +
                    covidData.getSwedenVaccine().get(0).getAgeGroupReports().get(0).getDose1() + " " +
                    covidData.getSwedenVaccine().get(0).getAgeGroupReports().get(0).getDose1AstraZeneca()
                    );
            is.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.gc();
        // get the vaccine distribution for sweden
        try {
            connection = getConnection(swedenVaccineDistributionURL);
            if(connection == null){
                return;
            }
            InputStream is = connection.getInputStream();
            Workbook workbook = new XSSFWorkbook(is);
            // get cases and deaths regional sweden
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if(row.getRowNum() >= 4) {
                    if (row.getCell(0) != null && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
                        String region = row.getCell(0).getRichStringCellValue().getString();
                        if (region.contains("Sverige")){
                            region = "Sverige";
                        }
                        if (region.contains("Jämtland")){
                            region = "Jämtland";
                        }
                        region = region.trim();
                        if (!region.contains("Fohm")) {
                            int regIndex = covidData.findSwedenVaccineRegion(region);
                            CovidVaccineSweden data = covidData.getSwedenVaccine().get(regIndex);
                            for (Cell cell : row) {
                                if (cell.getColumnIndex() >= 1) {
                                    if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                                        int year = (int) sheet.getRow(1).getCell(cell.getColumnIndex()).getNumericCellValue();
                                        int week = (int) sheet.getRow(2).getCell(cell.getColumnIndex()).getNumericCellValue();
                                        if (!data.distributedWeeklyHasWeek(week, year)) {
                                            data.addDistributedWeek(week, year);
                                        }
                                        int weekIndex = data.distributedWeeklyFindWeek(week, year);
                                        String product = sheet.getRow(3).getCell(cell.getColumnIndex()).getRichStringCellValue().getString().trim();
                                        if (product.equals("Pfizer/BioNTech")) {
                                            data.getDistributedWeekly().get(weekIndex).setPfizer((int) cell.getNumericCellValue());
                                        } else if (product.equals("Moderna")) {
                                            data.getDistributedWeekly().get(weekIndex).setModerna((int) cell.getNumericCellValue());
                                        } else if (product.equals("AstraZeneca")) {
                                            data.getDistributedWeekly().get(weekIndex).setAstraZeneca((int) cell.getNumericCellValue());
                                        }
                                    }
                                }
                            }
                        }
                        System.gc();
                        Log.i("Read", "Reading vaccine distribution sheet 1...");
                    }

                }
            }
            Log.d("Write", "Finished writing to Sweden vaccine list.");
            Log.d("Read", "Reading first entry from downloaded data to see if correct: " + sheet.getRow(4).getCell(3).getNumericCellValue());
            Log.d("Read", "Reading first entry from list to see if correct: \n" + covidData.getSwedenVaccine().get(0).getDistributedWeekly().get(2).getPfizer());
            is.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }



        /*
        try {
            connection = getConnection(worldVaccineURL);
            jsonString = readFile(connection);
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(jsonString.isEmpty()){
            Log.d("Read", "jsonString is empty");
            return;
        }

        // Parse the string
        int i = 0;
        String[] objects = jsonString.split("\\[")[1].split("\\},");
        int len = objects.length;
        for(String object : objects){
            // Parse each record entry and add it to a list.
            this.worldVaccineData.add(parseObject(object));
            i++;
            if (i%100 == 0){
                Log.d("Write", "Writing object " + Integer.toString(i) + " out of " + Integer.toString(len) + " to list.");
            }
        }

        Log.d("Write", "Finished writing to list.");
        Log.d("Read", "Reading first entry from dowmloaded data to see if correct: \n" + objects[len-1]);
        Log.d("Read", "Reading first entry from list to see if correct: \n" + worldVaccineData.get(len-1).toString());



         */


    }

    private HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() != 200){
            Log.e("HTTP", "Request did not return ok: " + connection.getResponseCode());
            return null;
        }

        return connection;
    }
    private String readFile(HttpURLConnection connection) throws IOException {
        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;

        int i = 0;
        while((line = reader.readLine()) != null){
            sb.append(line);
            i++;
            if(i%1000==0){
                Log.d("Read", "Reading line " + Integer.toString(i) + " in JSON from URL...");
            }
        }
        is.close();
        reader.close();


        Log.d("Read", "Finnished reading JSON from URL...");
        return sb.toString();
    }

    private CovidVaccineReportWorld parseObject(String obj){
        CovidVaccineReportWorld report = new CovidVaccineReportWorld();
        String key;
        String value;
        String[] splitString;
        obj = obj.split("\\{")[1]; // remove the { so that only the items are left
        String[] items = obj.split(",");
        for (String item : items){
            item = item.trim();
            splitString = item.split(" : ");
            key = splitString[0].replace("\"", "");
            value = splitString[1].replace("\"", "");
            //Log.d("Write", "item: " + item + " key: " + key + " value: " + value);
            if(key.equals("YearWeekISO")){
                report.setYearWeekISO(value);
            }else if (key.equals("FirstDose")){
                report.setFirstDose(StringToInt(value));
            }else if (key.equals("FirstDoseRefused")){
                report.setFirstDoseRefused(value);
            }else if (key.equals("SecondDose")){
                report.setSecondDose(StringToInt(value));
            }else if (key.equals("UnknownDose")){
                report.setUnknownDose(StringToInt(value));
            }else if (key.equals("NumberDosesReceived")){
                report.setNumberDosesReceived(StringToInt(value));
            }else if (key.equals("Region")){
                report.setRegion(value);
            }else if (key.equals("Population")){
                report.setPopulation(value);
            }else if (key.equals("ReportingCountry")){
                report.setReportingCountry(value);
            }else if (key.equals("TargetGroup")){
                report.setTargetGroup(value);
            }else if (key.equals("Vaccine")){
                report.setVaccine(value);
            }else if (key.equals("Denominator")){
                if(value.contains("}")) {
                    value = value.replace("}", "");
                    value = value.replace("]", "");
                    value = value.trim();
                }
                report.setDenominator(StringToInt(value));
            }
        }
        return report;
    }

    private int StringToInt(String str){
        if(str.equals("")){
            return 0;
        }
        else return Integer.parseInt(str);
    }


    public CovidData getCovidData() {
        return covidData;
    }


}
