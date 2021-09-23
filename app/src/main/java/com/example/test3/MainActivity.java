package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataExtractor data = new DataExtractor();
        Thread downloadCovidDataThread = new Thread(data);
        downloadCovidDataThread.start();

        Button loginButton = findViewById(R.id.login_button);
        ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(view.VISIBLE);

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


            }
        });
    }

}

