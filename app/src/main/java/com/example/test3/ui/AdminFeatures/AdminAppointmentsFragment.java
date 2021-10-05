package com.example.test3.ui.AdminFeatures;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.test3.DatabaseHandler.Booking;
import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;
import com.example.test3.DatabaseHandler.Vaccination;
import com.example.test3.R;
import com.example.test3.databinding.FragmentAdminAppointmentsBinding;
import com.example.test3.databinding.FragmentGalleryBinding;
import com.example.test3.ui.gallery.GalleryViewModel;
import com.google.type.Date;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminAppointmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAppointmentsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ArrayAdapter<String> adapter;
    private DatabaseHandler handler;

    private FragmentAdminAppointmentsBinding binding;
    private Spinner userSelectSpinner;
    private Spinner filterSpinner;
    private Button questionaireButton;
    private Button vaccinateButton;
    private TextView nameText;
    private TextView ageText;
    private TextView questionaireStatus;

    public AdminAppointmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminAppointmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminAppointmentsFragment newInstance(String param1, String param2) {
        AdminAppointmentsFragment fragment = new AdminAppointmentsFragment();
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
        binding = FragmentAdminAppointmentsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        questionaireButton = view.findViewById(R.id.questionaire_button);
        vaccinateButton = view.findViewById(R.id.vaccinate_button);
        nameText = view.findViewById(R.id.name_text);
        userSelectSpinner = view.findViewById(R.id.user_select_spinner);
        filterSpinner = view.findViewById(R.id.filter_spinner);
        ageText = view.findViewById(R.id.dob_text);
        Log.i("test", "Click");

        handler = new DatabaseHandler("http://83.254.68.246:3003/");
        //List<String> userList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(
                this.getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSelectSpinner.setAdapter(adapter);

        List<String> filter = new ArrayList<String>();
        filter.add("Today");
        filter.add("All");
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(
                this.getContext(), android.R.layout.simple_spinner_item, filter);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateUserSpinner(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        userSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                User user = handler.getUser(userSelectSpinner.getSelectedItem().toString());
                nameText.setText(user.getName());
                ageText.setText(user.getDateOfBirth());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        questionaireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("test", "Click");
                nameText.setText("Okej");
            }
        });

        vaccinateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = handler.getUser(userSelectSpinner.getSelectedItem().toString());
                List<Vaccination> vacc = new ArrayList<Vaccination>();
                vacc = handler.getUserVaccinations(user.getUsername());
                if(vacc.isEmpty()){
                    handler.doVaccination(user.getUsername(),1,"Moderna");
                    Log.i(user.getUsername(),"Ingen dos");
                }
                else{
                    handler.doVaccination(user.getUsername(),2,"Moderna");
                    Log.i(user.getUsername(),"En dos");
                }
                updateUserSpinner(filterSpinner.getSelectedItemPosition());
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    void updateUserSpinner(int selection){
        adapter.clear();
        switch(selection){
            case 0:
                for (Booking b: handler.getBookingsToday()) {
                    adapter.add(b.getUsername());
                }
                adapter.notifyDataSetChanged();
                userSelectSpinner.setEnabled(!adapter.isEmpty());
                if(adapter.isEmpty()){
                    nameText.setText("name");
                    ageText.setText("Date of birth");
                }
                break;
            case 1:
                for (Booking b: handler.getBookings()) {
                    adapter.add(b.getUsername());
                }
                adapter.notifyDataSetChanged();
                userSelectSpinner.setEnabled(!adapter.isEmpty());
                if(adapter.isEmpty()){
                    nameText.setText("Name");
                    ageText.setText("Date of birth");
                }
                break;

        }
        if(!adapter.isEmpty()){
            userSelectSpinner.setSelection(0,true);
        }
    }
}