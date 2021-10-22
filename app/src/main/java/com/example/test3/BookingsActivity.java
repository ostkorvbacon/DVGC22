package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.test3.DatabaseHandler.Clinique;
import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;
import com.example.test3.DatabaseHandler.Vaccination;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
    TextView chosenDate;
    DatePickerDialog picker;
    int chosenDay;
    int chosenMonth;
    int chosenYear;
    Spinner  schedule;
    List <Timestamp> freeTimes;
    boolean isCliniqueSelected;
    boolean isDateSelected;
    boolean isTimeSelected;
    boolean isTypeSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        relativeLayout = findViewById(R.id.booking);
        button=findViewById(R.id.createbook1);
        chosenDate = findViewById(R.id.booking_date_text);
        chosenDate.setInputType(InputType.TYPE_NULL);
        schedule =(Spinner) findViewById(R.id.spinner2);

        isCliniqueSelected = false;
        isDateSelected = false;
        isTimeSelected = false;
        isTypeSelected = false;

        List <String> CliniqueName= new ArrayList<String>();
        List <Clinique> Cliniques=handler.getCliniques();
        List <String> vaccin=new ArrayList<String>();
        if(handler.getPfizerQuantity() > 0){
            vaccin.add("Pfizer");
        }
        if(handler.getModernaQuantity() > 0){
            vaccin.add("Moderna");
        }
        if(handler.getAstraQuantity() > 0){
            vaccin.add("Astra");
        }

        for (int i=0;i<Cliniques.size();i++) {
             CliniqueName.add(Cliniques.get(i).getName());
        }


        chosenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // date picker dialog
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(BookingsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                chosenDay = dayOfMonth;
                                chosenMonth = (monthOfYear + 1);
                                chosenYear = year;
                                chosenDate.setText(chosenYear + "/" + chosenMonth + "/" + chosenDay);
                            }


                        }, year, month, day);
                Calendar cal = Calendar.getInstance();
                for(Vaccination vac : handler.getUserVaccinations(user.getUsername())){
                    if (vac.getDose() == 1){
                        if(getMinDate(vac.getDate()).after(cal)) {
                            cal = getMinDate(vac.getDate());
                        }
                    }
                }
                picker.getDatePicker().setMinDate(cal.getTimeInMillis());
                picker.show();
            }
        });

        chosenDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isDateSelected = true;
                // update time slot list
                updateFreeTimes(chosenYear, chosenMonth, chosenDay);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                isCliniqueSelected = true;
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
                isCliniqueSelected = false;
            }
        });

        ///Schedule spinner
        schedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isTimeSelected = true;
                String selectedTime = (String)adapterView.getItemAtPosition(i);
                for(Timestamp ts : freeTimes){
                    if(ts.getHours() == Integer.parseInt(selectedTime.split(":")[0]) &&
                            ts.getMinutes() == Integer.parseInt(selectedTime.split(":")[1])){
                        date = ts;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isTimeSelected = false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCliniqueSelected && isDateSelected && isTimeSelected && isTypeSelected) {
                    for (int i = 0; i < Cliniques.size(); i++) {
                        if (Cid == Cliniques.get(i).getId()) {

                            name = Cliniques.get(i).getName();
                        }
                    }
                    handler.newBooking(email, name, date, type);

                    Intent goTodash = new Intent(getApplicationContext(), MainMenuActivity.class);
                    goTodash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    goTodash.putExtra("loggedInUser", user);
                    startActivity(goTodash);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),R.string.booking_not_all_items_selected,Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        final Spinner vaccine =(Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> adp3 = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,vaccin);
        vaccine.setAdapter(adp3);
        vaccine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isTypeSelected = false;
            }
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type=vaccin.get(i);
                isTypeSelected = true;
            }
        });
    }

    private void updateFreeTimes(int year, int month, int day){
        Log.i("Booking", "Updating time slot list...");

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        freeTimes = handler.getFreeTimeSlots(cal);
        List <String> timeSlots = new ArrayList<String>();

        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        int i = 0;
        for(Timestamp ts : freeTimes){
            timeSlots.add(sdfTime.format(ts));
            i++;
        }

        ArrayAdapter<String> adp2 = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,timeSlots);
        schedule.setAdapter(adp2);
        schedule.setVisibility(View.VISIBLE);
    }

    private Calendar getMinDate(String dateOfDose1){
        int noOfDays = 14; //i.e two weeks
        Calendar calendar = Calendar.getInstance();
        String[] sepDate = dateOfDose1.split("/");
        calendar.setTime(new Date(Integer.parseInt(sepDate[0])-1900, Integer.parseInt(sepDate[1])-1, Integer.parseInt(sepDate[2])));
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        return calendar;
    }
}