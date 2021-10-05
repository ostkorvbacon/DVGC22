package com.example.test3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;


import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;
import com.example.test3.DatabaseHandler.Vaccination;
import com.example.test3.ui.profile.ProfileFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test3.databinding.ActivityMainMenuBinding;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //changed
        Toolbar tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_admin_appointments, R.id.nav_admin_schedule, R.id.nav_admin_stats)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //Hide/Show admin part side menu
        Intent intent = this.getIntent();
        User loggedInUser = (User)intent.getSerializableExtra("loggedInUser");

        if(loggedInUser.getRole().equals("Doctor")){
            Menu navMenu = navigationView.getMenu();
            navMenu.findItem(R.id.admin_tools).setVisible(true);
        }
        else{
            Menu navMenu = navigationView.getMenu();
            navMenu.findItem(R.id.admin_tools).setVisible(false);
        }
        //Hide/Show admin part side menu

        //checkIfTimeForSecondDose();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkIfTimeForSecondDose(){
        DatabaseHandler handler = new DatabaseHandler("http://83.254.68.246:3003/");
        Calendar cal = Calendar.getInstance();
        Intent intent = this.getIntent();
        User loggedInUser = (User)intent.getSerializableExtra("loggedInUser");
        /*Date date2 = new Date();
        date2.getTime();
        handler.newBooking(loggedInUser.getUsername(), "test",new Timestamp(date2.getTime()));*/
        for(Vaccination v : handler.getUserVaccinations(loggedInUser.getUsername())){
            if(v.getDose() == 1 && handler.getBooking(loggedInUser.getUsername()) == null){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                Log.i("Date",v.getDate());
                try {
                    Date date = formatter.parse(v.getDate());
                    cal.setTime(date);
                    cal.add(Calendar.DATE,14);
                    Calendar calNow = Calendar.getInstance();
                    calNow.getTime();
                    if(cal.before(calNow)){
                        vacNotification();
                    }
                } catch (ParseException e) {
                    Log.i("Failed parse date","pff");
                }

            }
        }
    }


    private void vacNotification(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Du kan nu boka din andra vaccination! Vill du vidare till bokningar eller boka senare");


        builder.setPositiveButton("Till boknngar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ToDo: Open booking page when implemented.
            }
        });
        builder.setNegativeButton("Senare", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.setCancelable(false);

        AlertDialog notification = builder.create();


        notification.show();
    }
}