package com.example.test3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;

import java.util.ArrayList;

public class QuestionnaireActivity extends AppCompatActivity {
    private DatabaseHandler database = new DatabaseHandler("http://83.254.68.246:3003/");
    private boolean[] answers;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        Intent getUser = this.getIntent();
        user = (User)getUser.getSerializableExtra("loggedInUser");

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
                    if (yesBox[i].isChecked() && !noBox[i].isChecked()) {
                        answers[i] = true;
                    } else if (noBox[i].isChecked() && !yesBox[i].isChecked()) {
                        answers[i] = false;
                    } else if(noBox[i].isChecked() && yesBox[i].isChecked()) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                            "You can only answer each question with a yes or no.",
                                Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }else{
                        Toast.makeText(getApplicationContext(),
                            "You must answer all questions before booking an appointment.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                String username = user.getUsername();
                if(database.questionnaireExists(username)) {
                    database.deleteQuestionnaire(username);
                }
                database.newQuestionnaire(username, answers);
                Intent goToBookings = new Intent(getApplicationContext(), BookingsActivity.class);
                startActivity(goToBookings);

            }
        });
    }
    @Override
    public Intent getSupportParentActivityIntent() {
        return getParentActivityIntentImpl();
    }

    @Override
    public Intent getParentActivityIntent() {
        return getParentActivityIntentImpl();
    }

    private Intent getParentActivityIntentImpl() {
        Intent i = null;
        Intent intent = this.getIntent();
        i = new Intent(this, MainMenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return i;
    }

}