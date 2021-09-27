package com.example.test3.ui.gallery;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
    TextView filter;
    boolean[] selectedFilters;
    ArrayList<Integer> filterList = new ArrayList<>();
    String[] filterArray;
    String chosenStat;

    public void determineDataRepresentation(String chosenFilters){
        switch(chosenStat){

            case "Total doses distributed":
                switch(chosenFilters){
                    case "By county":

                        break;
                    case "By product":

                        break;
                    case "By county, By product":

                        break;
                }
                break;

            case "Total doses administered":
                //saknar en fyra-kombination tror jag
                switch(chosenFilters){
                    case "By one dose":
                        break;
                    case "By one dose, By two doses":
                        break;
                    case "By one dose, By age group":
                        break;
                    case "By one dose, By county":
                        break;
                    case "By one dose, By product":
                        break;
                    case "By one dose, By two doses, By age group":
                        break;
                    case "By one dose, By two doses, By county":
                        break;
                    case "By one dose, By two doses, By product":
                        break;
                    case "By one dose, By age group, By County":
                        break;
                    case "By one dose, By age group, By product":
                        break;
                    case "By one dose, By county, By product":
                        break;
                    case "By one dose, By two doses, By age group, By county":
                        break;
                    case "By one dose, By two doses, By age group, By product":
                        break;
                    case "By one dose, By age group, By county, By product":
                        break;
                    case "By one dose, By two doses, By age group, By county, By product":
                        break;

                    case "By two doses":
                        break;
                    case "By two doses, By age group":
                        break;
                    case "By two doses, By county":
                        break;
                    case "By two doses, By product":
                        break;
                    case "By two doses, By age group, By county":
                        break;
                    case "By two doses, By age group, By product":
                        break;
                    case "By two doses, By county, By product":
                        break;
                    case "By two doses, By age group, By county, By product":
                        break;

                    case "By age group":
                        break;
                    case "By age group, By county":
                        break;
                    case "By age group, By product":
                        break;
                    case "By age group, By county, By product":
                        break;

                    case "By county":
                        break;
                    case "By county, By product":
                        break;

                    case "By product":
                        break;
                }
                break;

            case "Cumulative uptake (%)":
                switch(chosenFilters){
                    case "By week":

                        break;
                    case "By month":

                        break;
                    case "By week, By month":

                        break;
                }
                break;

            case "Total cases and deaths":
                switch(chosenFilters){
                    case "By county":

                        break;
                    case "By age group":

                        break;
                    case "By county, By age group":

                        break;
                }
                break;
        }
    }

    public void filter_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filters");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(filterArray, selectedFilters, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if(b){
                    filterList.add(i);
                    Collections.sort(filterList);
                }else{//when checkbox unselected
                    for(int k = 0; k < filterList.size(); k++) {
                        if(filterList.get(k) == i){
                            filterList.remove(k);
                            break;
                        }
                    }
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            StringBuilder chosenFilters = new StringBuilder();
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(filterList.size() != 0) {
                    for (int k = 0; k < filterList.size(); k++) {
                        chosenFilters.append(filterArray[filterList.get(k)]);
                        if (k != filterList.size() - 1) {
                            chosenFilters.append(", ");
                        }
                    }
                    filter.setText(chosenFilters.toString());
                }else {
                    Toast toast = Toast.makeText(getContext(),
                            "Must choose at least one filter.",
                            Toast.LENGTH_LONG);
                    toast.show();
                    filterList.add(0);
                    selectedFilters[0] = true;
                    chosenFilters.append(filterArray[0]);
                    filter.setText(chosenFilters.toString());
                }
                determineDataRepresentation(chosenFilters.toString());
            }
        });
        builder.show();
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        filter = root.findViewById(R.id.filter_text);
        final Spinner dashboardOptions = root.findViewById(R.id.dashboard_options);
        ArrayAdapter<CharSequence> optionsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.dashboard_stats, android.R.layout.simple_spinner_item);
        optionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dashboardOptions.setAdapter(optionsAdapter);

        dashboardOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenStat = adapterView.getItemAtPosition(i).toString();

                switch(chosenStat){
                    case "Total doses distributed":
                        filterArray = getResources().getStringArray(R.array.filter_total_dist);
                        selectedFilters = new boolean[filterArray.length];
                        break;
                    case "Total doses administered":
                        filterArray = getResources().getStringArray(R.array.filter_total_admin);
                        selectedFilters = new boolean[filterArray.length];
                        break;
                    case "Cumulative uptake (%)":
                        filterArray = getResources().getStringArray(R.array.filter_cumulative_uptake);
                        selectedFilters = new boolean[filterArray.length];
                        break;
                    case "Total cases and deaths":
                        filterArray = getResources().getStringArray(R.array.filter_cases_deaths);
                        selectedFilters = new boolean[filterArray.length];
                        break;
                    case "-":
                        filter.setVisibility(View.INVISIBLE);
                        ((TextView)root.findViewById(R.id.filter_legend)).setVisibility(View.INVISIBLE);
                        break;
                }
                if(!chosenStat.equals("-")){
                    ((TextView)root.findViewById(R.id.filter_legend)).setVisibility(View.VISIBLE);
                    filter.setVisibility(View.VISIBLE);
                    filterList.clear();
                    filterList.add(0);
                    selectedFilters[0] = true;
                    filter_dialog();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chosenCategoryAndFilter = filter_dialog();
                //logic for determining which data to show
                filter_dialog();
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