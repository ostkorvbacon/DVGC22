package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< Updated upstream
import com.example.test3.DataExtraction.CovidData;
import com.example.test3.DataExtraction.DataExtractor;


public class MainActivity extends AppCompatActivity {
    private CovidData covidData;
    public boolean loginAttempt(String loginId, String password){

        return loginId.equals("login");
        //pseudokod
        //if loginId in user database
        //if user.password.matches(password)
        //return true
        //else
        //return false
=======
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    String res = null;


    public boolean loginAttempt(String loginId, String password){

        String loginEndpoint = "http://83.254.68.246:3003/tryLogin";

        JSONObject loginData = new JSONObject();
        try {
            loginData.put("Email", loginId);
            loginData.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest loginReq = new JsonObjectRequest(Request.Method.POST,
                loginEndpoint,
                loginData,
                response -> {
                    try {
                        res = response.getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(res.equals("Success!")){
                        Intent loginIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        loginIntent.putExtra("loginId", loginId);
                        startActivity(loginIntent);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                },
                volleyError -> Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show()
        );
        queue.add(loginReq);
        return false;
>>>>>>> Stashed changes
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataExtractor data = new DataExtractor();
        Thread downloadCovidDataThread = new Thread(data);
        downloadCovidDataThread.start();

        Button loginButton = findViewById(R.id.login_button);
<<<<<<< Updated upstream
        ProgressBar loadingProgressBar = findViewById(R.id.loading);

=======
        Button createButton = findViewById(R.id.create_button);
        ProgressBar loadingProgressBar = findViewById(R.id.loading);


        //register listener
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(getApplicationContext(), CreateActivity.class);
                startActivity(createIntent);
            }
        });


        //login listener
>>>>>>> Stashed changes
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(view.VISIBLE);
<<<<<<< Updated upstream

                String loginId = ((EditText)findViewById(R.id.loginId)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();

                boolean success = loginAttempt(loginId, password);

                if(success){
                    //check if user is an admin and direct accordingly to the respective pages
                    Intent loginIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    loginIntent.putExtra("loginId", loginId);
                    startActivity(loginIntent);
                }else{
                    loadingProgressBar.setVisibility(view.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(),"Wrong username or password",Toast.LENGTH_SHORT);
                    toast.show();
                }


=======
                String loginId = ((EditText)findViewById(R.id.loginId)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();
                loginAttempt(loginId, password);
                loadingProgressBar.setVisibility(view.GONE);
>>>>>>> Stashed changes
            }
        });
    }

}

