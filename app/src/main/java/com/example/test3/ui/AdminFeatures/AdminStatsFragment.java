package com.example.test3.ui.AdminFeatures;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test3.DataExtraction.CovidCasesSweden;
import com.example.test3.DataExtraction.CovidData;
import com.example.test3.DataExtraction.CovidVaccineSweden;
import com.example.test3.DataExtraction.DataExtractor;
import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.R;
import com.example.test3.databinding.FragmentAdminAppointmentsBinding;
import com.example.test3.databinding.FragmentAdminStatsBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminStatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentAdminStatsBinding binding;
    private DatabaseHandler handler;
    private DataExtractor extractor;
    private CovidData covidData;

    private TextView usersText;
    private TextView vaccineText;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminStatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminStatsFragment newInstance(String param1, String param2) {
        AdminStatsFragment fragment = new AdminStatsFragment();
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
        handler = new DatabaseHandler("http://83.254.68.246:3003/");
        extractor = new DataExtractor();

        DataExtractor data = new DataExtractor();
        Thread downloadCovidDataThread = new Thread(data);
        downloadCovidDataThread.start();
        try {
            downloadCovidDataThread.join();
            covidData = data.getCovidData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminStatsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        usersText = view.findViewById(R.id.number_of_users_text);
        vaccineText = view.findViewById(R.id.vaccine_storage_text);

        usersText.setText("Current registered users:\n" + String.valueOf(handler.getUserList().size()));

        int pzifer=0,moderna=0,astraZeneca=0, total=0;

        pzifer = handler.getPfizerQuantity();
        moderna = handler.getModernaQuantity();
        astraZeneca = handler.getAstraQuantity();

        vaccineText.setText("Available Vaccines\n\n" + "Pzifer: " + NumberFormat.getIntegerInstance().format(pzifer) + "\nModerna: " + NumberFormat.getIntegerInstance().format(moderna) + "\nAstra Zeneca: " + NumberFormat.getIntegerInstance().format(astraZeneca) + "\n");

        // Inflate the layout for this fragment
        return view;
    }
}