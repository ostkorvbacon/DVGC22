package com.example.test3.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.test3.DatabaseHandler.User;
import com.example.test3.R;
import com.example.test3.VaccinePassport.VaccinePassport;
import com.example.test3.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Intent intent = this.getActivity().getIntent();
        User loggedInUser = (User)intent.getSerializableExtra("LoggedInUser");

        //do QR
        ImageView imageView = root.findViewById(R.id.imageView);
        TextView txtView = root.findViewById(R.id.textView15);
        VaccinePassport passport = new VaccinePassport(loggedInUser.getUsername(), imageView, txtView);
        passport.getQRCode();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}