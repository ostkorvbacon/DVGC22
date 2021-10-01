package com.example.test3.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.test3.DatabaseHandler.Booking;
import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;


import com.example.test3.DatabaseHandler.Vaccination;
import com.example.test3.R;
import com.example.test3.VaccinePassport.VaccinePassport;
import com.example.test3.databinding.FragmentHomeBinding;

import java.sql.Timestamp;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private DatabaseHandler database = new DatabaseHandler("http://83.254.68.246:3003/");
    ImageView qrCode;
    TextView txtView;
    CheckBox firstDose;
    CheckBox secondDose;
    EditText firstDoseDate;
    EditText secondDoseDate;
    EditText personalName;
    TextView dateLabel;
    TextView timeLabel;
    TextView bookTitle;
    EditText appointDate;
    EditText appointTime;
    ImageButton cancelButton;
    Button bookButton;
    //temp
    int count;
    public void setViewsId(View root){
        //bookings related
        personalName = root.findViewById(R.id.editTextTextPersonName8);
        dateLabel = root.findViewById(R.id.appointment_date_label);
        timeLabel = root.findViewById(R.id.appointment_time_label);
        bookTitle = root.findViewById(R.id.appointment_title);
        appointDate = root.findViewById(R.id.appointment_date);
        appointTime = root.findViewById(R.id.appointment_time);
        cancelButton = root.findViewById(R.id.cancel_button);
        bookButton = root.findViewById(R.id.book_button);
        //QR related
        qrCode = root.findViewById(R.id.imageView);
        txtView = root.findViewById(R.id.textView15);
        //vaccinations
        firstDose = root.findViewById(R.id.checkBox);
        secondDose = root.findViewById(R.id.checkBox2);
        firstDoseDate = root.findViewById(R.id.first_dose_date);
        secondDoseDate = root.findViewById(R.id.second_dose_date);
    }

    public void toggleVisibilityBooking(User loggedInUser){
        if(database.bookingExists(database.getBookingID(loggedInUser.getUsername()))){
            dateLabel.setVisibility(View.VISIBLE);
            timeLabel.setVisibility(View.VISIBLE);
            appointDate.setVisibility(View.VISIBLE);
            appointTime.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            bookButton.setVisibility(View.GONE);
            String booking = database.getBooking(loggedInUser.getUsername()).getDate().toString();
            String[] bookingDetails = booking.split("\\s");
            String[] strippedTime = bookingDetails[1].split(":00.0");
            String bookingDate = bookingDetails[0];
            appointDate.setText(bookingDate);
            appointTime.setText(strippedTime[0]);
        }else{
            dateLabel.setVisibility(View.GONE);
            timeLabel.setVisibility(View.GONE);
            appointDate.setVisibility(View.GONE);
            appointTime.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setViewsId(root);
        firstDose.setEnabled(false);
        secondDose.setEnabled(false);
        Intent intent = this.getActivity().getIntent();
        User loggedInUser = (User)intent.getSerializableExtra("loggedInUser");

        personalName.setText(loggedInUser.getName());
        database.newBooking(loggedInUser.getUsername(), "test", Timestamp.valueOf("2021-9-15 10:30:00.0"));

        //newVaccination(username, date, dose, type, getClinique(b.getCliniqueID()).getName())
        //database.newVaccination(loggedInUser.getUsername(), "2021/9/15", 2, "Pfizer", "test");

        for(Vaccination v: database.getUserVaccinations(loggedInUser.getUsername())){
            int dose = v.getDose();

            if(dose == 1){
                firstDose.setChecked(true);
                firstDoseDate.setText(v.getDate());
                //database.deleteVaccination(v.getId());
            }else if(dose == 2){
                secondDose.setChecked(true);
                secondDoseDate.setText(v.getDate());
                //database.deleteVaccination(v.getId());

                bookTitle.setVisibility(View.INVISIBLE);
                bookButton.setVisibility(View.GONE);
                txtView.setVisibility(View.VISIBLE);
                qrCode.setVisibility(View.VISIBLE);
            }
        }
        //checks if user has upcoming vaccination appointment and then displays it in UI
        toggleVisibilityBooking(loggedInUser);

        //generates QR-code if user is "fully" (2 doses as of now) vaccinated
        VaccinePassport passport = new VaccinePassport(loggedInUser.getUsername(), qrCode, txtView);
        passport.getQRCode();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = loggedInUser.getUsername();
                Booking cancelBooking = database.getBooking(username);
                database.deleteBookings(username);
                toggleVisibilityBooking(loggedInUser);
            }
        });
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent bookingsPage = new Intent(root.getContext(), );
                //bookingsPage.putExtra("loggedIn", loggedInUser);
                //startActivity(bookingsPage);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}