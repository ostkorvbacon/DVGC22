package com.example.test3.ui.AdminFeatures;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;
    private boolean mParam3;


    private ArrayAdapter<String> adapter;
    private DatabaseHandler handler;
    private AdminAppointmentsViewModel viewModel;

    private User user;

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
    public static AdminAppointmentsFragment newInstance(int param1, int param2, boolean param3) {
        AdminAppointmentsFragment fragment = new AdminAppointmentsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
            mParam3 = getArguments().getBoolean(ARG_PARAM3);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAdminAppointmentsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(requireActivity()).get(AdminAppointmentsViewModel.class);

        questionaireButton = view.findViewById(R.id.questionaire_button);
        vaccinateButton = view.findViewById(R.id.vaccinate_button);
        vaccinateButton.setEnabled(false);

        nameText = view.findViewById(R.id.name_text);
        questionaireStatus = view.findViewById(R.id.questionaire_status_text);
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
        filter.add(getString(R.string.today));
        filter.add(getString(R.string.all));
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
                    updateUser();
                    questionaireButton.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                questionaireButton.setEnabled(false);
            }
        });
        questionaireButton.setEnabled(false);
        questionaireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setCurrentUser(user);
                AdminQuestionaireAssesmentFragment fragment = new AdminQuestionaireAssesmentFragment();
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(container.getId(), fragment,"questionaire_admin");
                ft.hide(fm.getPrimaryNavigationFragment());
                ft.addToBackStack(this.getClass().getName());
                ft.commit();
            }
        });

        vaccinateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = handler.getUser(userSelectSpinner.getSelectedItem().toString());
                List<Vaccination> vacc = new ArrayList<Vaccination>();
                vacc = handler.getUserVaccinations(user.getUsername());
                Booking booking = handler.getBooking(user.getUsername());
                if(vacc.isEmpty()){
                    if(booking.getType().equals("Pfizer")){
                        Log.i("Vac", "Doing vaccination...");
                        handler.doVaccination(user.getUsername(),1,"Pfizer");
                        handler.setPfizerQuantity(handler.getPfizerQuantity()-1);
                    }
                    if(booking.getType().equals("Moderna")){
                        handler.doVaccination(user.getUsername(),1,"Moderna");
                        handler.setModernaQuantity(handler.getModernaQuantity()-1);
                    }
                    if(booking.getType().equals("Astra")){
                        handler.doVaccination(user.getUsername(),1,"Astra");
                        handler.setModernaQuantity(handler.getModernaQuantity()-1);
                    }
                }
                else{
                    if(booking.getType().equals("Pfizer")){
                        handler.doVaccination(user.getUsername(),2,"Pfizer");
                        handler.setPfizerQuantity(handler.getPfizerQuantity()-1);
                    }
                    if(booking.getType().equals("Moderna")){
                        handler.doVaccination(user.getUsername(),2,"Moderna");
                        handler.setModernaQuantity(handler.getModernaQuantity()-1);
                    }
                    if(booking.getType().equals("Astra")){
                        handler.doVaccination(user.getUsername(),2,"Astra");
                        handler.setModernaQuantity(handler.getModernaQuantity()-1);
                    }
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
                /*if(adapter.isEmpty()){
                    nameText.setText("name");
                    ageText.setText("Date of birth");
                }*/
                break;
            case 1:
                for (Booking b: handler.getBookings()) {
                    adapter.add(b.getUsername());
                }
                adapter.notifyDataSetChanged();
                userSelectSpinner.setEnabled(!adapter.isEmpty());
                /*if(adapter.isEmpty()){
                    nameText.setText("Name");
                    ageText.setText("Date of birth");
                }*/
                break;

        }
        if(!adapter.isEmpty()){
            userSelectSpinner.setSelection(0,true);
            questionaireButton.setEnabled(true);
            updateUser();
        }
        else{
            questionaireButton.setEnabled(false);
        }
    }

    void updateUser(){
        user = handler.getUser(userSelectSpinner.getSelectedItem().toString());
        nameText.setText(user.getName());
        ageText.setText(user.getDateOfBirth());
        if(handler.getQuestionnaire(user.getUsername()) != null){
            vaccinateButton.setEnabled(handler.getQuestionnaire(user.getUsername()).isApproved());
            if(handler.getQuestionnaire(user.getUsername()).isApproved()){
                questionaireStatus.setText(getString(R.string.approved));
            }
            else{
                questionaireStatus.setText(getString(R.string.pending));
            }
        }
        else{
            vaccinateButton.setEnabled(false);
            questionaireStatus.setText(getString(R.string.pending));

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            updateUser();
        }
        super.onHiddenChanged(hidden);
    }
}