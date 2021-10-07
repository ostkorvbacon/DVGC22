package com.example.test3.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.test3.DatabaseHandler.User;
import com.example.test3.R;
import com.example.test3.VaccinePassport.CameraActivity;
import com.example.test3.VaccinePassport.VaccinePassport;
import com.example.test3.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private final int PERMISSION_REQUEST_CAMERA = 1;
    private ActivityResultLauncher<String> mPermissionResult;

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

        // ask for camera permission
        ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result) {
                            Intent cameraIntent = new Intent(binding.getRoot().getContext(), CameraActivity.class);
                            cameraIntent.putExtra("ParentActivity", "mainActivity");
                            startActivity(cameraIntent);
                        } else {
                            Toast.makeText(binding.getRoot().getContext(), getString(R.string.Camera_permission_denied), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //QR scanner button listener
        Button scanPassport = root.findViewById(R.id.button_qr_scanner_home);
        scanPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPermissionResult.launch(Manifest.permission.CAMERA);
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