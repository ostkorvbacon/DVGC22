package com.example.test3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;

import java.util.ArrayList;

public class QuestionnaireActivity extends AppCompatActivity {
    private boolean[] answers;
    private DatabaseHandler database;
    public AlertDialog popUpDialog(String title, String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext());
        dialogBuilder.setTitle(title);
        dialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return dialogBuilder.create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        Intent getUser = this.getIntent();
        User user = (User)getUser.getSerializableExtra("loggedInUser");
        Button submitButton = findViewById(R.id.submit_button);
        //yesBoxes
        CheckBox q0 = findViewById(R.id.question0);
        CheckBox q1 = findViewById(R.id.question1);
        CheckBox q2 = findViewById(R.id.question2);
        CheckBox q3 = findViewById(R.id.question3);
        CheckBox q4 = findViewById(R.id.question4);
        //noBoxes
        CheckBox q00 = findViewById(R.id.question00);
        CheckBox q10 = findViewById(R.id.question10);
        CheckBox q20 = findViewById(R.id.question20);
        CheckBox q30 = findViewById(R.id.question30);
        CheckBox q40 = findViewById(R.id.question40);

        //ArrayList<CheckBox> noBox = new <CheckBox>
        CheckBox yesBox[] = {q0, q1, q2, q3, q4};
        CheckBox noBox[] = {q00, q10, q20, q30, q40};
        answers = new boolean[5];

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < 5; i++) {
                    if (yesBox[i].isChecked()) {
                        answers[i] = true;
                    } else if (noBox[i].isChecked()) {
                        answers[i] = false;
                    } else {
                        AlertDialog error = popUpDialog("Error",
                                "You must answer all questions before booking an appointment.");
                        error.show();
                        return;
                    }
                }
                //database.newQuestionnaire(user.getUsername(), answers);
                //Intent goToBookings = new Intent(this, BookingsPage);
                //startActivity(goToBookings);

            }
        });


    }
}