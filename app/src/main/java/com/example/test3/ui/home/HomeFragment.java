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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;
import com.example.test3.DatabaseHandler.Vaccination;
import com.example.test3.R;
import com.example.test3.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private DatabaseHandler database = new DatabaseHandler("http://83.254.68.246:3003/");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Intent intent = this.getActivity().getIntent();
        User user = (User)intent.getSerializableExtra("LoggedInUser");
        //Log.e("USER", user.toString());

        EditText personalName = root.findViewById(R.id.editTextTextPersonName8);
        TextView dateLabel = root.findViewById(R.id.appointment_date_label);
        TextView timeLabel = root.findViewById(R.id.appointment_time_label);
        EditText appointDate = root.findViewById(R.id.appointment_date);
        EditText appointTime = root.findViewById(R.id.appointment_time);
        ImageButton cancelAppoint = root.findViewById(R.id.cancel_button);

        CheckBox firstDose = root.findViewById(R.id.checkBox);
        CheckBox secondDose = root.findViewById(R.id.checkBox2);
        EditText firstDoseDate = root.findViewById(R.id.first_dose_date);
        EditText secondDoseDate = root.findViewById(R.id.second_dose_date);
        firstDose.setEnabled(false);
        secondDose.setEnabled(false);
        Log.i("VACC","getting vacc");
        //Vaccination vacc = database.newVaccination(user.getUsername(),"20/10/10", 1, "Pfizer", "test");
        
        personalName.setText(user.getName());
        /*
        for(Vaccination v: database.getUserVaccinations(user.getUsername())){
            int dose = v.getDose();

            if(dose == 1){
                firstDose.setChecked(true);
                firstDoseDate.setText(v.getDate());
            }else if(dose == 2){
                secondDose.setChecked(true);
                secondDoseDate.setText(v.getDate());
            }
        }*/

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}