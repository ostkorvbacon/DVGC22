package com.example.test3.ui.AdminFeatures;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.test3.BookingsActivity;
import com.example.test3.DatabaseHandler.AgeGroupSchedule;
import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.Vaccination;
import com.example.test3.MainActivity;
import com.example.test3.MainMenuActivity;
import com.example.test3.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminScheduleFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private DatabaseHandler database = new DatabaseHandler("http://83.254.68.246:3003/");

    private TextView ageLabel;
    private TextView dateLabel;
    private TextView noSchedules;
    private Button addButton;
    private EditText minAge;
    private EditText maxAge;
    private EditText scheduleDate;
    private EditText age0;
    private EditText age1;
    private EditText age2;
    private EditText age3;
    private EditText age4;
    private EditText date0;
    private EditText date1;
    private EditText date2;
    private EditText date3;
    private EditText date4;
    private ImageButton cancel0;
    private ImageButton cancel1;
    private ImageButton cancel2;
    private ImageButton cancel3;
    private ImageButton cancel4;
    private List<AgeGroupSchedule> schedules;
    DatePickerDialog picker;
    int chosenDay, chosenMonth, chosenYear;

    public AdminScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminScheduleFragment newInstance(String param1, String param2) {
        AdminScheduleFragment fragment = new AdminScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_admin_schedule, container, false);
        //current_age_group = root.findViewById(R.id.current_age_group);
        addButton = root.findViewById(R.id.apply_admin_settings);
        minAge = root.findViewById(R.id.min_age);
        maxAge = root.findViewById(R.id.max_age);
        scheduleDate = root.findViewById(R.id.schedule_date);
        scheduleDate.setInputType(InputType.TYPE_NULL);
        ageLabel = root.findViewById(R.id.upcoming_age_label);
        dateLabel = root.findViewById(R.id.upcoming_date_label);
        noSchedules = root.findViewById(R.id.no_schedules);
        age0 = root.findViewById(R.id.age0);
        age1 = root.findViewById(R.id.age1);
        age2 = root.findViewById(R.id.age2);
        age3 = root.findViewById(R.id.age3);
        age4 = root.findViewById(R.id.age4);
        date0 = root.findViewById(R.id.date0);
        date1 = root.findViewById(R.id.date1);
        date2 = root.findViewById(R.id.date2);
        date3 = root.findViewById(R.id.date3);
        date4 = root.findViewById(R.id.date4);
        cancel0 = root.findViewById(R.id.cancel0);
        cancel1 = root.findViewById(R.id.cancel1);
        cancel2 = root.findViewById(R.id.cancel2);
        cancel3 = root.findViewById(R.id.cancel3);
        cancel4 = root.findViewById(R.id.cancel4);

        ImageButton[] cancelButtons = {cancel0, cancel1, cancel2, cancel3, cancel4};
        EditText[] ageFields = {age0, age1, age2, age3, age4};
        EditText[] dateFields = {date0, date1, date2, date3, date4};

        updateSchedules(ageFields, dateFields, cancelButtons);

        scheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // date picker dialog
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                chosenDay = dayOfMonth;
                                chosenMonth = (monthOfYear + 1);
                                chosenYear = year;
                                scheduleDate.setText(chosenYear + "/" + chosenMonth + "/" + chosenDay);
                            }
                        }, year, month, day);
                Calendar cal = Calendar.getInstance();
                picker.getDatePicker().setMinDate(cal.getTimeInMillis());
                picker.show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(minAge.getText() != null &&
                        maxAge.getText() != null &&
                        scheduleDate.getText() != null) {
                    int minimumAge = Integer.valueOf(minAge.getText().toString());
                    int maximumAge = Integer.valueOf(maxAge.getText().toString());
                    String date = scheduleDate.getText().toString();

                    database.newAgeGroupSchedule(minimumAge, maximumAge, date);
                    updateSchedules(ageFields, dateFields, cancelButtons);
                }
                //closes keyboard and focus on edittext
                if (getActivity() == null) return;
                if (getActivity().getCurrentFocus() == null) return;
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                scheduleDate.clearFocus();
            }
        });

        cancel0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.deleteAgeGroupSchedule(schedules.get(0).getId());
                updateSchedules(ageFields, dateFields, cancelButtons);
            }
        });
        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.deleteAgeGroupSchedule(schedules.get(1).getId());
                updateSchedules(ageFields, dateFields, cancelButtons);
            }
        });
        cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.deleteAgeGroupSchedule(schedules.get(2).getId());
                updateSchedules(ageFields, dateFields, cancelButtons);
            }
        });
        cancel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.deleteAgeGroupSchedule(schedules.get(3).getId());
                updateSchedules(ageFields, dateFields, cancelButtons);
            }
        });
        cancel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.deleteAgeGroupSchedule(schedules.get(4).getId());
                updateSchedules(ageFields, dateFields, cancelButtons);
            }
        });
        return root;
    }
    private void updateSchedules(EditText[] ageFields, EditText[] dateFields, ImageButton[] cancelButtons){
        schedules = database.getAgeGroupSchedules();
        for (int k = 0; k < 5; k++) {
            ageFields[k].setVisibility(View.INVISIBLE);
            dateFields[k].setVisibility(View.INVISIBLE);
            if (schedules.size() <= k) {
                cancelButtons[k].setVisibility(View.INVISIBLE);
            } else {
                cancelButtons[k].setVisibility(View.VISIBLE);
            }
        }
        int i = 0;
        if(!schedules.isEmpty()){
            Log.i("VIS", schedules.toString());
            noSchedules.setVisibility(View.GONE);
            if(schedules.size() != 6){
                for (AgeGroupSchedule schedule : schedules) {
                    ageFields[i].setText(schedule.getMinAge() + " - " + schedule.getMaxAge());
                    dateFields[i].setText(schedule.getDate());
                    ageFields[i].setVisibility(View.VISIBLE);
                    dateFields[i].setVisibility(View.VISIBLE);
                    i++;
                    if (i >= 5) {
                        break;
                    }
                }
            }
        }else{
            ageLabel.setVisibility(View.GONE);
            dateLabel.setVisibility(View.GONE);
            noSchedules.setVisibility(View.VISIBLE);
        }
    }
}