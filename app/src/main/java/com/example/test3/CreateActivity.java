package com.example.test3;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.ArrayList;
import java.util.regex.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class CreateActivity extends AppCompatActivity  {

    ArrayList<String> emaillist = new ArrayList<String>();
    ArrayList<String> passlist = new ArrayList<String>();

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static boolean isDateValid(String date) {

        Pattern pattern = Pattern.compile("\\p{Digit}\\p{Digit}/\\p{Digit}\\p{Digit}/\\p{Digit}\\p{Digit}");
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create);


        emaillist=getIntent().getStringArrayListExtra("oldmaillist");
        passlist=getIntent().getStringArrayListExtra("oldpasslist");

        Button back= findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(backIntent);
            }
        });
        Button reg= findViewById(R.id.create);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name =findViewById(R.id.name);
                EditText email =findViewById(R.id.email);
                EditText pass =findViewById(R.id.pass);
                EditText nr =findViewById(R.id.nr);
                EditText dob =findViewById(R.id.dob);
                EditText addr =findViewById(R.id.addr);

                if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || pass.getText().toString().isEmpty() || nr.getText().toString().isEmpty() || dob.getText().toString().isEmpty() || addr.getText().toString().isEmpty() ) {

                    Toast toast = Toast.makeText(getApplicationContext(),"All fields required",Toast.LENGTH_SHORT);
                    toast.show();
                }

                else if (!isEmailValid(email.getText().toString())) {

                    Toast toast = Toast.makeText(getApplicationContext(),"Use a valid e-mail address",Toast.LENGTH_SHORT);
                    toast.show();

                }
                else if (!isDateValid(dob.getText().toString())) {

                    Toast toast = Toast.makeText(getApplicationContext(),"Use a valid date of birth (DD/MM/YY)",Toast.LENGTH_SHORT);
                    toast.show();

                }

                else {

                    Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                    emaillist.add(email.getText().toString());
                    passlist.add(pass.getText().toString());
                    
                    backIntent.putExtra("newemaillist",emaillist);
                    backIntent.putExtra("newpasslist",passlist);
                    startActivity(backIntent);
                }


            }
        });




    }
}