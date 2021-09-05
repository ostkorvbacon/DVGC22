package com.example.dvgc22_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<CovidDataReport> covidData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("test", "Programmet startar.");
        JSONExtractor json = new JSONExtractor();

        Thread downloadCovidDataThread = new Thread(json);
        downloadCovidDataThread.start();

        try {
            downloadCovidDataThread.join();
            covidData = json.getData();
            Log.i("test", "Från MainActivity: " + covidData.size()); // prints number of reports
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("test", "Från MainActivity: Slut");



    }
}