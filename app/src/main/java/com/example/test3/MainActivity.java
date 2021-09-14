package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> email = new ArrayList<String>();
    ArrayList<String> pass = new ArrayList<String>();
    public boolean loginAttempt(String loginId, String password, ArrayList<String> email, ArrayList<String> pass){


        for (int i=0;i<email.size();i++){

            if (loginId.equals(email.get(i))) {
                if (password.equals(pass.get(i))) {
                    return true;
                }

                else {
                    return false;
                }


            }

        }


        return false;

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

        Button loginButton = findViewById(R.id.login_button);
        ProgressBar loadingProgressBar = findViewById(R.id.loading);
        Button createButton = findViewById(R.id.create_button);


        Intent temp=getIntent();
        if (temp.hasExtra("newemaillist")) {

            email=getIntent().getStringArrayListExtra("newemaillist");
            pass=getIntent().getStringArrayListExtra("newpasslist");




        }
        else {
            email.add("login");
            pass.add("");
        }

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(getApplicationContext(), CreateActivity.class);
                createIntent.putExtra("oldmaillist",email);
                createIntent.putExtra("oldpasslist",pass);
                startActivity(createIntent);
            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(view.VISIBLE);

                String loginId = ((EditText)findViewById(R.id.loginId)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();

                boolean success = loginAttempt(loginId, password, email,pass);

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

