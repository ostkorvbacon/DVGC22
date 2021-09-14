package com.example.dvgc22_ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private  CovidData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("test", "Programmet startar.");
        DataExtractor json = new DataExtractor();


        Thread downloadCovidDataThread = new Thread(json);
        downloadCovidDataThread.start();

        /*
        try {
            downloadCovidDataThread.join();
            data = json.getCovidData();
            Log.i("test", "Från MainActivity: " + data.getSwedenRegionalCases().size()); // prints number of reports
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        Log.i("test", "Från MainActivity: Slut");



    }
}