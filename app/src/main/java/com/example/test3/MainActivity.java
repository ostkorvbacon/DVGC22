package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


//http relevant libraries



public class MainActivity extends AppCompatActivity {
    ArrayList<String> email = new ArrayList<String>();
    ArrayList<String> pass = new ArrayList<String>();
    String res = null;
    public void getData(TextView view){
        String myUrl = "https://corona.lmao.ninja/v2/all";
        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                response -> {
                    try{
                        //Create a JSON object containing information from the API.
                        JSONObject myJsonObject = new JSONObject(response);
                        view.setText(myJsonObject.getString("cases"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    public boolean loginAttempt(String loginId, String password){
        TextView testtxt = findViewById(R.id.test);
        TextView messagetxt = findViewById(R.id.messagetxt);
        String loginEndpoint = "http://83.254.68.246:3003/tryLogin";

        JSONObject registerData = new JSONObject();
        try{
            registerData.put("Email", "aaa3@gmail.com");
            registerData.put("Password", "pass123");
            registerData.put("Name", "Toni");
            registerData.put("Phone", "0738887799");
            registerData.put("Dob", "21/12/9");
            registerData.put("City", "Karlstad");
            registerData.put("Addr", "gatan123");
            registerData.put("Role", "Doctor");
        }catch (JSONException e){
            e.printStackTrace();
        }

        JSONObject loginData = new JSONObject();
        try {
            loginData.put("Email", loginId);
            loginData.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest loginReq = new JsonObjectRequest(Request.Method.POST,
                loginEndpoint,
                loginData,
                response -> {
                    try {
                        res = response.getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(res.equals("Success!")){
                        Intent loginIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        loginIntent.putExtra("loginId", loginId);
                        startActivity(loginIntent);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                },
                volleyError -> Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show()
        );
        queue.add(loginReq);

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.login_button);
        //ProgressBar loadingProgressBar = findViewById(R.id.loading);
        Button createButton = findViewById(R.id.create_button);


        Intent temp=getIntent();
        if (temp.hasExtra("newemaillist")) {
            email=getIntent().getStringArrayListExtra("newemaillist");
            pass=getIntent().getStringArrayListExtra("newpasslist");
        }
        else {
            email.add("login");
            pass.add("");
        }

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(getApplicationContext(), CreateActivity.class);
                createIntent.putExtra("oldmaillist",email);
                createIntent.putExtra("oldpasslist",pass);
                startActivity(createIntent);
            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadingProgressBar.setVisibility(view.VISIBLE);

                String loginId = ((EditText)findViewById(R.id.loginId)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();


                loginAttempt(loginId, password);
                //boolean success = loginAttempt(loginId, password);

                /*
                if(success){
                    //check if user is an admin and direct accordingly to the respective pages
                    Intent loginIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    loginIntent.putExtra("loginId", loginId);
                    startActivity(loginIntent);
                }else{
                    loadingProgressBar.setVisibility(view.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(),"Wrong username or password",Toast.LENGTH_SHORT);
                    toast.show();
                }*/


            }
        });
    }

}

