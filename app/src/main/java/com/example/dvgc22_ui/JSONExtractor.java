package com.example.dvgc22_ui;

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
import java.util.ArrayList;
import java.util.List;

/*
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;




 */


// Class to Extract the information from the JSON file from ECDC and output it into objects
public class JSONExtractor implements Runnable {
    private List<CovidVaccineReportWorld> worldVaccineData;
    private List<CovidCasesSwedenRegional> swedenRegionalCases;

    @Override
    public void run(){
        String jsonString = "";
        String worldVaccineURL = "https://opendata.ecdc.europa.eu/covid19/vaccine_tracker/json/";
        String swedenCasesURL = "https://www.arcgis.com/sharing/rest/content/items/b5e7488e117749c19881cce45db13f7e/data";
        HttpURLConnection connection;
        worldVaccineData = new ArrayList<CovidVaccineReportWorld>();
        swedenRegionalCases = new ArrayList<CovidCasesSwedenRegional>();
        // (bad) world vaccine data = https://opendata.ecdc.europa.eu/covid19/vaccine_tracker/json/ (json)
        // world cases and deaths data = https://covid19.who.int/WHO-COVID-19-global-table-data.csv (csv)
        // sweden vaccine data = https://fohm.maps.arcgis.com/sharing/rest/content/items/fc749115877443d29c2a49ea9eca77e9/data (xlsx)
        // sweden cases and deaths data = https://www.arcgis.com/sharing/rest/content/items/b5e7488e117749c19881cce45db13f7e/data (xlsx)
        // sweden vaccine type per region = https://www.folkhalsomyndigheten.se/contentassets/ad481fe4487f4e6a8d1bcd95a370bc1a/v35-leveranser-av-covid-vaccin-till-och-med-vecka-37.xlsx

        // how to read xlsx: https://www.baeldung.com/java-microsoft-excel

        try{
            connection = getConnection(swedenCasesURL);
            InputStream is = connection.getInputStream();
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(3); // 3 for cases and deaths by county.
            for (Row row : sheet) {
                if(row.getRowNum() != 0) {
                    if (row.getCell(0) != null && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
                        CovidCasesSwedenRegional data = new CovidCasesSwedenRegional();
                        data.setRegionName(row.getCell(0).getRichStringCellValue().getString());
                        data.setCases((int) row.getCell(1).getNumericCellValue());
                        data.setCasesPer100000(row.getCell(2).getNumericCellValue());
                        data.setDeaths((int) row.getCell(4).getNumericCellValue());
                        swedenRegionalCases.add(data);
                        //Log.d("Read", "cell 0: " + Double.toString(row.getCell(1).getNumericCellValue()));
                    }

                }
            }

            Log.d("Write", "Finished writing to list.");
            Log.d("Read", "Reading first entry from dowmloaded data to see if correct: " +
                    sheet.getRow(1).getCell(0).getStringCellValue());
            Log.d("Read", "Reading first entry from list to see if correct: \n" + swedenRegionalCases.get(0).toString());

            connection.disconnect();
        } catch(IOException e){
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

    public List<CovidVaccineReportWorld> getWorldVaccine(){
        return this.worldVaccineData;
    }

    public List<CovidCasesSwedenRegional> getSwedenRegionalCases(){
        return this.swedenRegionalCases;
    }
}
