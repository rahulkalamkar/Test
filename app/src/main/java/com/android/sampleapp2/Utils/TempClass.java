package com.android.sampleapp2.Utils;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rahul.kalamkar on 6/28/2018.
 */

public class TempClass {

    public static void main(String[] args) {
        String dtStart = "2014-12-10T16:26:56.13Z";
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            System.out.println("format 1 " + format.format(dtStart));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
