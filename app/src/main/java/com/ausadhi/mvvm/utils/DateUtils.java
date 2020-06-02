package com.ausadhi.mvvm.utils;



import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static long WORK_DURATION_MILLISECONDS = 43200000;


//    private static SimpleDateFormat classTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//    private static SimpleDateFormat classTimeFormat = new SimpleDateFormat("h:mma", Locale.getDefault());
//    private static SimpleDateFormat classDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//    private static SimpleDateFormat classDateSec = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
//    private static SimpleDateFormat classDateExpiry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//    private static SimpleDateFormat classDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
//    private static SimpleDateFormat eventDateTime = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
//
//    private static SimpleDateFormat classDateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
//    private static SimpleDateFormat packageDateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
//    private static SimpleDateFormat notificationDate = new SimpleDateFormat("h:mm a, MMM d", Locale.getDefault());
//    private static SimpleDateFormat classRatingDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
//    private static SimpleDateFormat classTime24Format = new SimpleDateFormat("HH:mm", Locale.getDefault());
//    private static SimpleDateFormat classTime12Format = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
//    private static SimpleDateFormat ExtendedDateFormat = new SimpleDateFormat("d MMM ", Locale.getDefault());

    private static SimpleDateFormat formatDateTimeServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private static SimpleDateFormat formatDateTimeServerTwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private static SimpleDateFormat simpleFormat = new SimpleDateFormat("MMM dd yyyy hh:mm aa", Locale.getDefault());
    private static SimpleDateFormat simpleFormatdmy = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private static SimpleDateFormat simpleFormatymd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static SimpleDateFormat simpleFormatddmmmyyyy = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    private static SimpleDateFormat simpleFormatddmmmyyyyhhmmss = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());




    public static String formatDate(String date) {
        try {
            Date formatDate = formatDateTimeServer.parse(date);

            return simpleFormat.format(formatDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;

    }

    public static String formatDate(long time) {
        try {
            Date date = new Date(time);
            return simpleFormatddmmmyyyy.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

}
