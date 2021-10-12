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
    LinearLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        relativeLayout = findViewById(R.id.booking);




        List <String> CliniqueName= new ArrayList<String>();



        List <Clinique> Cliniques=handler.getCliniques();

        for (int i=0;i<Cliniques.size();i++) {
             CliniqueName.add(Cliniques.get(i).getName());

        }


        List<Booking> BookingsToday=handler.getBookings();
        List <Timestamp> usedtimes=new ArrayList<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        List <Timestamp> freetimes=new ArrayList<>();
        freetimes.add(timestamp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);

        for (int i=0;i<10;i++){

            cal.add(Calendar.DATE, 1);
            timestamp = new Timestamp(cal.getTime().getTime());
            freetimes.add(timestamp);
        }




        final Spinner  schedule =(Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<Timestamp> adp2 = new ArrayAdapter<Timestamp> (this,android.R.layout.simple_spinner_dropdown_item,freetimes);
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
                for (int i3=0;i3<BookingsToday.size();i3++){

                    if(BookingsToday.get(i3).getCliniqueID()==Cid){
                        usedtimes.add(BookingsToday.get(i3).getDate());

                    }
                }


                for (int i3=0;i3<usedtimes.size();i3++){
                    for (int j=0;j<freetimes.size();j++){

                        if(usedtimes.get(i)==freetimes.get(j)){
                            freetimes.remove(j);
                        }
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
             date= (Timestamp) adapterView.getItemAtPosition(i);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button button=findViewById(R.id.createbook1);
        System.out.println(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<Cliniques.size();i++) {
                    if (Cid==Cliniques.get(i).getId()) {

                        name=Cliniques.get(i).getName();
                    }

                }

                handler.newBooking(email,name,date);

                Intent goTodash = new Intent(getApplicationContext(), MainMenuActivity.class);
                goTodash.putExtra("loggedInUser", user);
                startActivity(goTodash);


            }
        });


    }



}