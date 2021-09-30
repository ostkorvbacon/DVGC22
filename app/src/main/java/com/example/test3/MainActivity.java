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

import com.example.test3.DataExtraction.CovidData;
import com.example.test3.DataExtraction.DataExtractor;
import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;


public class MainActivity extends AppCompatActivity {
    private DatabaseHandler handler = new DatabaseHandler("http://83.254.68.246:3003/");
    public static CovidData covidData = null;

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
                String username = "bert@hotmail.com";//((EditText)findViewById(R.id.loginId)).getText().toString();
                String password = "bert12345";//((EditText)findViewById(R.id.password)).getText().toString();
                
                if(handler.login(username, password)){
                    User loggedInUser = new User();
                    loggedInUser = handler.getUser(username);
                    Intent loginIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    loginIntent.putExtra("LoggedInUser", loggedInUser);
                    startActivity(loginIntent);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Login failed.",Toast.LENGTH_LONG);
                    toast.show();
                }
                loadingProgressBar.setVisibility(view.GONE);
            }
        });
    }
}

