package com.example.dvgc22_ui;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


interface Callback{
    void callback(List<CovidDataReport> data);
}

// Class to Extract the information from the JSON file from ECDC and output it into objects
public class JSONExtractor implements Runnable {
    private List<CovidDataReport> data;
    private Callback c;

    @Override
    public void run(){
        String jsonString = "";
        HttpURLConnection connection;
        data = new ArrayList<CovidDataReport>();


        try {
            connection = (HttpURLConnection) new URL("https://opendata.ecdc.europa.eu/covid19/vaccine_tracker/json/").openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() != 200){
                Log.e("HTTP", "Request did not return ok: " + connection.getResponseCode());
                return;
            }
            jsonString = readFile(connection);

            connection.disconnect();

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
                this.data.add(parseObject(object));
                i++;
                if (i%100 == 0){
                    Log.d("Write", "Writing object " + Integer.toString(i) + " out of " + Integer.toString(len) + " to list.");
                }
            }

            Log.d("Write", "Finished writing to list.");
            Log.d("Read", "Reading first entry from dowmloaded data to see if correct: \n" + objects[len-1]);
            Log.d("Read", "Reading first entry from list to see if correct: \n" + data.get(len-1).toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Read","ERROR: Could not read file!");
        }


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

    private CovidDataReport parseObject(String obj){
        CovidDataReport report = new CovidDataReport();
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

    public List<CovidDataReport> getData(){
        return this.data;
    }
}
