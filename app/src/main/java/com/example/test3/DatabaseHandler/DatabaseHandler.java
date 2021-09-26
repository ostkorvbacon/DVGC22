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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
            s = getJSONValue(s);
            list.add(new User(s));
        }
        return list;
    }

    public User getUser(String username){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"getUser", "POST", body);
        String[] info = resp.split(",");
        int i = 0;
        for (String s : info){
            info[i] = getJSONValue(s);
            i++;
        }
        return new User(info[0], info[1], info[2], info[3], info[4], info[5], info[6]);
    }

    public User newUser(String username, String password, String name, String phoneNr, String dateOfBirth, String city, String address, String role){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        data.put("Password", password);
        data.put("Name", name);
        data.put("Phone", phoneNr);
        data.put("Dob", dateOfBirth);
        data.put("City", city);
        data.put("Addr", address);
        data.put("Role", role);
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"newUser", "POST", body);
        return getUser(username);
    }

    public boolean login(String username, String password){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        data.put("Password", password);
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"tryLogin", "POST", body);
        resp = getJSONValue(resp);
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

    public boolean deleteUser(String username){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"deleteUser", "POST", body);
        resp = getJSONValue(resp);
        if (resp.equals("Success!")){
            return true;
        }
        return false;
    }

    public Clinique newClinique(String name, String phone, String city, String address){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("CliniqueName", name);
        data.put("Phone", phone);
        data.put("City", city);
        data.put("Addr", address);
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"newClinique", "POST", body);
        return getClinique(name);
    }

    public boolean cliniqueExists(String name){
        for (Clinique c : getCliniques()){
            if(c.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public Vaccination newVaccination(String username, String date, int dose, String type, String cliniqueName){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        data.put("VacDate", date);
        data.put("Dosage", Integer.toString(dose));
        data.put("Type", type);
        data.put("CliniqueId", Integer.toString(getCliniqueID(cliniqueName)));
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"newVaccination", "POST", body);
        return getVaccination(username, date, cliniqueName);
    }

    public Vaccination getVaccination(String username, String date, String cliniqueName){
        for (Vaccination v : getVaccinations()){
            if (v.getUsername().equals(username) && v.getCliniqueID() == getCliniqueID(cliniqueName) && v.getDate().equals(date)){
                return v;
            }
        }
        return null;
    }

    public List<Vaccination> getUserVaccinations(String username){
        List<Vaccination> list = new ArrayList<>();
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"getUserVaccinations", "POST", body);

        resp = resp.replace("{\"vaccinations\":[", "").replace("]}", "");
        resp = resp.replace("{", "");
        if(!resp.equals("")) {
            String[] vaccinations = resp.split("\\}");
            for (String vaccination : vaccinations) {
                if (!vaccination.equals("")) {
                    int i = 0;
                    String[] vaccineInfo = new String[6];
                    for (String item : vaccination.split(",")) {
                        if(!item.equals("")) {
                            // extract info
                            vaccineInfo[i] = getJSONValue(item);
                            i++;
                        }
                    }
                    list.add(new Vaccination(Integer.parseInt(vaccineInfo[0]), vaccineInfo[1], vaccineInfo[2], Integer.parseInt(vaccineInfo[3]), vaccineInfo[4], Integer.parseInt(vaccineInfo[5])));
                }
            }
        }

        return list;
    }

    public List<Vaccination> getVaccinations(){
        List<Vaccination> list = new ArrayList<>();
        String resp = getResponse(domain+"getVaccinations", "GET", "");
        resp = resp.replace("{\"vaccinations\":[", "").replace("]}", "");
        resp = resp.replace("{", "");
        if(!resp.equals("")) {
            String[] vaccinations = resp.split("\\}");
            for (String vaccination : vaccinations) {
                if (!vaccination.equals("")) {
                    int i = 0;
                    String[] vaccineInfo = new String[6];
                    for (String item : vaccination.split(",")) {
                        if(!item.equals("")) {
                            // extract info
                            vaccineInfo[i] = getJSONValue(item);
                            i++;
                        }
                    }
                    list.add(new Vaccination(Integer.parseInt(vaccineInfo[0]), vaccineInfo[1], vaccineInfo[2], Integer.parseInt(vaccineInfo[3]), vaccineInfo[4], Integer.parseInt(vaccineInfo[5])));
                }
            }
        }

        return list;

    }

    public int getCliniqueID(String name){
        return getClinique(name).getId();
    }

    public Clinique getClinique(int ID){
        for (Clinique c : getCliniques()){
            if (c.getId() == ID){
                return c;
            }
        }
        return new Clinique();
    }

    public Clinique getClinique(String name){
        for (Clinique c : getCliniques()){
            if (c.getName().equals(name)){
                return c;
            }
        }
        return new Clinique();
    }

    public List<Clinique> getCliniquesByCity(String city){
        List<Clinique> list = new ArrayList<Clinique>();
        for (Clinique c : getCliniques()){
            if (c.getName().equals(city)){
                list.add(c);
            }
        }
        return list;
    }

    public List<Clinique> getCliniques(){
        List<Clinique> list = new ArrayList<Clinique>();
        String resp = getResponse(domain+"getCliniques", "GET", "");
        resp = resp.replace("{\"Cliniques\":[", "").replace("]}", "");
        resp = resp.replace("{", "");
        if(!resp.equals("")) {
            String[] cliniques = resp.split("\\}");
            int c = 0;
            for (String clinique : cliniques) {
                if (!clinique.equals("")) {
                    int i = 0;
                    String[] cliniqueInfo = new String[5];
                    for (String item : clinique.split(",")) {
                        if(!item.equals("")) {
                            // extract info
                            cliniqueInfo[i] = getJSONValue(item);
                            i++;
                        }
                    }
                    list.add(new Clinique(Integer.parseInt(cliniqueInfo[0]), cliniqueInfo[1], cliniqueInfo[2], cliniqueInfo[3], cliniqueInfo[4]));
                }
                c++;
            }
        }
        return list;
    }

    public void deleteClinique(String cliniqueName){
        deleteClinique(getCliniqueID(cliniqueName));
    }

    public void deleteClinique(int cliniqueID){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("CliniqueId", Integer.toString(cliniqueID));
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"deleteClinique", "POST", body);
    }

    public void deleteVaccination(int VaccinationID){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("VacId", Integer.toString(VaccinationID));
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"deleteVaccination", "POST", body);
    }

    public boolean vaccinationExists(String username, String date, String cliniqueName){
        for (Vaccination v : getVaccinations()){
            if (v.getUsername().equals(username) && v.getCliniqueID() == getCliniqueID(cliniqueName) && v.getDate().equals(date)){
                return true;
            }
        }
        return false;
    }

    // tests all the api functions to see if they work as intended!
    public boolean testAPIFunctions(){
        String username = "testing12345@hotmail.com";
        String password = "test123";
        String name = "Test Testsson";
        String phone = "0723435634";
        String doB = "2021/12/08";
        String city = "Karlstad";
        String address = "Testgatan 12";
        String role = "Doctor";

        if(userExists(username)){
            deleteUser(username);
        }
        if(!userExists(username)) {
            User test = new User(username, name, phone, doB, city, address, role);
            User user = newUser(username, password, name, phone, doB, city, address, role);
            if(!(test.toString().equals(user.toString()))){
                Log.i("APITest", "Create user does not work correctly!");
                Log.e("APITest", test.toString());
                Log.e("APITest", user.toString());
                return false;
            }
            if(!user.toString().equals(getUser(username).toString())){
                Log.i("APITest", "Get user does not work correctly!");
                Log.e("APITest", getUser(username).toString());
                Log.e("APITest", user.toString());
                return false;
            }
            if(!login(username, password)){
                Log.i("APITest", "login user, with correct data, does not work correctly!");
                return false;
            }
            if(login(username + "fel", password)){
                Log.i("APITest", "login user, with incorrect username, does not work correctly!");
                return false;
            }
            if(login(username, password + "fel")){
                Log.i("APITest", "login user, with incorrect password, does not work correctly!");
                return false;
            }
            if(!userExists(username)){
                Log.i("APITest", "User exists, with existing user, does not work correctly!");
                return false;
            }
            if(userExists(username + "fel")){
                Log.i("APITest", "User exists, without existing user, does not work correctly!");
                return false;
            }
            if(deleteUser(username)){
                if (userExists(username)){
                    Log.i("APITest", "delete user does not work correctly!");
                    return false;
                }
            }
        }

        String cliniqueName = "Test Klinik 123";
        String kPhone = "0723423422";
        String kCity = "Karlstad";
        String kAddr = "Stora Gatan 123";

        if(cliniqueExists(cliniqueName)){
            deleteClinique(cliniqueName);
        }
        if (!cliniqueExists(cliniqueName)){
            User user = newUser(username, password, name, phone, doB, city, address, role);
            Clinique c = newClinique(cliniqueName, kPhone, kCity, kAddr);
            Clinique test = new Clinique(c.getId(), cliniqueName, kPhone, kCity, kAddr);
            if(!(test.toString().equals(c.toString()))){
                Log.i("APITest", "Create clinique does not work correctly!");
                Log.e("APITest", test.toString());
                Log.e("APITest", c.toString());
                return false;
            }
            if(!c.toString().equals(getClinique(cliniqueName).toString())){
                Log.i("APITest", "Get clinique does not work correctly!");
                Log.e("APITest", getClinique(cliniqueName).toString());
                Log.e("APITest", c.toString());
                return false;
            }
            Vaccination v = newVaccination(user.getUsername(), "2020/11/10", 1, "Pfizer", c.getName());
            Vaccination vtest = new Vaccination(v.getId(), user.getUsername(), "2020/11/10", 1, "Pfizer", getCliniqueID(c.getName()));
            if(!(vtest.toString().equals(v.toString()))){
                Log.i("APITest", "Create vaccination does not work correctly!");
                Log.e("APITest", vtest.toString());
                Log.e("APITest", v.toString());
                return false;
            }
            if(!v.toString().equals(getVaccination(user.getUsername(), "2020/11/10", c.getName()).toString())){
                Log.i("APITest", "Get clinique does not work correctly!");
                Log.e("APITest", getVaccination(user.getUsername(), "2020/11/10", c.getName()).toString());
                Log.e("APITest", v.toString());
                return false;
            }
            if(!vaccinationExists(user.getUsername(),"2020/11/10", c.getName())){
                Log.i("APITest", "vaccination exists, with existing vaccination, does not work correctly!");
                return false;
            }
            if(vaccinationExists(user.getUsername() + "fel","2020/11/10", c.getName())){
                Log.i("APITest", "vaccination exists, with wrong username, does not work correctly!");
                return false;
            }
            if(vaccinationExists(user.getUsername(),"2020/11/10" + "fel", c.getName())){
                Log.i("APITest", "vaccination exists, with wrong date, does not work correctly!");
                return false;
            }
            if(vaccinationExists(user.getUsername(),"2020/11/10", c.getName() + "fel")){
                Log.i("APITest", "vaccination exists, with wrong clinique name, does not work correctly!");
                return false;
            }
            deleteVaccination(v.getId());
            if(vaccinationExists(user.getUsername(),"2020/11/10", c.getName())){
                Log.i("APITest", "delete vaccination does not work correctly!");
                return false;
            }
            Log.i("APITest", "deleted vaccination");
            if(!cliniqueExists(c.getName())){
                Log.i("APITest", "clinique exists, with existing clinique, does not work correctly!");
                return false;
            }
            Log.i("APITest", "clinique exists");
            if(cliniqueExists(c.getName() + "fel")){
                Log.i("APITest", "clinique exists, with incorrect clinique name, does not work correctly!");
                return false;
            }
            deleteClinique(cliniqueName);
            if(cliniqueExists(c.getName())){
                Log.i("APITest", "clinique delete does not work correctly!");
                return false;
            }
        }

        return true;
    }

    private String constructJsonObject(LinkedHashMap<String, String> data){
        String ret = "{";
        for(Map.Entry<String, String> entry : data.entrySet()){
            ret += "\"" + entry.getKey() + "\": \"" + entry.getValue() + "\",";
        }
        ret = ret.substring(0, ret.length()-1);
        ret += "}";

        return ret;
    }

    private String getJSONValue(String json){
        json = json.replace("{", "").replace("}", "").replace("\"", "");
        String[] items = json.split(":");
        json = "";
        int c = 0;
        for (String item : items){
            if(c != 0){
                json += c!=1?":"+item:item;
            }
            c++;
        }

        return json;
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
                Log.e("HTTP", "Error: Did not get a response from Server.");
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