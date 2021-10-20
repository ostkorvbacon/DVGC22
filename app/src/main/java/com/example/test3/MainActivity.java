package com.example.test3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.test3.DataExtraction.CovidData;
import com.example.test3.DataExtraction.DataExtractor;
import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;
import com.example.test3.VaccinePassport.CameraActivity;


public class MainActivity extends AppCompatActivity {
    private DatabaseHandler handler = new DatabaseHandler("http://83.254.68.246:3003/");
    public static CovidData covidData = null;
    private final int PERMISSION_REQUEST_CAMERA = 1;

    //set to true for insta login
    public boolean instaLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        if(handler.testAPIFunctions()){
            Log.i("API test", "Success!");
        }

         */

        DataExtractor data = new DataExtractor();
        Thread downloadCovidDataThread = new Thread(data);
        downloadCovidDataThread.start();
        try {
            downloadCovidDataThread.join();
            covidData = data.getCovidData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /*
        DatabaseHandler handler = new DatabaseHandler("http://83.254.68.246:3003/");
        if(handler.testAPIFunctions()){
            Log.i("APITest", "Api test succeeded!");
        }
        else{
            Log.i("APITest", "Api test failed!");
        }*/

        Button loginButton = findViewById(R.id.login_button);
        Button createButton = findViewById(R.id.create_button);
        Button scanPassport = findViewById(R.id.QR_scanner_login);
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
                String username = ((EditText)findViewById(R.id.loginId)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();

                if(handler.login(username, password)){
                    User loggedInUser = handler.getUser(username);
                    Intent loginIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    loginIntent.putExtra("loggedInUser", loggedInUser);
                    startActivity(loginIntent);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Login failed.",Toast.LENGTH_LONG);
                    toast.show();
                }
                loadingProgressBar.setVisibility(view.GONE);
            }
        });


        //QR scanner button listener
        scanPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCamera();
            }
        });

        if(instaLogin) {
            Intent loginIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
            User admin = handler.getUser("admin@gmail.com");
            loginIntent.putExtra("loggedInUser", admin);
            startActivity(loginIntent);
            return;
        }
    }

    public void requestCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
            cameraIntent.putExtra("ParentActivity", "mainActivity");
            startActivity(cameraIntent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
                cameraIntent.putExtra("ParentActivity", "mainActivity");
                startActivity(cameraIntent);
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getString(R.string.Camera_permission_denied), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}

