package com.example.test3.DatabaseHandler;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
Example uses of this handler:
    DatabaseHandler handler = new DatabaseHandler("http://83.254.68.246:3003/");
    String username = "testing1234@hotmail.com";
    String password = "password123";
    if(!handler.userExists(username)) {
        User user1 = handler.newUser(username, password, "Test Testsson", "0723435634", "21/12/8", "Karlstad", "Testgatan 12", "Doctor");
        Log.i("HTTP", user1.toString());
    }
    else{
        Log.i("HTTP", "User reg failed, user already exists!");
    }
    if(handler.login(username, password)){
        Log.i("HTTP", "Login successfully.");
    }
    else{
        Log.i("HTTP", "Login failed.");
    }
    handler.getUserList();
    User user2 = handler.getUser("something@gmail.com");
    Log.i("HTTP", user2.toString());
*/

public class DatabaseHandler {
    private String domain;

    public DatabaseHandler(String domain){
        this.domain = domain;
    }

    public List<User> getUserList(){
        List<User> list = new ArrayList<User>();
        String resp = getResponse(domain+"getUserList", "GET", "");
        // for each string in the list of usernames
        for (String s : resp.replace("{\"Emails\":[", "").replace("]}", "").split(",")){
            // extract username
            s = s.split(":")[1].replace("\"", "").replace("}", "");
            list.add(new User(s));
        }
        return list;
    }

    public User getUser(String username){
        String body = "{\"Email\": \"" + username + "\"}";
        String resp = getResponse(domain+"getUser", "POST", body);

        resp = resp.replace("{", "").replace("}", ""). replace("\"", "");
        String[] info = resp.split(",");
        int i = 0;
        for (String s : info){
            info[i] = "";
            String[] items = s.split(":");
            int c = 0;
            for (String item : items){
                if(c != 0){
                    info[i] += c!=1?":"+item:item;
                }
                c++;
            }
            i++;
        }
        return new User(info[0], info[1], info[2], info[3], info[4], info[5], info[6]);
    }

    public User newUser(String username, String password, String name, String phoneNr, String dateOfBirth, String city, String address, String role){
        String body = "{";
        body += "\"Email\": \"" + username + "\",";
        body += "\"Password\": \"" + password + "\",";
        body += "\"Name\": \"" + name + "\",";
        body += "\"Phone\": \"" + phoneNr + "\",";
        body += "\"Dob\": \"" + dateOfBirth + "\",";
        body += "\"City\": \"" + city + "\",";
        body += "\"Addr\": \"" + address + "\",";
        body += "\"Role\": \"" + role + "\"";
        body += "}";
        String resp = getResponse(domain+"newUser", "POST", body);
        return getUser(username);
    }

    public boolean login(String username, String password){
        String body = "{";
        body += "\"Email\": \"" + username + "\",";
        body += "\"Password\": \"" + password + "\"";
        body += "}";
        String resp = getResponse(domain+"tryLogin", "POST", body);
        resp = resp.replace("{", "").replace("}", "").replace("\"", "");
        resp = resp.split(":")[1]; //"Success!" OR "ERROR"
        if (resp.equals("Success!")){
            return true;
        }
        return false;
    }

    public boolean userExists(String username){
        for (User u : getUserList()){
            if(u.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    private String getResponse(String url, String requestMethod, String body){
        try {
            DatabaseReader reader = new DatabaseReader();
            if(requestMethod.equals("GET")){
                reader.init(url, requestMethod);
            }else{
                reader.init(url, requestMethod, body);
            }
            Thread HTTPThread = new Thread(reader);
            HTTPThread.start();
            HTTPThread.join();

            return reader.getString();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("HTTP", "Error: Could not join thread.");
        }
        return "";
    }

    private HttpURLConnection establishConnection(String url, String requestMethod, String body) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(requestMethod);
        if(requestMethod.equals("POST") && body != null){
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            Log.i("HTTP", "Body: " + body);
            try(OutputStream os = connection.getOutputStream()) {
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(body);
                osw.flush();
                osw.close();
                os.flush();
            }
        }
        if (connection.getResponseCode() != 200){
            Log.e("HTTP", "Request did not return ok: " + connection.getResponseCode());
            return null;
        }

        return connection;
    }

    private String readStream(HttpURLConnection connection) throws IOException {
        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while((line = reader.readLine()) != null){
            sb.append(line);
        }
        is.close();
        reader.close();
        Log.d("Read", "Finished reading from database...");
        return sb.toString();
    }

    private class DatabaseReader implements Runnable{
        private String response;
        private String url;
        private String requestMethod;
        private String body;
        @Override
        public void run() {
            try {
                response = readStream(establishConnection(url, requestMethod, body));
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("HTTP", "Error: Did not get a response from getUserList.");
            }
        }
        public String getString(){
            return response;
        }
        public void init(String url, String requestMethod){
            this.url = url;
            this.requestMethod = requestMethod;
        }
        public void init(String url, String requestMethod, String body){
            this.url = url;
            this.requestMethod = requestMethod;
            this.body = body;
        }

    }
}
