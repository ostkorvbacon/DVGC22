package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.test3.DataExtraction.DataExtractor;
import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.login_button);
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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(view.VISIBLE);
                String loginId = ((EditText)findViewById(R.id.loginId)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();
                loginAttempt(loginId, password);
                loadingProgressBar.setVisibility(view.GONE);
            }
        });
    }

}

