package com.example.test3.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.test3.R;
import com.example.test3.databinding.FragmentGalleryBinding;

import java.util.ArrayList;
import java.util.Collections;

public class GalleryFragment extends Fragment{

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView filtertxt = root.findViewById(R.id.filter_text);
        final Spinner dashboardOptions = root.findViewById(R.id.dashboard_options);
        final Spinner dashboardFilter = root.findViewById(R.id.dashboard_filter);

        ArrayAdapter<CharSequence> optionsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.dashboard_stats, android.R.layout.simple_spinner_item);
        optionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dashboardOptions.setAdapter(optionsAdapter);

        dashboardOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String chosenStat = adapterView.getItemAtPosition(i).toString();
                ArrayAdapter<CharSequence> filterAdapter;
                if(!chosenStat.equals("-")){
                    dashboardFilter.setVisibility(View.VISIBLE);
                    filtertxt.setVisibility(View.VISIBLE);
                }

                switch(chosenStat){
                    case "Total doses distributed":
                        filterAdapter = ArrayAdapter.createFromResource(getContext(),
                                R.array.filter_total_dist, android.R.layout.simple_spinner_item);
                        dashboardFilter.setAdapter(filterAdapter);
                        break;
                    case "Total doses administered":
                        filterAdapter = ArrayAdapter.createFromResource(getContext(),
                                R.array.filter_total_admin, android.R.layout.simple_spinner_item);
                        dashboardFilter.setAdapter(filterAdapter);
                        break;
                    case "Cumulative uptake (%)":
                        filterAdapter = ArrayAdapter.createFromResource(getContext(),
                                R.array.filter_cumulative_uptake, android.R.layout.simple_spinner_item);
                        dashboardFilter.setAdapter(filterAdapter);
                        break;
                    case "Total cases and deaths":
                        filterAdapter = ArrayAdapter.createFromResource(getContext(),
                                R.array.filter_cases_deaths, android.R.layout.simple_spinner_item);
                        dashboardFilter.setAdapter(filterAdapter);
                        break;
                    case "-":
                        dashboardFilter.setVisibility(View.INVISIBLE);
                        filtertxt.setVisibility(View.INVISIBLE);
                        break;
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        dashboardFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //display different statistics here
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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