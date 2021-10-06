package com.example.test3.ui.gallery;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.test3.DataExtraction.CovidCasesSweden;
import com.example.test3.DataExtraction.CovidData;
import com.example.test3.DataExtraction.CovidVaccineSweden;
import com.example.test3.DataExtraction.DataExtractor;
import com.example.test3.MainActivity;
import com.example.test3.R;
import com.example.test3.databinding.FragmentGalleryBinding;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GalleryFragment extends Fragment{

    public static String in;
    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    TextView filter;
    boolean[] selectedFilters;
    ArrayList<Integer> filterList = new ArrayList<>();
    String[] filterArray;
    String chosenStat;
    Context mContext;



    public void determineDataRepresentation(String chosenFilters){
        CovidData covidData = MainActivity.covidData;
        View root = binding.getRoot();
        ListView listView = (ListView) root.findViewById(R.id.list_view);
        int index, length, length2, i, k, w, y;
        String[] countyGroup2 = {"Sverige", "Stockholm", "Uppsala",
                "Södermanland", "Östergötland", "Jönköping", "Kronoberg",
                "Kalmar", "Gotland", "Blekinge", "Skåne", "Halland",
                "Västra Götaland", "Värmland", "Örebro", "Västmanland",
                "Dalarna", "Gävleborg", "Västernorrland", "Jämtland",
                "Västerbotten" ,"Norrbotten"};

        String[] countyGroup = {"Sverige", "Blekinge", "Dalarna", "Gotland",
                "Gävleborg", "Halland", "Jämtland Härjedalen", "Jönköping",
                "Kalmar", "Kronoberg", "Norrbotten", "Skåne", "Stockholm",
                "Sörmland", "Uppsala", "Värmland", "Västerbotten", "Västernorrland",
                "Västmanland", "Västra Götaland", "Örebro", "Östergötland" };


        String[] ageGroup = {"Age_0_9", "Age_10_19", "Age_20_29",
                "Age_30_39", "Age_40_49","Age_50_59",  "Age_60_69",
                "Age_70_79", "Age_80_89", "Age_90_plus"};

        String[] vacineGroup = {"Pfizer", "Moderna", "AstraZeneca"};


        String[] countyGroup3 = { "Sverige", "Blekinge", "Dalarna", "Gotland",
                "Gävleborg", "Halland", "Jämtland", "Jönköping", "Kalmar", "Kronoberg",
                "Norrbotten", "Skåne", "Stockholm", "Södermanland", "Uppsala", "Värmland",
                "Västerbotten", "Västernorrland", "Västmanland", "Västra Götaland", "Örebro",
                "Östergötland"};

        switch(chosenStat){

            case "Total doses distributed":
                switch(chosenFilters){
                    case "By county":
                        Log.i("DBC", "start");
                        in = "DBC";
                        int index2;
                        int sum = 0, sumTot = 0;
                        index = covidData.getSwedenVaccine().get(0).distributedWeeklyFindWeek(20, 2021);
                        length = countyGroup3.length;
                        List<CovidVaccineSweden.VaccineDistributedWeekly> listDBC;
                        ArrayList<dispData> listDBC2 = new ArrayList<>();

                        for(i = 0; i < length; i++)
                        {
                            index2 = covidData.findSwedenVaccineRegion(countyGroup3[i]);
                            listDBC = covidData.getSwedenVaccine().get(index2).getDistributedWeekly();

                            w = 52;
                            y= 2020;
                            sum = 0;
                            sumTot = 0;
                            while(covidData.getSwedenVaccine().get(0).distributedWeeklyHasWeek(w, y))
                            {
                                index = covidData.getSwedenVaccine().get(0).distributedWeeklyFindWeek(w, y);
                                sum = listDBC.get(index).getPfizer() + listDBC.get(index).getAstraZeneca() + listDBC.get(index).getModerna()
                                listDBC2.add(new dispData("Year: " + y + " Week: " + w,5 , sum));

                                if(w == 53)
                                {
                                    w = 1;
                                    y = y + 1;
                                }
                                w = w + 1;
                                sumTot = sumTot + listDBC.get(index).getPfizer() + listDBC.get(index).getAstraZeneca() + listDBC.get(index).getModerna();

                            }

                            CustomArrayAdapter adapterBP = new CustomArrayAdapter(
                                    getContext(),
                                    R.layout.custom_list_view,
                                    listDBC2);
                            listView.setAdapter(adapterBP);


                        }



                        Log.i("DBC", "end");
                        Log.i("DBC", "--");

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
                        Log.i("ABC", "start");
                        in = "ABC";

                        ArrayList<dispData> listABC3 = new ArrayList<>();
                        length = countyGroup2.length;

                        for(i = 0 ; i < length; i++)
                        {
                            index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                            List<CovidVaccineSweden.AgeGroupReport> listABC2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                            listABC3.add(new dispData(countyGroup2[i], listABC2.get(9).getDose1(), listABC2.get(9).getDose2()));
                        }
                        CustomArrayAdapter adapterABC = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listABC3);
                        listView.setAdapter(adapterABC);
                        Log.i("ABC", "end");
                        break;
                    case "By one dose, By two doses, By product":
                        Log.i("BP", "start");
                        in = "BP";
                        i = 0;
                        index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                        ArrayList<dispData> listBP3 = new ArrayList<>();
                        List<CovidVaccineSweden.AgeGroupReport> listBP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        listBP3.add(new dispData(vacineGroup[0], listBP2.get(9).getDose1Pfizer(), listBP2.get(9).getDose2Pfizer()));
                        listBP3.add(new dispData(vacineGroup[1], listBP2.get(9).getDose1Moderna(), listBP2.get(9).getDose2Moderna()));
                        listBP3.add(new dispData(vacineGroup[2], listBP2.get(9).getDose1AstraZeneca(), listBP2.get(9).getDose2AstraZeneca()));

                        CustomArrayAdapter adapterBP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listBP3);
                        listView.setAdapter(adapterBP);
                        Log.i("BP", "end");
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
                        Log.i("BCBP", "start");
                        in = "BCBP";

                        index = covidData.findSwedenCasesAndDeathsRegion(countyGroup2[0]);
                        List<CovidVaccineSweden.AgeGroupReport> listBCBP = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        ArrayList<dispData> listBCBP3 = new ArrayList<>();
                        length = countyGroup2.length;
                        length2 = vacineGroup.length;
                        for(k = 0; k < length2; k++)
                        {
                            for (i = 0; i < length; i++)
                            {
                                index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                                List<CovidVaccineSweden.AgeGroupReport> listBCBP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                                if(k == 0)
                                    listBCBP3.add(new dispData(countyGroup2[i] + "\n" + vacineGroup[k], listBCBP2.get(9).getDose1Pfizer(), listBCBP2.get(9).getDose2Pfizer()));
                                if(k == 1)
                                    listBCBP3.add(new dispData(countyGroup2[i] + "\n" + vacineGroup[k], listBCBP2.get(9).getDose1Moderna(), listBCBP2.get(9).getDose2Moderna()));
                                if(k == 2)
                                    listBCBP3.add(new dispData(countyGroup2[i] + "\n" + vacineGroup[k], listBCBP2.get(9).getDose1AstraZeneca(), listBCBP2.get(9).getDose2AstraZeneca()));
                            }
                        }
                        CustomArrayAdapter adapterBCBP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listBCBP3);
                        listView.setAdapter(adapterBCBP);
                        Log.i("BCBP", "end");
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
                        Log.i("BC", "start");
                        in = "BC";


                        ArrayList<dispData> listBC = new ArrayList<>();
                        length = countyGroup.length;
                        for(i = 0 ; i < length; i++)
                        {
                            index = covidData.findSwedenCasesAndDeathsRegion(countyGroup[i]);
                            CovidCasesSweden.AgeGroupReport listBC2 = covidData.getSwedenCasesAndDeaths().get(index).getAgeGroupReport("Total");
                            listBC.add(new dispData(countyGroup[i], listBC2.getCases(), listBC2.getDeaths()));

                        }

                        CustomArrayAdapter adapter2 = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listBC);
                        listView.setAdapter(adapter2);
                        Log.i("BC", "end");
                        break;
                    case "By age group":
                        Log.i("BAG", "start");
                        in = "BAG";


                        ArrayList<dispData> listBAG = new ArrayList<>();
                        length = ageGroup.length;
                        for(i = 0 ; i < length; i++)
                        {
                            index = covidData.findSwedenCasesAndDeathsRegion("Sverige");
                            CovidCasesSweden.AgeGroupReport listBAG2 = covidData.getSwedenCasesAndDeaths().get(index).getAgeGroupReport(ageGroup[i]);
                            listBAG.add(new dispData(ageGroup[i], listBAG2.getCases(), listBAG2.getDeaths()));

                        }

                        CustomArrayAdapter adapterBAG = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listBAG);
                        listView.setAdapter(adapterBAG);
                        Log.i("BAG", "end");

                        break;
                    case "By county, By age group":
                        Log.i("BCBAG", "start");
                        in = "BCBAG";

                        ArrayList<dispData> listBCBAG = new ArrayList<>();
                        length = ageGroup.length;
                        length2 = countyGroup.length;
                        for(k = 0; k < length2; k++) {
                            for (i = 0; i < length; i++) {
                                index = covidData.findSwedenCasesAndDeathsRegion(countyGroup[k]);
                                CovidCasesSweden.AgeGroupReport listBCBAG2 = covidData.getSwedenCasesAndDeaths().get(index).getAgeGroupReport(ageGroup[i]);
                                listBCBAG.add(new dispData(countyGroup[k] + ": " + ageGroup[i], listBCBAG2.getCases(), listBCBAG2.getDeaths()));

                            }
                        }
                        CustomArrayAdapter adapterBCBAG = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listBCBAG);
                        listView.setAdapter(adapterBCBAG);
                        Log.i("BCBAG", "end");
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
/*

String[] countyGroup = {"Sverige", "Blekinge", "Dalarna", "Gotland",
                                "Gävleborg", "Halland", "Jämtland Härjedalen", "Jönköping",
                                "Kalmar", "Kronoberg", "Norrbotten", "Skåne", "Stockholm",
                                "Sörmland", "Uppsala", "Värmland", "Västerbotten", "Västernorrland",
                                "Västmanland", "Västra Götaland", "Örebro", "Östergötland" };

graph.getViewport().setXAxisBoundsManual(true);
                        graph.getViewport().setMinX(0);

                        String[] ageGroup = {"Age_0_9", "Age_10_19", "Age_20_29", "Age_30_39", "Age_40_49","Age_50_59",  "Age_60_69", "Age_70_79", "Age_80_89", "Age_90_plus"};
                        index = covidData.findSwedenCasesAndDeathsRegion("Sverige");

                        graph.removeAllSeries();
                        length = ageGroup.length;
                        x1 = 1;
                        x2 = 0;
                        graph.getViewport().setMaxX(length*2);


                        for(i = 1; i < length+1; i ++){
                            CovidCasesSweden.AgeGroupReport list1 = covidData.getSwedenCasesAndDeaths().get(index).getAgeGroupReport(ageGroup[i-1]);

                            y = list1.getCases();
                            x1 = x2 +1;
                            series1.appendData(new DataPoint(x1, y), true, length*2);

                            x2 = x1 +1 ;
                            y = list1.getDeaths();
                            series2.appendData(new DataPoint(x2, y), true, length*2);
                        }
                        graph.addSeries(series1);
                        graph.addSeries(series2);

                        series1.setColor(Color.BLUE);
                        series1.setDrawValuesOnTop(true);
                        series1.setValuesOnTopColor(Color.BLUE);
                        series1.setValuesOnTopSize(25);
                        series1.setSpacing(25);

                        series2.setColor(Color.RED);
                        series2.setDrawValuesOnTop(true);
                        series2.setValuesOnTopColor(Color.RED);
                        series2.setValuesOnTopSize(25);
                        series2.setSpacing(25);

 */