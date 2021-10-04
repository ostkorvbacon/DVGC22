package com.example.test3.ui.gallery;

import android.content.Context;
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

public class CustomArrayAdapter extends ArrayAdapter<CovidCasesSweden.AgeGroupReport> {
    private Context mContext;
    int mResource;


    public CustomArrayAdapter(Context context, int resource, List<CovidCasesSweden.AgeGroupReport> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String group = getItem(position).getAgeGroup();
        int cases = getItem(position).getCases();
        int deaths = getItem(position).getDeaths();

        CovidCasesSweden.AgeGroupReport dispData = new CovidCasesSweden.AgeGroupReport(group, cases, deaths);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvGroup = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvCases = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvDeaths = (TextView) convertView.findViewById(R.id.textView3);

        tvGroup.setText(group);
        tvCases.setText(cases);
        tvDeaths.setText(deaths);

        return convertView;

    }
}
