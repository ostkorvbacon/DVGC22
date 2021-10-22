package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.test3.DatabaseHandler.Booking;
import com.example.test3.DatabaseHandler.Clinique;
import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;
import com.example.test3.ui.home.HomeFragment;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BookingsActivity extends AppCompatActivity {
    private DatabaseHandler handler = new DatabaseHandler("http://83.254.68.246:3003/");
    int i=0;
    private User user;
    int Cid=0;
    Timestamp date;
    String name;
    String email;
    String type;
    LinearLayout relativeLayout;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        relativeLayout = findViewById(R.id.booking);
        button=findViewById(R.id.createbook1);
        button.setClickable(false);

        List <String> CliniqueName= new ArrayList<String>();
        List <Clinique> Cliniques=handler.getCliniques();
        List <String> vaccin=new ArrayList<String>();
        vaccin.add("Pfizer");
        vaccin.add("Moderna");
        vaccin.add("Astra");

        for (int i=0;i<Cliniques.size();i++) {
             CliniqueName.add(Cliniques.get(i).getName());

        }

        List<Booking> BookingsToday=handler.getBookings();
        List <Timestamp> usedtimes=new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.set(2021, 10, 19); // set from date picker
        List <Timestamp> freetimes = handler.getFreeTimeSlots(cal);
        List <String> timeSlots = new ArrayList<String>();

        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        int i = 0;
        for(Timestamp ts : freetimes){
            timeSlots.add(sdfTime.format(ts));
            i++;
        }

        final Spinner  schedule =(Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adp2 = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,timeSlots);
        schedule.setAdapter(adp2);
        schedule.setVisibility(View.INVISIBLE);

        Intent getUser = this.getIntent();
        user = (User)getUser.getSerializableExtra("loggedInUser");
        email=user.getUsername();
        i=0;

        final Spinner booking =(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,CliniqueName);
        booking.setAdapter(adp);
        booking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    for (int i2=0;i2<Cliniques.size();i2++) {
                        if (adapterView.getItemAtPosition(i).toString()==Cliniques.get(i2).getName()){

                            Cid=Cliniques.get(i2).getId();
                            schedule.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ///Schedule spinner
        schedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                button.setClickable(true);
                String selectedTime = (String)adapterView.getItemAtPosition(i);
                for(Timestamp ts : freetimes){
                    if(ts.getHours() == Integer.parseInt(selectedTime.split(":")[0]) &&
                            ts.getMinutes() == Integer.parseInt(selectedTime.split(":")[1])){
                        date = ts;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                button.setClickable(false);
            }
        });


        System.out.println(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<Cliniques.size();i++) {
                    if (Cid==Cliniques.get(i).getId()) {

                        name=Cliniques.get(i).getName();
                    }
                }
                handler.newBooking(email,name,date,type);

                Intent goTodash = new Intent(getApplicationContext(), MainMenuActivity.class);
                goTodash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                goTodash.putExtra("loggedInUser", user);
                startActivity(goTodash);
            }
        });

        final Spinner vaccine =(Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> adp3 = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,vaccin);
        vaccine.setAdapter(adp3);
        vaccine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            type=vaccin.get(i);
            System.out.println(type);
            }
        });
    }
}