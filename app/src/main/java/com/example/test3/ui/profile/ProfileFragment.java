package com.example.test3.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;
import com.example.test3.R;


public class ProfileFragment extends Fragment {
    private DatabaseHandler database = new DatabaseHandler("http://83.254.68.246:3003/");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent intent = new Intent();
        User user = (User)intent.getSerializableExtra("LoggedInUser");

        Button editDetails = this.getActivity().findViewById(R.id.profile_ok_button);
        EditText email = this.getActivity().findViewById(R.id.profile_email);
        EditText pass = this.getActivity().findViewById(R.id.editTextTextPassword2);
        EditText name = this.getActivity().findViewById(R.id.editTextTextPersonName3);
        EditText gender = this.getActivity().findViewById(R.id.profile_gender);
        EditText dob = this.getActivity().findViewById(R.id.profile_dob);
        EditText phone = this.getActivity().findViewById(R.id.profile_phone);
        EditText city = this.getActivity().findViewById(R.id.profile_city);
        EditText addr = this.getActivity().findViewById(R.id.profile_addr);

        email.setText(user.getUsername());
        //no getpass method
        name.setText(user.getName());
        //no gender attr for user
        dob.setText(user.getDateOfBirth());
        phone.setText(user.getPhoneNr());
        city.setText(user.getCity());
        addr.setText(user.getAddress());

        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //commented out as deleting then adding newUser -> lost user vacc info
                /*no editprofile api method
                database.deleteUser(user.getUsername());
                database.newUser(email.getText().toString(),
                        pass.getText().toString(),
                        name.getText().toString(),
                        phone.getText().toString(),
                        dob.getText().toString(),
                        city.getText().toString(),
                        addr.getText().toString(),
                        user.getRole());

                 */
            }
        });

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}