package com.example.test3.ui.AdminFeatures;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.MainActivity;
import com.example.test3.MainMenuActivity;
import com.example.test3.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminScheduleFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private DatabaseHandler database = new DatabaseHandler("http://83.254.68.246:3003/");
    private TextView current_age_group;
    private Button apply_changes;
    public AdminScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminScheduleFragment newInstance(String param1, String param2) {
        AdminScheduleFragment fragment = new AdminScheduleFragment();
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
        View root =  inflater.inflate(R.layout.fragment_admin_schedule, container, false);
        current_age_group = root.findViewById(R.id.current_age_group);
        apply_changes = root.findViewById(R.id.apply_admin_settings);

        String cur_age_group = Integer.toString(database.getMinimumAgeForVaccination());
        current_age_group.setText(current_age_group.getText() + cur_age_group);

        apply_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int new_age_group = Integer.valueOf(current_age_group.getText().toString());
                database.setMinimumAgeForVaccination(new_age_group);

                //closes keyboard and focus on edittext
                if (getActivity() == null) return;
                if (getActivity().getCurrentFocus() == null) return;
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                current_age_group.clearFocus();
            }
        });

        return root;
    }
}