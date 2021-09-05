package com.example.test1;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout a=findViewById(R.id.laymain);
        LinearLayout b=findViewById(R.id.laymenu1);
        LinearLayout c=findViewById(R.id.laymenu2);

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
                a.setVisibility(View.GONE);
                b.setVisibility(View.VISIBLE);
            }
        });


        Button menu2= findViewById(R.id.menu2);
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.setVisibility(View.VISIBLE);
                b.setVisibility(View.GONE);
                c.setVisibility(View.VISIBLE);
                a.setVisibility(View.GONE);


            }
        });

        Button back1= findViewById(R.id.back1);
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                b.setVisibility(View.GONE);
                a.setVisibility(View.VISIBLE);
            }
        });

        Button back2= findViewById(R.id.back2);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c.setVisibility(View.GONE);
                a.setVisibility(View.VISIBLE);
            }
        });
    }
}
