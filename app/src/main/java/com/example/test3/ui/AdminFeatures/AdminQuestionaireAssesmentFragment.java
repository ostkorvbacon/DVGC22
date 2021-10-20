package com.example.test3.ui.AdminFeatures;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;
import com.example.test3.R;
import com.example.test3.databinding.FragmentAdminAppointmentsBinding;
import com.example.test3.databinding.FragmentAdminQuestionaireAssesmentBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminQuestionaireAssesmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminQuestionaireAssesmentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnBackPressedCallback callback;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentAdminQuestionaireAssesmentBinding binding;
    private AdminAppointmentsViewModel viewModel;
    private DatabaseHandler handler;

    private TextView text;
    private CheckBox answer1Yes;
    private CheckBox answer1No;
    private CheckBox answer2Yes;
    private CheckBox answer2No;
    private CheckBox answer3Yes;
    private CheckBox answer3No;
    private CheckBox answer4Yes;
    private CheckBox answer4No;
    private CheckBox answer5Yes;
    private CheckBox answer5No;
    private Button acceptButton;
    private Button denyButton;


    public AdminQuestionaireAssesmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminQuestionaireAssesmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminQuestionaireAssesmentFragment newInstance(String param1, String param2) {
        AdminQuestionaireAssesmentFragment fragment = new AdminQuestionaireAssesmentFragment();
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
        handler = new DatabaseHandler("http://83.254.68.246:3003/");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminQuestionaireAssesmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(requireActivity()).get(AdminAppointmentsViewModel.class);
        User user = viewModel.getCurrentUser();

        answer1Yes = view.findViewById(R.id.answer0);
        answer1No = view.findViewById(R.id.answer00);
        answer2Yes = view.findViewById(R.id.answer1);
        answer2No = view.findViewById(R.id.answer10);
        answer3Yes = view.findViewById(R.id.answer2);
        answer3No = view.findViewById(R.id.answer20);
        answer4Yes = view.findViewById(R.id.answer3);
        answer4No = view.findViewById(R.id.answer30);
        answer5Yes = view.findViewById(R.id.answer4);
        answer5No = view.findViewById(R.id.answer40);
        acceptButton = view.findViewById(R.id.accept_button);
        denyButton = view.findViewById(R.id.deny_button);

        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //AdminAppointmentsFragment fragment = new AdminAppointmentsFragment().newInstance(viewModel.getFilterPos(),viewModel.getUserPos(), true);
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fm.findFragmentByTag("questionaire_admin"));
                ft.show(fm.getPrimaryNavigationFragment());
                ft.addToBackStack(this.getClass().getName());
                ft.commit();
                callback.remove();
            }

        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this. callback);

        boolean[] answers;
        Log.i("User",user.getUsername());
        answers = handler.getQuestionnaire(user.getUsername()).getQuestionAnswers();

        answer1Yes.setClickable(true);
        answer1No.setClickable(true);
        answer2Yes.setClickable(true);
        answer2No.setClickable(true);
        answer3Yes.setClickable(true);
        answer3No.setClickable(true);
        answer4Yes.setClickable(true);
        answer4No.setClickable(true);
        answer5Yes.setClickable(true);
        answer5No.setClickable(true);

        answer1Yes.setChecked(answers[0]);
        answer1No.setChecked(!answers[0]);
        answer2Yes.setChecked(answers[1]);
        answer2No.setChecked(!answers[1]);
        answer3Yes.setChecked(answers[2]);
        answer3No.setChecked(!answers[2]);
        answer4Yes.setChecked(answers[3]);
        answer4No.setChecked(!answers[3]);
        answer5Yes.setChecked(answers[4]);
        answer5No.setChecked(!answers[4]);

        answer1Yes.setClickable(false);
        answer1No.setClickable(false);
        answer2Yes.setClickable(false);
        answer2No.setClickable(false);
        answer3Yes.setClickable(false);
        answer3No.setClickable(false);
        answer4Yes.setClickable(false);
        answer4No.setClickable(false);
        answer5Yes.setClickable(false);
        answer5No.setClickable(false);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.updateQuestionnaire(user.getUsername(),true);
                getActivity().onBackPressed();
            }
        });

        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.updateQuestionnaire(user.getUsername(),false);
                getActivity().onBackPressed();
            }
        });


        return view;
    }

    @Override
    public void onPause() {
        callback.remove();
        super.onPause();
    }

    @Override
    public void onResume() {
        requireActivity().getOnBackPressedDispatcher().addCallback(this. callback);
        super.onResume();
    }
}