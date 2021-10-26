package com.example.test3.VaccinePassport;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test3.DatabaseHandler.DatabaseHandler;
import com.example.test3.DatabaseHandler.User;
import com.example.test3.DatabaseHandler.Vaccination;
import com.example.test3.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VaccinePassport{
    private String username;
    private ImageView iV;
    private TextView tv;
    private DatabaseHandler api;

    public VaccinePassport(String username, ImageView view, TextView tv){
        this.username = username;
        this.iV = view;
        this.tv = tv;
        api = new DatabaseHandler("http://83.254.68.246:3003/");
    }

    public void getQRCode(){
        QRCodeWriter writer = new QRCodeWriter();
        User u = api.getUser(username);
        Vaccination v = null;
        for(Vaccination element : api.getVaccinations()){
            if(element.getUsername().equals(u.getUsername()) && element.getDose() == 2){
                v = element;
            }
        }
        if(v != null && hasBeenTwoWeeksAfterDose2(v.getDate())) {
            iV.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            String date = v.getDate();
            String name = u.getName();
            String type = v.getType();
            try {
                BitMatrix bitMatrix = writer.encode(name + "," + date + "," + type, BarcodeFormat.QR_CODE, 512, 512);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }
                iV.setImageBitmap(bmp);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        else{
            iV.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.INVISIBLE);
        }
    }

    private boolean hasBeenTwoWeeksAfterDose2(String date){
        int noOfDays = 14; //i.e two weeks
        Calendar calendar = Calendar.getInstance();
        String[] sepDate = date.split("/");
        calendar.setTime(new Date(Integer.parseInt(sepDate[0])-1900, Integer.parseInt(sepDate[1])-1, Integer.parseInt(sepDate[2])));
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        Date valid_after = calendar.getTime();
        if (new Date().after(valid_after)) {
            return true;
        }
        return false;
    }
}
