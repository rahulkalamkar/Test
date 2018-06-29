package com.android.sampleapp2;

import android.icu.text.SimpleDateFormat;
import android.icu.util.GregorianCalendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.sampleapp2.Model.Character;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DetailActivity extends AppCompatActivity {

    TextView txtName, txtMass, txtHeight, txtDate, txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatil);
        txtName = (TextView) findViewById(R.id.textViewName);
        txtMass = (TextView) findViewById(R.id.textViewMass);
        txtHeight = (TextView) findViewById(R.id.textViewHeight);
        txtDate = (TextView) findViewById(R.id.textViewDate);
        txtTime = (TextView) findViewById(R.id.textViewTime);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Character character = (Character) bundle.getSerializable("Character");
            setData(character);
        }
        formatDateString("");
    }

    public void setData(Character character) {
        txtName.setText(character.getName());
        txtMass.setText(character.getMass() + " kg");
        txtHeight.setText(getHeightInMeters(character.getHeight()) + " m");
        txtDate.setText(formatDateString(character.getCreated()));
        txtTime.setText(formatTimeString(character.getCreated()));
    }

    public String getHeightInMeters(String height) {
        float heightCM = Float.parseFloat(height);
        float heightM = heightCM / 100;
        return "" + heightM;
    }

    public String formatDateString(String string) {
        String strDate;
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(android.icu.util.TimeZone.getTimeZone("GMT"));
        try {
            Date date = format.parse(string);
            strDate = date.getDate() + "-" + (date.getMonth() + 1) + "-" + (date.getYear() + 1900);
        } catch (Exception e) {
            e.printStackTrace();
            strDate = string;
        }
        return strDate;
    }

    public String formatTimeString(String string) {
        String strDate;
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(android.icu.util.TimeZone.getTimeZone("GMT"));
        try {
            Date date = format.parse(string);
            strDate = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
        } catch (Exception e) {
            e.printStackTrace();
            strDate = string;
        }
        return strDate;
    }
}
