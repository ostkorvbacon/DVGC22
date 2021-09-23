package com.example.test3.APIHandlers;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.test3.MainActivity;
import com.example.test3.R;
import com.google.logging.type.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHandler extends AppCompatActivity {

    public Endpoints endpoints;
    final String baseAdress = "http://83.254.68.246:3003/";
    public String responseResult = null;


    /*newUser endpoint
        pre:userData is to be filled with the following
            "Email", "Password", "Name", "Phone", "Dob", "City", "Addr", "Role"
        post:
    */
    public void storeResult(JSONObject response) {
        try {
            responseResult = response.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
            responseResult = "err: couldn't getString()";
        }
    }
    public void registerUser(JSONObject userData, RequestQueue queue){
        String targetEndpoint = "http://83.254.68.246:3003/tryLogin";
        Object lock = new Object();
        //String theresult = null;
        //TextView txt0 = findViewById(R.id.test);
        //TextView txt1 = findViewById(R.id.messagetxt);
        //txt1.setText(targetEndpoint);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                targetEndpoint,
                userData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        storeResult(response);
                        //result = response.getString("status");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG).show();
                        //result = error.toString();
                    }
        });
        queue.add(req);
        }
    }
/*
    //tryLogin
    public boolean login(String email, String password){

        return false;
    }
    //getUser
    public String getUser(String email){

    }
    //getUserList
    public JSONArray getUserList(){

    }
*/

