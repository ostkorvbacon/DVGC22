package com.example.test3.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.test3.DatabaseHandler.Booking;
import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.Questionnaire;
import com.example.test3.DatabaseHandler.User;


import com.example.test3.DatabaseHandler.Vaccination;
import com.example.test3.DialogActivity;
import com.example.test3.QuestionnaireActivity;
import com.example.test3.R;
import com.example.test3.VaccinePassport.CameraActivity;
import com.example.test3.VaccinePassport.VaccinePassport;
import com.example.test3.databinding.FragmentHomeBinding;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private final int PERMISSION_REQUEST_CAMERA = 1;
    private ActivityResultLauncher<String> mPermissionResult;
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
    TextView cliniqueLabel;
    EditText appointDate;
    EditText appointTime;
    EditText appointClinique;
    ImageButton cancelButton;
    Button bookButton;
    Button qrScannerButton;
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
        cliniqueLabel = root.findViewById(R.id.appointment_clinique_label);
        appointClinique = root.findViewById(R.id.appointment_clinique);
        //QR related
        qrCode = root.findViewById(R.id.imageView);
        txtView = root.findViewById(R.id.textView15);
        //vaccinations
        firstDose = root.findViewById(R.id.checkBox);
        secondDose = root.findViewById(R.id.checkBox2);
        firstDoseDate = root.findViewById(R.id.first_dose_date);
        secondDoseDate = root.findViewById(R.id.second_dose_date);
        qrScannerButton = root.findViewById(R.id.button_qr_scanner_home);
    }

    public void toggleVisibilityBooking(User loggedInUser){
        if(!database.isUserEligibleForBookingVaccination(loggedInUser.getUsername())){
            bookButton.setVisibility(View.GONE);
        }
        if(database.bookingExists(database.getBookingID(loggedInUser.getUsername()))){
            dateLabel.setVisibility(View.VISIBLE);
            timeLabel.setVisibility(View.VISIBLE);
            appointDate.setVisibility(View.VISIBLE);
            appointTime.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            appointClinique.setVisibility(View.VISIBLE);
            cliniqueLabel.setVisibility(View.VISIBLE);
            bookButton.setVisibility(View.GONE);
            String booking = database.getBooking(loggedInUser.getUsername()).getDate().toString();
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
            String[] bookingDetails = booking.split("\\s");
            String strippedTime = sdfTime.format(database.getBooking(loggedInUser.getUsername()).getDate());
            int cliniqueID = database.getBooking(loggedInUser.getUsername()).getCliniqueID();
            String bookingDate = bookingDetails[0];
            appointDate.setText(bookingDate);
            appointTime.setText(strippedTime);
            appointClinique.setText(database.getClinique(cliniqueID).getName());
        }else{
            dateLabel.setVisibility(View.GONE);
            timeLabel.setVisibility(View.GONE);
            appointDate.setVisibility(View.GONE);
            appointTime.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            appointClinique.setVisibility(View.GONE);
            cliniqueLabel.setVisibility(View.GONE);
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

        DialogActivity test=new DialogActivity();
        System.out.println("Hej");
        System.out.println(loggedInUser);
        System.out.println(database.getUserVaccinations(loggedInUser.getUsername()).size());
        System.out.println(database.getBooking(loggedInUser.getUsername()));
        System.out.println("Hejdå");

        if (database.getUserVaccinations(loggedInUser.getUsername()).size()==1 && database.getBooking(loggedInUser.getUsername())==null){

            test.show(getActivity().getSupportFragmentManager(),"dialog" );

        }
        personalName.setText(loggedInUser.getName());
        //User currentuser=database.getUser()

        //database.newBooking(loggedInUser.getUsername(), "test", Timestamp.valueOf("2021-9-15 10:30:00.0"));
        //newVaccination(username, date, dose, type, getClinique(b.getCliniqueID()).getName())
        //database.newVaccination(loggedInUser.getUsername(), "2021/9/15", 2, "Pfizer", "test");
        //database.deleteVaccination(v.getId());
        boolean isTwoWeekSinceVaccionation = false;
        boolean isFullyVaccinated = false;
        for(Vaccination v: database.getUserVaccinations(loggedInUser.getUsername())){
            int dose = v.getDose();

            if(dose == 1){
                firstDose.setChecked(true);
                firstDoseDate.setText(v.getDate());
            }else if(dose == 2){
                isTwoWeekSinceVaccionation = hasBeenTwoWeeksAfterDose2(v.getDate());
                isFullyVaccinated = true;
                secondDose.setChecked(true);
                secondDoseDate.setText(v.getDate());

                bookTitle.setVisibility(View.INVISIBLE);
                bookButton.setVisibility(View.GONE);
                txtView.setVisibility(View.VISIBLE);
                qrCode.setVisibility(View.VISIBLE);
            }
        }
        if (!isTwoWeekSinceVaccionation){
            ConstraintLayout.LayoutParams buttonLayout = (ConstraintLayout.LayoutParams) qrScannerButton.getLayoutParams();
            buttonLayout.horizontalBias = 0;
            buttonLayout.setMarginStart(42);
            if(!isFullyVaccinated) {
                buttonLayout.topToBottom = bookButton.getId();
            }
            else{
                buttonLayout.topToBottom = secondDose.getId();
            }
        }
        //checks if user has upcoming vaccination appointment and then displays it in UI
        toggleVisibilityBooking(loggedInUser);

        //generates QR-code if user is "fully" (2 doses as of now) vaccinated
        VaccinePassport passport = new VaccinePassport(loggedInUser.getUsername(), qrCode, txtView);
        passport.getQRCode();


        // ask for camera permission
        ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result) {
                            Intent cameraIntent = new Intent(binding.getRoot().getContext(), CameraActivity.class);
                            cameraIntent.putExtra("ParentActivity", "mainMenuActivity");
                            startActivity(cameraIntent);
                        } else {
                            Toast.makeText(binding.getRoot().getContext(), getString(R.string.Camera_permission_denied), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //QR scanner button listener
        qrScannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPermissionResult.launch(Manifest.permission.CAMERA);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = loggedInUser.getUsername();
                Booking cancelBooking = database.getBooking(username);
                database.deleteBookings(username);
                database.deleteQuestionnaire(username);
                bookButton.setVisibility(View.VISIBLE);
                toggleVisibilityBooking(loggedInUser);
            }
        });
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent questionnaire = new Intent(root.getContext(), QuestionnaireActivity.class);
                questionnaire.putExtra("loggedInUser", loggedInUser);
                startActivity(questionnaire);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean hasBeenTwoWeeksAfterDose2(String date){
        int noOfDays = 14; //i.e two weeks
        Calendar calendar = Calendar.getInstance();
        String[] sepDate = date.split("/");
        calendar.setTime(new Date(Integer.parseInt(sepDate[0])-1900, Integer.parseInt(sepDate[1])-1, Integer.parseInt(sepDate[2])));
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        Date valid_after = calendar.getTime();
        if (new Date().after(valid_after)) {
            return true;
        }


        return false;
    }




}