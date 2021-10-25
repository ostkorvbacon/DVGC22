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
import android.widget.ImageView;
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
import com.jjoe64.graphview.series.Series;

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
    ImageView categoryIcon, filterIcon;



    public void determineDataRepresentation(String chosenFilters){
        CovidData covidData = MainActivity.covidData;
        View root = binding.getRoot();
        ListView listView = (ListView) root.findViewById(R.id.list_view);
        int index, index2, length, length2, i, k, w, y, m;
        int sum = 0, sumTot = 0;
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
                                sum = listDBC.get(index).getPfizer() + listDBC.get(index).getAstraZeneca() + listDBC.get(index).getModerna();
                                listDBC2.add(new dispData(countyGroup3[i], "Year: " + y + " Week: " + w, String.valueOf(sum)));
                                if(w == 53)
                                {
                                    w = 1;
                                    y = y + 1;
                                }
                                else { w = w + 1; }
                                sumTot = sumTot + listDBC.get(index).getPfizer() + listDBC.get(index).getAstraZeneca() + listDBC.get(index).getModerna();
                            }
                            listDBC2.add(new dispData(countyGroup3[i], "Total: ", String.valueOf(sumTot)));
                        }
                        CustomArrayAdapter adapterDBC = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listDBC2);
                        listView.setAdapter(adapterDBC);
                        Log.i("DBC", "end");
                        break;
                    case "By product":
                        Log.i("DBP", "start");
                        in = "DBP";
                        index = covidData.getSwedenVaccine().get(0).distributedWeeklyFindWeek(20, 2021);
                        length = countyGroup3.length;
                        List<CovidVaccineSweden.VaccineDistributedWeekly> listDBP;
                        ArrayList<dispData> listDBP2 = new ArrayList<>();
                        index2 = covidData.findSwedenVaccineRegion(countyGroup3[0]);
                        listDBP = covidData.getSwedenVaccine().get(index2).getDistributedWeekly();
                        w = 52;
                        y= 2020;
                        sum = 0;
                        sumTot = 0;
                        while(covidData.getSwedenVaccine().get(0).distributedWeeklyHasWeek(w, y))
                        {
                            index = covidData.getSwedenVaccine().get(0).distributedWeeklyFindWeek(w, y);
                            sum = listDBP.get(index).getPfizer() + listDBP.get(index).getAstraZeneca() + listDBP.get(index).getModerna();
                            listDBP2.add(new dispData( "Year: " + y + "\nWeek: " + w,
                            "Total: " + String.valueOf(sum) +
                                "\nPfizer: " + String.valueOf(listDBP.get(index).getPfizer()),
                            "Moderna: " + String.valueOf(listDBP.get(index).getModerna()) +
                                "\nAstraZeneca: " + String.valueOf(listDBP.get(index).getAstraZeneca())
                            ));
                            if(w == 53)
                            {
                                w = 1;
                                y = y + 1;
                            }
                            else { w = w + 1; }
                        }
                        CustomArrayAdapter adapterDBP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view2,
                                listDBP2);
                        listView.setAdapter(adapterDBP);
                        Log.i("DBP", "end");
                        break;
                    case "By county, By product":
                        Log.i("DBCBP", "start");
                        in = "DBCBP";
                        index = covidData.getSwedenVaccine().get(0).distributedWeeklyFindWeek(20, 2021);
                        length = countyGroup3.length;
                        List<CovidVaccineSweden.VaccineDistributedWeekly> listDBCBP;
                        ArrayList<dispData> listDBCBP2 = new ArrayList<>();
                        for(i = 0; i < length; i++)
                        {
                            index2 = covidData.findSwedenVaccineRegion(countyGroup3[i]);
                            listDBCBP = covidData.getSwedenVaccine().get(index2).getDistributedWeekly();
                            w = 52;
                            y= 2020;
                            sum = 0;
                            sumTot = 0;
                            while(covidData.getSwedenVaccine().get(0).distributedWeeklyHasWeek(w, y))
                            {
                                index = covidData.getSwedenVaccine().get(0).distributedWeeklyFindWeek(w, y);
                                sum = listDBCBP.get(index).getPfizer() + listDBCBP.get(index).getAstraZeneca() + listDBCBP.get(index).getModerna();
                                listDBCBP2.add(new dispData(countyGroup3[i] + "\nYear: " + y + "\nWeek: " + w,
                                        "Total: " + String.valueOf(sum) +
                                                "\nPfizer: " + String.valueOf(listDBCBP.get(index).getPfizer()),
                                        "Moderna: " + String.valueOf(listDBCBP.get(index).getModerna()) +
                                                "\nAstraZeneca: " + String.valueOf(listDBCBP.get(index).getAstraZeneca())));
                                if(w == 53) {
                                    w = 1;
                                    y = y + 1; }
                                else { w = w + 1; }
                            }
                        }
                        CustomArrayAdapter adapterDBCBP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view2,
                                listDBCBP2);
                        listView.setAdapter(adapterDBCBP);
                        Log.i("DBC", "end");
                        break;
                }
                break;

            case "Total doses administered":
                //saknar en fyra-kombination tror jag
                switch(chosenFilters){
                    case "By one dose":
                        Log.i("OB", "start");
                        in = "OB";
                        i = 0;
                        index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                        ArrayList<dispData> listOB3 = new ArrayList<>();
                        List<CovidVaccineSweden.AgeGroupReport> listOB2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        listOB3.add(new dispData("Total: ",
                                String.valueOf(listOB2.get(9).getDose1Pfizer() + listOB2.get(9).getDose1Moderna() + listOB2.get(9).getDose1AstraZeneca()),
                                String.valueOf(listOB2.get(9).getDose2Pfizer() + listOB2.get(9).getDose2Moderna() + listOB2.get(9).getDose2AstraZeneca())));


                        CustomArrayAdapter adapterOB = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listOB3);
                        listView.setAdapter(adapterOB);
                        Log.i("OB", "end");
                        break;
                    case "By one dose, By two doses":
                        Log.i("OBT", "start");
                        in = "OBT";
                        i = 0;
                        index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                        ArrayList<dispData> listOBT3 = new ArrayList<>();
                        List<CovidVaccineSweden.AgeGroupReport> listOBT2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        listOBT3.add(new dispData("Total",
                                String.valueOf(listOBT2.get(9).getDose1Pfizer() + listOBT2.get(9).getDose1Moderna() + listOBT2.get(9).getDose1AstraZeneca()),
                                String.valueOf(listOBT2.get(9).getDose2Pfizer() + listOBT2.get(9).getDose2Moderna() + listOBT2.get(9).getDose2AstraZeneca())));


                        CustomArrayAdapter adapterOBT = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listOBT3);
                        listView.setAdapter(adapterOBT);
                        Log.i("OBT", "end");
                        break;
                    case "By one dose, By age group":
                        Log.i("OABA", "start");
                        in = "OABA";
                        index = covidData.findSwedenCasesAndDeathsRegion(countyGroup2[0]);
                        List<CovidVaccineSweden.AgeGroupReport> listOABA = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        ArrayList<dispData> listOABA3 = new ArrayList<>();
                        length = ageGroup.length;

                        for (i = 0; i < 9; i++)
                        {
                            List<CovidVaccineSweden.AgeGroupReport> listOABA2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                            listOABA3.add(new dispData("Group: " + listOABA2.get(i).getGroup(),
                                    String.valueOf(listOABA2.get(i).getDose1Pfizer() + listOABA2.get(i).getDose1Moderna() + listOABA2.get(i).getDose1AstraZeneca()),
                                    String.valueOf(listOABA2.get(i).getDose2Pfizer() + listOABA2.get(i).getDose2Moderna() + listOABA2.get(i).getDose2AstraZeneca())));
                        }
                        CustomArrayAdapter adapterOABA = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listOABA3);
                        listView.setAdapter(adapterOABA);
                        Log.i("ABA", "end");
                        break;
                    case "By one dose, By county":
                        Log.i("AOBC", "start");
                        in = "AOBC";

                        ArrayList<dispData> listAOBC3 = new ArrayList<>();
                        length = countyGroup2.length;

                        for(i = 0 ; i < length; i++)
                        {
                            index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                            List<CovidVaccineSweden.AgeGroupReport> listAOBC2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                            listAOBC3.add(new dispData(countyGroup2[i], String.valueOf(listAOBC2.get(9).getDose1()), String.valueOf(listAOBC2.get(9).getDose2())));
                        }
                        CustomArrayAdapter adapterAOBC = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listAOBC3);
                        listView.setAdapter(adapterAOBC);
                        Log.i("AOBC", "end");
                        break;
                    case "By one dose, By product":
                        Log.i("OBP", "start");
                        in = "OBP";
                        i = 0;
                        index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                        ArrayList<dispData> listOBP3 = new ArrayList<>();
                        List<CovidVaccineSweden.AgeGroupReport> listOBP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        listOBP3.add(new dispData(vacineGroup[0],
                                String.valueOf(listOBP2.get(9).getDose1Pfizer()),
                                String.valueOf(listOBP2.get(9).getDose2Pfizer())));
                        listOBP3.add(new dispData(vacineGroup[1],
                                String.valueOf(listOBP2.get(9).getDose1Moderna()),
                                String.valueOf(listOBP2.get(9).getDose2Moderna())));
                        listOBP3.add(new dispData(vacineGroup[2],
                                String.valueOf(listOBP2.get(9).getDose1AstraZeneca()),
                                String.valueOf(listOBP2.get(9).getDose2AstraZeneca())));

                        CustomArrayAdapter adapterOBP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listOBP3);
                        listView.setAdapter(adapterOBP);
                        Log.i("OBP", "end");
                        break;
                    case "By one dose, By two doses, By age group":
                    case "By age group":
                        Log.i("ABA", "start");
                        in = "ABA";
                        index = covidData.findSwedenCasesAndDeathsRegion(countyGroup2[0]);
                        List<CovidVaccineSweden.AgeGroupReport> listABA = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        ArrayList<dispData> listABA3 = new ArrayList<>();
                        length = ageGroup.length;

                        for (i = 0; i < 9; i++)
                        {
                            List<CovidVaccineSweden.AgeGroupReport> listABA2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                            listABA3.add(new dispData("Group: " + listABA2.get(i).getGroup(),
                                    String.valueOf(listABA2.get(i).getDose1Pfizer() + listABA2.get(i).getDose1Moderna() + listABA2.get(i).getDose1AstraZeneca()),
                                    String.valueOf(listABA2.get(i).getDose2Pfizer() + listABA2.get(i).getDose2Moderna() + listABA2.get(i).getDose2AstraZeneca())));
                        }
                        CustomArrayAdapter adapterABA = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listABA3);
                        listView.setAdapter(adapterABA);
                        Log.i("ABA", "end");
                        break;
                    case "By one dose, By two doses, By county":
                    case "By county":

                        Log.i("ABC", "start");
                        in = "ABC";

                        ArrayList<dispData> listABC3 = new ArrayList<>();
                        length = countyGroup2.length;

                        for(i = 0 ; i < length; i++)
                        {
                            index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                            List<CovidVaccineSweden.AgeGroupReport> listABC2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                            listABC3.add(new dispData(countyGroup2[i], String.valueOf(listABC2.get(9).getDose1()), String.valueOf(listABC2.get(9).getDose2())));
                        }
                        CustomArrayAdapter adapterABC = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listABC3);
                        listView.setAdapter(adapterABC);
                        Log.i("ABC", "end");
                        break;
                    case "By one dose, By two doses, By product":
                    case "By product":
                        Log.i("BP", "start");
                        in = "BP";
                        i = 0;
                        index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                        ArrayList<dispData> listBP3 = new ArrayList<>();
                        List<CovidVaccineSweden.AgeGroupReport> listBP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        listBP3.add(new dispData(vacineGroup[0],
                                String.valueOf(listBP2.get(9).getDose1Pfizer()),
                                String.valueOf(listBP2.get(9).getDose2Pfizer())));
                        listBP3.add(new dispData(vacineGroup[1],
                                String.valueOf(listBP2.get(9).getDose1Moderna()),
                                String.valueOf(listBP2.get(9).getDose2Moderna())));
                        listBP3.add(new dispData(vacineGroup[2],
                                String.valueOf(listBP2.get(9).getDose1AstraZeneca()),
                                String.valueOf(listBP2.get(9).getDose2AstraZeneca())));

                        CustomArrayAdapter adapterBP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listBP3);
                        listView.setAdapter(adapterBP);
                        Log.i("BP", "end");
                        break;
                    case "By one dose, By age group, By county":
                        Log.i("OBABC", "start");
                        in = "OBABC";

                        ArrayList<dispData> listOBABC3 = new ArrayList<>();
                        length = countyGroup2.length;
                        for(m = 0; m < 9; m++) {
                            Log.i("OBABC", "M: " + m);

                            for (i = 0; i < length; i++) {
                                index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                                List<CovidVaccineSweden.AgeGroupReport> listOBABC2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();

                                listOBABC3.add(new dispData(countyGroup2[i] + "\nAge: " + listOBABC2.get(m).getGroup(),
                                        String.valueOf(listOBABC2.get(m).getDose1Pfizer() + listOBABC2.get(m).getDose1Moderna() + listOBABC2.get(m).getDose1AstraZeneca()),
                                        String.valueOf(listOBABC2.get(m).getDose2Pfizer() + listOBABC2.get(m).getDose2Moderna() + listOBABC2.get(m).getDose2AstraZeneca())));
                            }
                        }
                        CustomArrayAdapter adapterOBABC = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listOBABC3);
                        listView.setAdapter(adapterOBABC);
                        Log.i("OBABC", "end");
                        break;
                    case "By one dose, By age group, By product":
                        Log.i("OBABP", "start");
                        in = "OBABP";

                        index = covidData.findSwedenCasesAndDeathsRegion(countyGroup2[0]);
                        List<CovidVaccineSweden.AgeGroupReport> listOBABP = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        ArrayList<dispData> listOBABP3 = new ArrayList<>();
                        length = countyGroup2.length;
                        length2 = vacineGroup.length;
                        for(m = 0; m < 9; m++) {
                            for (k = 0; k < length2; k++) {
                                index = covidData.findSwedenVaccineRegion(countyGroup2[0]);
                                List<CovidVaccineSweden.AgeGroupReport> listOBABP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                                if (k == 0)
                                    listOBABP3.add(new dispData( "\nAge: " + listOBABP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                            String.valueOf(listOBABP2.get(m).getDose1Pfizer()),
                                            String.valueOf(listOBABP2.get(m).getDose2Pfizer())));
                                if (k == 1)
                                    listOBABP3.add(new dispData("\nAge: " + listOBABP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                            String.valueOf(listOBABP2.get(m).getDose1Moderna()),
                                            String.valueOf(listOBABP2.get(m).getDose2Moderna())));
                                if (k == 2)
                                    listOBABP3.add(new dispData( "\nAge: " + listOBABP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                            String.valueOf(listOBABP2.get(m).getDose1AstraZeneca()),
                                            String.valueOf(listOBABP2.get(m).getDose2AstraZeneca())));

                            }
                        }
                        CustomArrayAdapter adapterOBABP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listOBABP3);
                        listView.setAdapter(adapterOBABP);
                        Log.i("OBABP", "end");
                        break;
                    case "By one dose, By county, By product":
                        Log.i("OBCBP", "start");
                        in = "OBCBP";

                        index = covidData.findSwedenCasesAndDeathsRegion(countyGroup2[0]);
                        List<CovidVaccineSweden.AgeGroupReport> listOBCBP = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        ArrayList<dispData> listOBCBP3 = new ArrayList<>();
                        length = countyGroup2.length;
                        length2 = vacineGroup.length;
                        for(k = 0; k < length2; k++)
                        {
                            for (i = 0; i < length; i++)
                            {
                                index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                                List<CovidVaccineSweden.AgeGroupReport> listOBCBP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                                if(k == 0)
                                    listOBCBP3.add(new dispData(countyGroup2[i] + "\n" + vacineGroup[k],
                                            String.valueOf(listOBCBP2.get(9).getDose1Pfizer()),
                                            String.valueOf(listOBCBP2.get(9).getDose2Pfizer())));
                                if(k == 1)
                                    listOBCBP3.add(new dispData(countyGroup2[i] + "\n" + vacineGroup[k],
                                            String.valueOf(listOBCBP2.get(9).getDose1Moderna()),
                                            String.valueOf(listOBCBP2.get(9).getDose2Moderna())));
                                if(k == 2)
                                    listOBCBP3.add(new dispData(countyGroup2[i] + "\n" + vacineGroup[k],
                                            String.valueOf(listOBCBP2.get(9).getDose1AstraZeneca()),
                                            String.valueOf(listOBCBP2.get(9).getDose2AstraZeneca())));
                            }
                        }
                        CustomArrayAdapter adapterOBCBP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listOBCBP3);
                        listView.setAdapter(adapterOBCBP);
                        Log.i("OBCBP", "end");
                        break;
                    case "By one dose, By two doses, By age group, By county":
                    case "By age group, By county":
                        Log.i("BABC", "start");
                        in = "BABC";

                        ArrayList<dispData> listBABC3 = new ArrayList<>();
                        length = countyGroup2.length;
                        for(m = 0; m < 9; m++) {
                            Log.i("BABC", "M: " + m);

                            for (i = 0; i < length; i++) {
                                index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                                List<CovidVaccineSweden.AgeGroupReport> listBABC2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();

                                listBABC3.add(new dispData(countyGroup2[i] + "\nAge: " + listBABC2.get(m).getGroup(),
                                    String.valueOf(listBABC2.get(m).getDose1Pfizer() + listBABC2.get(m).getDose1Moderna() + listBABC2.get(m).getDose1AstraZeneca()),
                                    String.valueOf(listBABC2.get(m).getDose2Pfizer() + listBABC2.get(m).getDose2Moderna() + listBABC2.get(m).getDose2AstraZeneca())));
                            }
                        }
                        CustomArrayAdapter adapterBABC = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listBABC3);
                        listView.setAdapter(adapterBABC);
                        Log.i("BABC", "end");
                        break;
                    case "By one dose, By two doses, By age group, By product":
                    case "By age group, By product":
                        Log.i("BABP", "start");
                        in = "BABP";

                        index = covidData.findSwedenCasesAndDeathsRegion(countyGroup2[0]);
                        List<CovidVaccineSweden.AgeGroupReport> listBABP = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        ArrayList<dispData> listBABP3 = new ArrayList<>();
                        length = countyGroup2.length;
                        length2 = vacineGroup.length;
                        for(m = 0; m < 9; m++) {
                            for (k = 0; k < length2; k++) {
                                index = covidData.findSwedenVaccineRegion(countyGroup2[0]);
                                List<CovidVaccineSweden.AgeGroupReport> listBABP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                                if (k == 0)
                                    listBABP3.add(new dispData( "\nAge: " + listBABP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                            String.valueOf(listBABP2.get(m).getDose1Pfizer()),
                                            String.valueOf(listBABP2.get(m).getDose2Pfizer())));
                                if (k == 1)
                                    listBABP3.add(new dispData("\nAge: " + listBABP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                            String.valueOf(listBABP2.get(m).getDose1Moderna()),
                                            String.valueOf(listBABP2.get(m).getDose2Moderna())));
                                if (k == 2)
                                    listBABP3.add(new dispData( "\nAge: " + listBABP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                            String.valueOf(listBABP2.get(m).getDose1AstraZeneca()),
                                            String.valueOf(listBABP2.get(m).getDose2AstraZeneca())));

                            }
                        }
                        CustomArrayAdapter adapterBABP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listBABP3);
                        listView.setAdapter(adapterBABP);
                        Log.i("BABP", "end");
                        break;
                    case "By one dose, By age group, By county, By product":
                        Log.i("OBABCBP", "start");
                        in = "OBABCBP";

                        index = covidData.findSwedenCasesAndDeathsRegion(countyGroup2[0]);
                        List<CovidVaccineSweden.AgeGroupReport> listOBABCBP = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        ArrayList<dispData> listOBABCBP3 = new ArrayList<>();
                        length = countyGroup2.length;
                        length2 = vacineGroup.length;
                        for(m = 0; m < 9; m++) {
                            for (k = 0; k < length2; k++) {
                                for (i = 0; i < length; i++) {
                                    index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                                    List<CovidVaccineSweden.AgeGroupReport> listOBABCBP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                                    if (k == 0)
                                        listOBABCBP3.add(new dispData(countyGroup2[i] + "\nAge: " + listOBABCBP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                                String.valueOf(listOBABCBP2.get(m).getDose1Pfizer()),
                                                String.valueOf(listOBABCBP2.get(m).getDose2Pfizer())));
                                    if (k == 1)
                                        listOBABCBP3.add(new dispData(countyGroup2[i] + "\nAge: " + listOBABCBP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                                String.valueOf(listOBABCBP2.get(m).getDose1Moderna()),
                                                String.valueOf(listOBABCBP2.get(m).getDose2Moderna())));
                                    if (k == 2)
                                        listOBABCBP3.add(new dispData(countyGroup2[i] + "\nAge: " + listOBABCBP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                                String.valueOf(listOBABCBP2.get(m).getDose1AstraZeneca()),
                                                String.valueOf(listOBABCBP2.get(m).getDose2AstraZeneca())));
                                }
                            }
                        }
                        CustomArrayAdapter adapterOBABCBP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listOBABCBP3);
                        listView.setAdapter(adapterOBABCBP);
                        Log.i("OBABCBP", "end");
                        break;
                    case "By one dose, By two doses, By county, By product":
                    case "By county, By product":
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
                                    listBCBP3.add(new dispData(countyGroup2[i] + "\n" + vacineGroup[k],
                                            String.valueOf(listBCBP2.get(9).getDose1Pfizer()),
                                            String.valueOf(listBCBP2.get(9).getDose2Pfizer())));
                                if(k == 1)
                                    listBCBP3.add(new dispData(countyGroup2[i] + "\n" + vacineGroup[k],
                                            String.valueOf(listBCBP2.get(9).getDose1Moderna()),
                                            String.valueOf(listBCBP2.get(9).getDose2Moderna())));
                                if(k == 2)
                                    listBCBP3.add(new dispData(countyGroup2[i] + "\n" + vacineGroup[k],
                                            String.valueOf(listBCBP2.get(9).getDose1AstraZeneca()),
                                            String.valueOf(listBCBP2.get(9).getDose2AstraZeneca())));
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
                    case "By age group, By county, By product":
                        Log.i("BABCBP", "start");
                        in = "BABCBP";

                        index = covidData.findSwedenCasesAndDeathsRegion(countyGroup2[0]);
                        List<CovidVaccineSweden.AgeGroupReport> listBABCBP = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        ArrayList<dispData> listBABCBP3 = new ArrayList<>();
                        length = countyGroup2.length;
                        length2 = vacineGroup.length;
                        for(m = 0; m < 9; m++) {
                            for (k = 0; k < length2; k++) {
                                for (i = 0; i < length; i++) {
                                    index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                                    List<CovidVaccineSweden.AgeGroupReport> listBABCBP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                                    if (k == 0)
                                        listBABCBP3.add(new dispData(countyGroup2[i] + "\nAge: " + listBABCBP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                                String.valueOf(listBABCBP2.get(m).getDose1Pfizer()),
                                                String.valueOf(listBABCBP2.get(m).getDose2Pfizer())));
                                    if (k == 1)
                                        listBABCBP3.add(new dispData(countyGroup2[i] + "\nAge: " + listBABCBP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                                String.valueOf(listBABCBP2.get(m).getDose1Moderna()),
                                                String.valueOf(listBABCBP2.get(m).getDose2Moderna())));
                                    if (k == 2)
                                        listBABCBP3.add(new dispData(countyGroup2[i] + "\nAge: " + listBABCBP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                                String.valueOf(listBABCBP2.get(m).getDose1AstraZeneca()),
                                                String.valueOf(listBABCBP2.get(m).getDose2AstraZeneca())));
                                }
                            }
                        }
                        CustomArrayAdapter adapterBABCBP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listBABCBP3);
                        listView.setAdapter(adapterBABCBP);
                        Log.i("BABCBP", "end");
                        break;
                    case "By two doses":
                        Log.i("TB", "start");
                        in = "TB";
                        i = 0;
                        index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                        ArrayList<dispData> listTB3 = new ArrayList<>();
                        List<CovidVaccineSweden.AgeGroupReport> listTB2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        listTB3.add(new dispData("Total: ",
                                String.valueOf(listTB2.get(9).getDose1Pfizer() + listTB2.get(9).getDose1Moderna() + listTB2.get(9).getDose1AstraZeneca()),
                                String.valueOf(listTB2.get(9).getDose2Pfizer() + listTB2.get(9).getDose2Moderna() + listTB2.get(9).getDose2AstraZeneca())));


                        CustomArrayAdapter adapterTB = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listTB3);
                        listView.setAdapter(adapterTB);
                        Log.i("TB", "end");
                        break;
                    case "By two doses, By age group":
                        Log.i("TABA", "start");
                        in = "TABA";
                        index = covidData.findSwedenCasesAndDeathsRegion(countyGroup2[0]);
                        List<CovidVaccineSweden.AgeGroupReport> listTABA = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        ArrayList<dispData> listTABA3 = new ArrayList<>();
                        length = ageGroup.length;

                        for (i = 0; i < 9; i++)
                        {
                            List<CovidVaccineSweden.AgeGroupReport> listTABA2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                            listTABA3.add(new dispData("Group: " + listTABA2.get(i).getGroup(),
                                    String.valueOf(listTABA2.get(i).getDose1Pfizer() + listTABA2.get(i).getDose1Moderna() + listTABA2.get(i).getDose1AstraZeneca()),
                                    String.valueOf(listTABA2.get(i).getDose2Pfizer() + listTABA2.get(i).getDose2Moderna() + listTABA2.get(i).getDose2AstraZeneca())));
                        }
                        CustomArrayAdapter adapterTABA = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listTABA3);
                        listView.setAdapter(adapterTABA);
                        Log.i("TABA", "end");
                        break;
                    case "By two doses, By county":
                        Log.i("TABC", "start");
                        in = "TABC";

                        ArrayList<dispData> listTABC3 = new ArrayList<>();
                        length = countyGroup2.length;

                        for(i = 0 ; i < length; i++)
                        {
                            index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                            List<CovidVaccineSweden.AgeGroupReport> listTABC2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                            listTABC3.add(new dispData(countyGroup2[i], String.valueOf(listTABC2.get(9).getDose1()), String.valueOf(listTABC2.get(9).getDose2())));
                        }
                        CustomArrayAdapter adapterTABC = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listTABC3);
                        listView.setAdapter(adapterTABC);
                        Log.i("TABC", "end");
                        break;
                    case "By two doses, By product":
                        Log.i("TBP", "start");
                        in = "TBP";
                        i = 0;
                        index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                        ArrayList<dispData> listTBP3 = new ArrayList<>();
                        List<CovidVaccineSweden.AgeGroupReport> listTBP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        listTBP3.add(new dispData(vacineGroup[0],
                                String.valueOf(listTBP2.get(9).getDose1Pfizer()),
                                String.valueOf(listTBP2.get(9).getDose2Pfizer())));
                        listTBP3.add(new dispData(vacineGroup[1],
                                String.valueOf(listTBP2.get(9).getDose1Moderna()),
                                String.valueOf(listTBP2.get(9).getDose2Moderna())));
                        listTBP3.add(new dispData(vacineGroup[2],
                                String.valueOf(listTBP2.get(9).getDose1AstraZeneca()),
                                String.valueOf(listTBP2.get(9).getDose2AstraZeneca())));

                        CustomArrayAdapter adapterTBP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listTBP3);
                        listView.setAdapter(adapterTBP);
                        Log.i("TBP", "end");
                        break;
                    case "By two doses, By age group, By county":
                        Log.i("TBABC", "start");
                        in = "TBABC";

                        ArrayList<dispData> listTBABC3 = new ArrayList<>();
                        length = countyGroup2.length;
                        for(m = 0; m < 9; m++) {
                            Log.i("TBABC", "M: " + m);

                            for (i = 0; i < length; i++) {
                                index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                                List<CovidVaccineSweden.AgeGroupReport> listTBABC2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();

                                listTBABC3.add(new dispData(countyGroup2[i] + "\nAge: " + listTBABC2.get(m).getGroup(),
                                        String.valueOf(listTBABC2.get(m).getDose1Pfizer() + listTBABC2.get(m).getDose1Moderna() + listTBABC2.get(m).getDose1AstraZeneca()),
                                        String.valueOf(listTBABC2.get(m).getDose2Pfizer() + listTBABC2.get(m).getDose2Moderna() + listTBABC2.get(m).getDose2AstraZeneca())));
                            }
                        }
                        CustomArrayAdapter adapterTBABC = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listTBABC3);
                        listView.setAdapter(adapterTBABC);
                        Log.i("TBABC", "end");
                        break;
                    case "By two doses, By age group, By product":
                        Log.i("TBABP", "start");
                        in = "TBABP";

                        index = covidData.findSwedenCasesAndDeathsRegion(countyGroup2[0]);
                        List<CovidVaccineSweden.AgeGroupReport> listTBABP = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        ArrayList<dispData> listTBABP3 = new ArrayList<>();
                        length = countyGroup2.length;
                        length2 = vacineGroup.length;
                        for(m = 0; m < 9; m++) {
                            for (k = 0; k < length2; k++) {
                                index = covidData.findSwedenVaccineRegion(countyGroup2[0]);
                                List<CovidVaccineSweden.AgeGroupReport> listTBABP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                                if (k == 0)
                                    listTBABP3.add(new dispData( "\nAge: " + listTBABP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                            String.valueOf(listTBABP2.get(m).getDose1Pfizer()),
                                            String.valueOf(listTBABP2.get(m).getDose2Pfizer())));
                                if (k == 1)
                                    listTBABP3.add(new dispData("\nAge: " + listTBABP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                            String.valueOf(listTBABP2.get(m).getDose1Moderna()),
                                            String.valueOf(listTBABP2.get(m).getDose2Moderna())));
                                if (k == 2)
                                    listTBABP3.add(new dispData( "\nAge: " + listTBABP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                            String.valueOf(listTBABP2.get(m).getDose1AstraZeneca()),
                                            String.valueOf(listTBABP2.get(m).getDose2AstraZeneca())));

                            }
                        }
                        CustomArrayAdapter adapterTBABP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listTBABP3);
                        listView.setAdapter(adapterTBABP);
                        Log.i("TBABP", "end");
                        break;
                    case "By two doses, By county, By product":
                        Log.i("TBCBP", "start");
                        in = "TBCBP";

                        index = covidData.findSwedenCasesAndDeathsRegion(countyGroup2[0]);
                        List<CovidVaccineSweden.AgeGroupReport> listTBCBP = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        ArrayList<dispData> listTBCBP3 = new ArrayList<>();
                        length = countyGroup2.length;
                        length2 = vacineGroup.length;
                        for(k = 0; k < length2; k++)
                        {
                            for (i = 0; i < length; i++)
                            {
                                index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                                List<CovidVaccineSweden.AgeGroupReport> listTBCBP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                                if(k == 0)
                                    listTBCBP3.add(new dispData(countyGroup2[i] + "\n" + vacineGroup[k],
                                            String.valueOf(listTBCBP2.get(9).getDose1Pfizer()),
                                            String.valueOf(listTBCBP2.get(9).getDose2Pfizer())));
                                if(k == 1)
                                    listTBCBP3.add(new dispData(countyGroup2[i] + "\n" + vacineGroup[k],
                                            String.valueOf(listTBCBP2.get(9).getDose1Moderna()),
                                            String.valueOf(listTBCBP2.get(9).getDose2Moderna())));
                                if(k == 2)
                                    listTBCBP3.add(new dispData(countyGroup2[i] + "\n" + vacineGroup[k],
                                            String.valueOf(listTBCBP2.get(9).getDose1AstraZeneca()),
                                            String.valueOf(listTBCBP2.get(9).getDose2AstraZeneca())));
                            }
                        }
                        CustomArrayAdapter adapterTBCBP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listTBCBP3);
                        listView.setAdapter(adapterTBCBP);
                        Log.i("TBCBP", "end");
                        break;
                    case "By two doses, By age group, By county, By product":
                        Log.i("TBABCBP", "start");
                        in = "TBABCBP";

                        index = covidData.findSwedenCasesAndDeathsRegion(countyGroup2[0]);
                        List<CovidVaccineSweden.AgeGroupReport> listTBABCBP = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                        ArrayList<dispData> listTBABCBP3 = new ArrayList<>();
                        length = countyGroup2.length;
                        length2 = vacineGroup.length;
                        for(m = 0; m < 9; m++) {
                            for (k = 0; k < length2; k++) {
                                for (i = 0; i < length; i++) {
                                    index = covidData.findSwedenVaccineRegion(countyGroup2[i]);
                                    List<CovidVaccineSweden.AgeGroupReport> listTBABCBP2 = covidData.getSwedenVaccine().get(index).getAgeGroupReports();
                                    if (k == 0)
                                        listTBABCBP3.add(new dispData(countyGroup2[i] + "\nAge: " + listTBABCBP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                                String.valueOf(listTBABCBP2.get(m).getDose1Pfizer()),
                                                String.valueOf(listTBABCBP2.get(m).getDose2Pfizer())));
                                    if (k == 1)
                                        listTBABCBP3.add(new dispData(countyGroup2[i] + "\nAge: " + listTBABCBP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                                String.valueOf(listTBABCBP2.get(m).getDose1Moderna()),
                                                String.valueOf(listTBABCBP2.get(m).getDose2Moderna())));
                                    if (k == 2)
                                        listTBABCBP3.add(new dispData(countyGroup2[i] + "\nAge: " + listTBABCBP2.get(m).getGroup() + "\n" + vacineGroup[k],
                                                String.valueOf(listTBABCBP2.get(m).getDose1AstraZeneca()),
                                                String.valueOf(listTBABCBP2.get(m).getDose2AstraZeneca())));
                                }
                            }
                        }
                        CustomArrayAdapter adapterTBABCBP = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view3,
                                listTBABCBP3);
                        listView.setAdapter(adapterTBABCBP);
                        Log.i("TBABCBP", "end");
                        break;
                    /*
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
                    */
                }
                break;

            case "Cumulative uptake (%)":
                switch(chosenFilters){
                    case "By week":
                        Log.i("BW", "start");
                        in = "BW";

                        List<CovidVaccineSweden.WeeklyReport> listBW;
                        ArrayList<dispData> listBW2 = new ArrayList<>();
                        index2 = covidData.findSwedenVaccineRegion(countyGroup3[0]);
                        listBW = covidData.getSwedenVaccine().get(index2).getWeeklyReports();
                        w = 52;
                        y= 2020;
                        sum = 0;
                        sumTot = 0;
                        while(covidData.getSwedenVaccine().get(0).weeklyReportsHasWeek(w, y))
                        {
                            index = covidData.getSwedenVaccine().get(0).weeklyReportsFindWeek(w, y);

                            listBW2.add(new dispData( "Year: " + y + "\nWeek: " + w,
                                    String.format("%.2f", listBW.get(index).getDose1Quota()*100) + "%",
                                    String.format("%.2f", listBW.get(index).getDose2Quota()*100) + "%"
                            ));
                            if(w == 53)
                            {
                                w = 1;
                                y = y + 1;
                            }
                            else { w = w + 1; }
                        }
                        CustomArrayAdapter adapterBW = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listBW2);
                        listView.setAdapter(adapterBW);
                        Log.i("BW", "end");
                        break;
                    case "By month":
                        Log.i("BM", "start");
                        in = "BM";

                        List<CovidVaccineSweden.WeeklyReport> listBM;
                        ArrayList<dispData> listBM2 = new ArrayList<>();
                        index2 = covidData.findSwedenVaccineRegion(countyGroup3[0]);
                        listBM = covidData.getSwedenVaccine().get(index2).getWeeklyReports();
                        w = 52;
                        y= 2020;
                        m = 12;
                        while(covidData.getSwedenVaccine().get(0).weeklyReportsHasWeek(w, y))
                        {
                            index = covidData.getSwedenVaccine().get(0).weeklyReportsFindWeek(w, y);


                            listBM2.add(new dispData( "Year: " + y + "\nMonth: " + m,
                                    String.format("%.2f", listBM.get(index).getDose1Quota()*100) + "%",
                                    String.format("%.2f", listBM.get(index).getDose2Quota()*100) + "%"
                            ));
                            if(w == 53)
                            {
                                m = 1;
                                w = 1;
                                y = y + 1;
                            }
                            else
                            {
                                if(w % 4 == 0)
                                    if(m != 12)
                                        m = w/4 +1;
                                w = w + 1;
                            }
                        }
                        CustomArrayAdapter adapterBM = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listBM2);
                        listView.setAdapter(adapterBM);
                        Log.i("BM", "end");
                        break;
                    case "By week, By month":
                        Log.i("BWBM", "start");
                        in = "BWBM";

                        List<CovidVaccineSweden.WeeklyReport> listBWBM;
                        ArrayList<dispData> listBWBM2 = new ArrayList<>();
                        index2 = covidData.findSwedenVaccineRegion(countyGroup3[0]);
                        listBWBM = covidData.getSwedenVaccine().get(index2).getWeeklyReports();
                        w = 52;
                        y= 2020;
                        m = 12;
                        while(covidData.getSwedenVaccine().get(0).weeklyReportsHasWeek(w, y))
                        {
                            index = covidData.getSwedenVaccine().get(0).weeklyReportsFindWeek(w, y);


                            listBWBM2.add(new dispData( "Year: " + y + "\nMonth: " + m + "\nWeek: " + w,
                                    String.format("%.2f", listBWBM.get(index).getDose1Quota()*100) + "%",
                                    String.format("%.2f", listBWBM.get(index).getDose2Quota()*100) + "%"
                            ));
                            if(w == 53)
                            {
                                m = 1;
                                w = 1;
                                y = y + 1;
                            }
                            else
                            {
                                if(w % 4 == 0)
                                    if(m != 12)
                                        m = w/4 +1;
                                w = w + 1;
                            }
                        }
                        CustomArrayAdapter adapterBWBM = new CustomArrayAdapter(
                                getContext(),
                                R.layout.custom_list_view,
                                listBWBM2);
                        listView.setAdapter(adapterBWBM);
                        Log.i("BWBM", "end");
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
                            listBC.add(new dispData(countyGroup[i], String.valueOf(listBC2.getCases()), String.valueOf(listBC2.getDeaths())));
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
                            listBAG.add(new dispData(ageGroup[i], String.valueOf(listBAG2.getCases()), String.valueOf(listBAG2.getDeaths())));
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
                                listBCBAG.add(new dispData(countyGroup[k] + ": " + ageGroup[i], String.valueOf(listBCBAG2.getCases()), String.valueOf(listBCBAG2.getDeaths())));
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
                filterIcon.setVisibility(View.INVISIBLE);
                filter.setVisibility(View.VISIBLE);
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
        filterIcon = root.findViewById(R.id.filter_icon);
        categoryIcon = root.findViewById(R.id.category_icon);

        filter = root.findViewById(R.id.filter_text);
        final Spinner dashboardOptions = root.findViewById(R.id.dashboard_options);
        ArrayAdapter<CharSequence> optionsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.dashboard_stats, android.R.layout.simple_spinner_item);
        optionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dashboardOptions.setAdapter(optionsAdapter);

/*
        Log.i("Graf", "start");

        String[] countyGroup4 = { "Sverige", "Blekinge", "Dalarna", "Gotland",
                "Gävleborg", "Halland", "Jämtland", "Jönköping", "Kalmar", "Kronoberg",
                "Norrbotten", "Skåne", "Stockholm", "Södermanland", "Uppsala", "Värmland",
                "Västerbotten", "Västernorrland", "Västmanland", "Västra Götaland", "Örebro",
                "Östergötland"};

        CovidData covidData = MainActivity.covidData;
        LineGraphSeries<DataPoint> series = null;
        LineGraphSeries<DataPoint> series2 = null;

        int sum, sumTot, w, y, index, index2, length, i;

        GraphView graph = (GraphView) root.findViewById(R.id.graph);
        graph.setVisibility(View.VISIBLE);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);



        List<CovidVaccineSweden.WeeklyReport> listBW;
        ArrayList<dispData> listBW2 = new ArrayList<>();
        index2 = covidData.findSwedenVaccineRegion(countyGroup4[0]);
        listBW = covidData.getSwedenVaccine().get(index2).getWeeklyReports();
        w = 52;
        y= 2020;
        sum = 0;
        sumTot = 0;
        i = 0;
        while(covidData.getSwedenVaccine().get(0).weeklyReportsHasWeek(w, y))
            i = i +1;
        length = i;
        w = 52;
        y= 2020;
        i = 0;
        while(covidData.getSwedenVaccine().get(0).weeklyReportsHasWeek(w, y))
        {

            index = covidData.getSwedenVaccine().get(0).weeklyReportsFindWeek(w, y);

            series.appendData(new DataPoint(i, listBW.get(index).getDose1Quota()*100), true, length*2);
            series2.appendData(new DataPoint(i, listBW.get(index).getDose2Quota()*100), true, length*2);


            i ++;
            if(w == 53)
            {
                w = 1;
                y = y + 1;
            }
            else { w = w + 1; }
        }



        graph.addSeries(series);
        series.setColor(Color.BLUE);
        Log.i("Graf", "end");
*/

        dashboardOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenStat = adapterView.getItemAtPosition(i).toString();
                switch(chosenStat){
                    case "Total doses distributed":
                        //graph.setVisibility(View.INVISIBLE);

                        filterArray = getResources().getStringArray(R.array.filter_total_dist);
                        selectedFilters = new boolean[filterArray.length];
                        break;
                    case "Total doses administered":
                        //graph.setVisibility(View.INVISIBLE);

                        filterArray = getResources().getStringArray(R.array.filter_total_admin);
                        selectedFilters = new boolean[filterArray.length];
                        break;
                    case "Cumulative uptake (%)":
                        //graph.setVisibility(View.INVISIBLE);

                        filterArray = getResources().getStringArray(R.array.filter_cumulative_uptake);
                        selectedFilters = new boolean[filterArray.length];
                        break;
                    case "Total cases and deaths":
                        //graph.setVisibility(View.INVISIBLE);

                        filterArray = getResources().getStringArray(R.array.filter_cases_deaths);
                        selectedFilters = new boolean[filterArray.length];
                        break;
                    case "":
                        filter.setVisibility(View.INVISIBLE);
                        filterIcon.setVisibility(View.VISIBLE);
                        categoryIcon.setVisibility(View.VISIBLE);
                        break;
                }
                if(!chosenStat.equals("")){
                    categoryIcon.setVisibility(View.INVISIBLE);
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