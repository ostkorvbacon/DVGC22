package com.example.test3.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;
import com.example.test3.R;
import com.example.test3.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private DatabaseHandler database = new DatabaseHandler("http://83.254.68.246:3003/");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        Intent intent = this.getActivity().getIntent();
        User user = (User)intent.getSerializableExtra("loggedInUser");

        EditText email = root.findViewById(R.id.profile_email);
        EditText name = root.findViewById(R.id.editTextTextPersonName3);
        EditText dob = root.findViewById(R.id.profile_dob);
        EditText phone = root.findViewById(R.id.profile_phone);
        EditText city = root.findViewById(R.id.profile_city);
        EditText addr = root.findViewById(R.id.profile_addr);

        String userName = user.getUsername().toString();
        email.setText(userName);
        name.setText(user.getName());
        dob.setText(user.getDateOfBirth().toString());
        phone.setText(user.getPhoneNr());
        city.setText(user.getCity());
        addr.setText(user.getAddress());

        return root;
    }
}