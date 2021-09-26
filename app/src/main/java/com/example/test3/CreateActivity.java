package com.example.test3;
import static org.apache.poi.hssf.util.HSSFColor.*;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
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

import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;

import org.apache.poi.hssf.util.HSSFColor;
import org.json.JSONException;
import org.json.JSONObject;


public class CreateActivity extends AppCompatActivity  {
    private DatabaseHandler handler = new DatabaseHandler("http://83.254.68.246:3003/");

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //TODO: ändra formatteringen av dob till YY/MM/DD
    public static boolean isDateValid(String date) {
        Pattern pattern = Pattern.compile("\\p{Digit}\\p{Digit}/\\p{Digit}\\p{Digit}/\\p{Digit}\\p{Digit}");
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create);

        Button reg= findViewById(R.id.create);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText)findViewById(R.id.email)).getText().toString();
                String password = ((EditText)findViewById(R.id.pass)).getText().toString();
                String name = ((EditText)findViewById(R.id.name)).getText().toString();
                String nr = ((EditText)findViewById(R.id.nr)).getText().toString();
                String dob = ((EditText)findViewById(R.id.dob)).getText().toString();
                String city = ((EditText)findViewById(R.id.city)).getText().toString();
                String addr = ((EditText)findViewById(R.id.addr)).getText().toString();
                ColorStateList originalColor = ((TextView)findViewById(R.id.email_leg)).getTextColors();
                ((TextView)findViewById(R.id.email_leg)).setTextColor(originalColor);

                if ( username.isEmpty() || password.isEmpty() || name.isEmpty() || nr.isEmpty() ||
                        dob.isEmpty() || city.isEmpty() || addr.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "All fields required",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (!isEmailValid(username)) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Use a valid e-mail address",Toast.LENGTH_SHORT);
                    toast.show();
                }
                /*else if (!isDateValid(dob)) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Use a valid date of birth (YY/MM/DD)",Toast.LENGTH_SHORT);
                    toast.show();
                }*/else {//all fields match the correct formatting, register user
                    if(!handler.userExists(username)) {
                        User newUser = handler.newUser(username,
                                password,
                                name,
                                nr,
                                dob,
                                city,
                                addr,
                                "User");//default tills vi kommit fram till hur vi assignar doctor roll
                        Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(backIntent);
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Username already exists.",Toast.LENGTH_LONG);
                        toast.show();
                        ((TextView)findViewById(R.id.email_leg)).setTextColor(Color.RED);
                    }
                }
            }
        });
    }
}
