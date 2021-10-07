package com.example.test3.ui.gallery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.test3.DataExtraction.CovidCasesSweden;
import com.example.test3.R;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<dispData> {
    private Context mContext;
    int mResource;



    public CustomArrayAdapter(Context context, int resource, List<dispData> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String group = getItem(position).getGroup();
        String cases = getItem(position).getCases();
        String deaths = getItem(position).getDeaths();

        dispData dispData = new dispData(group, cases, deaths);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvGroup = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvCases = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvDeaths = (TextView) convertView.findViewById(R.id.textView3);
        String in2 = GalleryFragment.in;
        if(in2 == "BC" || in2 == "BAG" || in2 == "BCBAG")
        {
            tvGroup.setText(group + ":");
            tvCases.setText("Cases: " + cases);
            tvDeaths.setText("Deaths: " + deaths);
        }
        else if(in2 == "ABC" || in2 == "BP" || in2 == "BCBP")
        {
            tvGroup.setText(group + ":");
            tvCases.setText("First Dose: " + cases);
            tvDeaths.setText("Second Dose: " + deaths);
        }
        else if(in2 == "DBC")
        {
            tvGroup.setText(group + "");
            tvCases.setText("" + cases);
            tvDeaths.setText("Delivered: " + deaths);
        }
        else if(in2 == "DBP" || in2 == "DBCBP")
        {
            tvGroup.setText(group);
            tvCases.setText(cases);
            tvDeaths.setText(deaths);

        }

            return convertView;

    }
}