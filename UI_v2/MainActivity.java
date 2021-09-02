package com.example.test1;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sub1= findViewById(R.id.subid1);
        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView a = findViewById(R.id.texttest);
                a.setText("Hej igen!!!!!!");

            }
        });

        Button menu= findViewById(R.id.menu1);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.menu);

            }
        });

      //  Button b= findViewById(R.id.back);
       // b.setOnClickListener(new View.OnClickListener() {
         //   @Override
           // public void onClick(View view) {
             //   setContentView(R.layout.menu2);

            //}
        //});

        Button menu2= findViewById(R.id.menu2);
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.menu2);

            }
        });


    }
}