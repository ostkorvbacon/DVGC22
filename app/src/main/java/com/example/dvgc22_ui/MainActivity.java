package com.example.dvgc22_ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

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