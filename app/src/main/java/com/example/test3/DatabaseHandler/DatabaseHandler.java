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
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
        if (!resp.equals("ERROR: Login Failed")){
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

    public boolean isUserAdmin(String username){
        if(userExists(username)){
            if(!getUser(username).getRole().toLowerCase().equals("user")){
                return true;
            }
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
        LinkedHashMap<String, String> input = new LinkedHashMap<>();
        input.put("Email", username);
        String body = constructJsonObject(input);
        String resp = getResponse(domain+"getUserVaccinations", "POST", body);
        List<List<String>> output = getJsonArrayValues("vaccinations", resp);
        for(List<String> innerList : output){
            list.add(new Vaccination(Integer.parseInt(innerList.get(0)), innerList.get(1), innerList.get(2), Integer.parseInt(innerList.get(3)), innerList.get(4), Integer.parseInt(innerList.get(5))));
        }
        return list;
    }

    public List<Vaccination> getVaccinations(){
        List<Vaccination> list = new ArrayList<>();
        String resp = getResponse(domain+"getVaccinations", "GET", "");
        List<List<String>> data = getJsonArrayValues("vaccinations", resp);
        for(List<String> innerList : data){
            list.add(new Vaccination(Integer.parseInt(innerList.get(0)), innerList.get(1), innerList.get(2), Integer.parseInt(innerList.get(3)), innerList.get(4), Integer.parseInt(innerList.get(5))));
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
        List<List<String>> data = getJsonArrayValues("cliniques", resp);
        for(List<String> innerList : data){
            list.add(new Clinique(Integer.parseInt(innerList.get(0)), innerList.get(1), innerList.get(2), innerList.get(3), innerList.get(4)));
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

    public int getBookingID(String username){
        for(Booking b : getBookings()){
            if (b.getUsername().equals(username)){
                return b.getId();
            }
        }
        return -1;
    }

    public boolean bookingExists(int bookingID){
        for (Booking b : getBookings()){
            if (b.getId() == bookingID){
                return true;
            }
        }
        return false;
    }

    public Booking getBooking(String username){
        for(Booking b : getBookings()){
            if (b.getUsername().equals(username)){
                return b;
            }
        }
        return null;
    }

    public Booking newBooking(String username, String cliniqueName, Timestamp dateTime){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        data.put("CliniqueId", Integer.toString(getCliniqueID(cliniqueName)));
        data.put("Date", dateTime.toString());
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"newBooking", "POST", body);
        return getBooking(username);
    }

    public List<Booking> getUserBookings(String username){
        List<Booking> list = new ArrayList<Booking>();
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"getUserBookings", "POST", body);
        List<List<String>> output = getJsonArrayValues("bookings", resp);
        for(List<String> innerList : output){
            list.add(new Booking(Integer.parseInt(innerList.get(0)),  innerList.get(1), Integer.parseInt(innerList.get(2)), getTimestampFromString(innerList.get(3))));
        }
        return list;
    }

    public List<Booking> getBookings(){
        List<Booking> list = new ArrayList<Booking>();
        String resp = getResponse(domain+"getBookings", "GET", "");
        List<List<String>> output = getJsonArrayValues("bookings", resp);
        for(List<String> innerList : output){
            list.add(new Booking(Integer.parseInt(innerList.get(0)),  innerList.get(1), Integer.parseInt(innerList.get(2)), getTimestampFromString(innerList.get(3))));
        }
        return list;
    }

    public List<Booking> getBookingsToday(){
        List<Booking> list = new ArrayList<Booking>();
        String resp = getResponse(domain+"getBookingsToday", "POST", "");
        List<List<String>> output = getJsonArrayValues("bookings", resp);
        for(List<String> innerList : output){
            list.add(new Booking(Integer.parseInt(innerList.get(0)),  innerList.get(1), Integer.parseInt(innerList.get(2)), getTimestampFromString(innerList.get(3))));
        }
        return list;
    }

    public List<Booking> getBookingsToday(String cliniqueName){
        List<Booking> list = new ArrayList<Booking>();
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("CliniqueId", Integer.toString(getCliniqueID(cliniqueName)));
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"getBookingsToday", "POST", body);
        List<List<String>> output = getJsonArrayValues("bookings", resp);
        for(List<String> innerList : output){
            list.add(new Booking(Integer.parseInt(innerList.get(0)),  innerList.get(1), Integer.parseInt(innerList.get(2)), getTimestampFromString(innerList.get(3))));
        }
        return list;
    }

    public void deleteBookings(String username){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"deleteBooking", "POST", body);
    }

    public Vaccination doVaccination(String username, int dose, String type){
        Booking b = getBooking(username);
        String[] dateinfo = b.getDate().toString().split(" ")[0].split("-");
        String date = dateinfo[0] + "/" + dateinfo[1] + "/" + dateinfo[2];
        Vaccination v = newVaccination(username, date, dose, type, getClinique(b.getCliniqueID()).getName());
        if (bookingExists(getBookingID(username))) {
            deleteBookings(username);
        }
        if (questionnaireExists(username)) {
            deleteQuestionnaire(username);
        }
        return v;
    }

    public Questionnaire getQuestionnaire(String username){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"getQuestionnaire", "POST", body);
        List<String> info = getJsonValues(resp);
        if(!resp.equals("")) {
            return new Questionnaire(info.get(0), convertFromQuestAns(info.get(1)), info.get(2).equals("1"));
        }
        return null;
    }

    public void deleteQuestionnaire(String username){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"deleteQuestionnaire", "POST", body);
    }

    public boolean questionnaireExists(String username){
        if(getQuestionnaire(username) != null){
            return true;
        }
        return false;
    }

    public void updateQuestionnaire(String username, boolean approved){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        data.put("Approved", approved?"1":"0");
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"updateQuestionnaire", "POST", body);
    }

    public Questionnaire newQuestionnaire(String username, boolean[] questionAnswers){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Email", username);
        data.put("Answers", convertToQuestAns(questionAnswers));
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"newQuestionnaire", "POST", body);
        List<String> info = getJsonValues(resp);
        if(resp.contains("Success")) {
            return getQuestionnaire(username);
        }
        return null;
    }

    public void setMinimumAgeForVaccination(int age){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Setting", "MinVaccAge");
        data.put("Value", Integer.toString(age));
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"updateSetting", "POST", body);
    }

    public int getMinimumAgeForVaccination(){
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("Setting", "MinVaccAge");
        String body = constructJsonObject(data);
        String resp = getResponse(domain+"getSetting", "POST", body);
        return Integer.parseInt(getJsonValues(resp).get(1));
    }

    public boolean isQualifiedForBooking(String username){
        User user = getUser(username);
        int age = Integer.parseInt(user.getDateOfBirth().split("/")[0]);
        int year = new Date().getYear() + 1900;
        if(age >= getMinimumAgeForVaccination()){
            return true;
        }
        return false;
    }



    // tests all the api functions to see if they work as intended!
    public boolean testAPIFunctions(){
        Log.i("APITest", "Starting API test...");
        String username = "testing12345@hotmail.com";
        String password = "test123";
        String name = "Test Testsson";
        String phone = "0723435634";
        String doB = "1997/12/08";
        String city = "Karlstad";
        String address = "Testgatan 12";
        String role = "Doctor";

        String cliniqueName = "Test Klinik 123";
        String kPhone = "0723423422";
        String kCity = "Karlstad";
        String kAddr = "Stora Gatan 123";

        boolean[] questAns = {true, true, false, true, false};

        Timestamp date = Timestamp.valueOf("2021-09-27 10:30:00.0");
        String[] dateinfo = date.toString().split(" ")[0].split("-");
        String dateNoTime = dateinfo[0] + "/" + dateinfo[1] + "/" + dateinfo[2];

        // test users
        Log.i("APITest", "testing users...");
        clearTestData(username, dateNoTime, cliniqueName);
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

        // test cliniques and vaccinations
        Log.i("APITest", "testing cliniques and vaccinations...");
        clearTestData(username, dateNoTime, cliniqueName);
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
            if(!cliniqueExists(c.getName())){
                Log.i("APITest", "clinique exists, with existing clinique, does not work correctly!");
                return false;
            }
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

        // test bookings
        Log.i("APITest", "testing bookings...");
        clearTestData(username, dateNoTime, cliniqueName);
        if(!bookingExists(getBookingID(username))){
            User user = newUser(username, password, name, phone, doB, city, address, role);
            Clinique c = newClinique(cliniqueName, kPhone, kCity, kAddr);
            Booking b = newBooking(user.getUsername(), c.getName(), date);
            Booking test = new Booking(b.getId(), user.getUsername(), c.getId(), date);
            if(!(test.toString().equals(b.toString()))){
                Log.i("APITest", "Create booking does not work correctly!");
                Log.e("APITest", test.toString());
                Log.e("APITest", b.toString());
                return false;
            }
            if(!b.toString().equals(getBooking(user.getUsername()).toString())){
                Log.i("APITest", "Get booking does not work correctly!");
                Log.e("APITest", getBooking(user.getUsername()).toString());
                Log.e("APITest", b.toString());
                return false;
            }
            if(!bookingExists(b.getId())){
                Log.i("APITest", "Booking exists does not work correctly!");
                return false;
            }
            Vaccination v = doVaccination(user.getUsername(), 1, "Pfizer");
            if(!vaccinationExists(user.getUsername(), dateNoTime, c.getName())){
                Log.i("APITest", "DoVaccination does not generate new vaccination");
                return false;
            }
            if(bookingExists(b.getId())){
                Log.i("APITest", "DoVaccination does not remove booking");
                return false;
            }
        }

        // Test questionnaires
        // make sure there is nothing left from earlier tests that have failed before the end
        Log.i("APITest", "testing questionnaires...");
        clearTestData(username, dateNoTime, cliniqueName);
        if(!questionnaireExists(username)){
            User user = newUser(username, password, name, phone, doB, city, address, role);
            Clinique c = newClinique(cliniqueName, kPhone, kCity, kAddr);
            Booking b = newBooking(user.getUsername(), c.getName(), date);
            Questionnaire q = newQuestionnaire(username, questAns);
            Questionnaire test = new Questionnaire(username, questAns);
            if(!(test.toString().equals(q.toString()))){
                Log.i("APITest", "Create questionnaire does not work correctly!");
                Log.e("APITest", test.toString());
                Log.e("APITest", q.toString());
                return false;
            }
            if(!q.toString().equals(getQuestionnaire(user.getUsername()).toString())){
                Log.i("APITest", "Get questionnaire does not work correctly!");
                Log.e("APITest", getBooking(user.getUsername()).toString());
                Log.e("APITest", q.toString());
                return false;
            }
            if(!questionnaireExists(username)){
                Log.i("APITest", "Questionnaire exists does not work correctly!");
                return false;
            }
            updateQuestionnaire(username, true);
            if(!getQuestionnaire(username).isApproved()){
                Log.i("APITest", "Questionnaire update does not work correctly!");
                return false;
            }
            updateQuestionnaire(username, false);
            if(getQuestionnaire(username).isApproved()){
                Log.i("APITest", "Questionnaire update does not work correctly!");
                return false;
            }
            Vaccination v = doVaccination(user.getUsername(), 1, "Pfizer");
            if(!vaccinationExists(user.getUsername(), dateNoTime, c.getName())){
                Log.i("APITest", "DoVaccination does not generate new vaccination");
                return false;
            }
            if(questionnaireExists(q.getUsername())){
                Log.i("APITest", "DoVaccination does not delete questionnaire");
                return false;
            }
        }

        // test minimum age for vaccination
        clearTestData(username, dateNoTime, cliniqueName);
        Log.i("APITest", "testing minimum age for vaccination...");
        int originalValue = getMinimumAgeForVaccination();
        setMinimumAgeForVaccination(20);
        if(getMinimumAgeForVaccination() != 20){
            Log.i("APITest", "setMinimumAgeForVaccination does not work correctly");
            return false;
        }
        User user = newUser(username, password, name, phone, doB, city, address, role);
        isQualifiedForBooking(username);
        if(!isQualifiedForBooking(username)){
            Log.i("APITest", "isQualifiedForBooking does not work correctly");
            return false;
        }
        setMinimumAgeForVaccination(originalValue);

        // clear the test data from the database
        clearTestData(username, dateNoTime, cliniqueName);

        Log.i("APITest", "Api test passed!");

        return true;
    }

    private void clearTestData(String username, String dateNoTime, String cliniqueName){
        if(questionnaireExists(username)){
            deleteQuestionnaire(username);
            getQuestionnaire(username);
        }
        if(vaccinationExists(username, dateNoTime, cliniqueName)){
            deleteVaccination(getVaccination(username, dateNoTime, cliniqueName).getId());
        }
        if(bookingExists(getBookingID(username))){
            deleteBookings(username);
        }
        if(userExists(username)){
            deleteUser(username);
        }
        if(cliniqueExists(cliniqueName)){
            deleteClinique(cliniqueName);
        }
    }

    private String convertToQuestAns(boolean[] qList){
        String ret = "";
        for(boolean q : qList){
            ret += q?"1/":"0/";
        }
        return ret;
    }

    private boolean[] convertFromQuestAns(String s){
        boolean[] qList = new boolean[s.split("/").length];
        int i = 0;
        for (String ans : s.split("/")){
            qList[i] = ans.equals("1");
            i++;
        }
        return qList;
    }

    private Timestamp getTimestampFromString(String s){
        try {
            return new Timestamp(DateUtil.provideDateFormat().parse(s).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("HTTP", "Could not parse string to timestamp");
        }
        return null;
    }

    private List<List<String>> getJsonArrayValues(String arrayName, String json){
        List<List<String>> data = new ArrayList<>();
        json = json.replace("{\"" + arrayName + "\":[", "").replace("]}", "");
        json = json.replace("{", "");
        if(!json.equals("")) {
            String[] items = json.split("\\}");
            for (String item : items) {
                if (!item.equals("")) {
                    data.add(getJsonValues(item));
                }
            }
        }
        return data;
    }

    private List<String> getJsonValues(String jsonObject){
        List<String> list = new ArrayList<>();
        for (String field : jsonObject.split(",")) {
            if(!field.equals("")) {
                // extract info
                list.add(getJSONValue(field));
            }
        }
        return list;
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