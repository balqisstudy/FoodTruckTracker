package com.example.foodtrucktracker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtils {
    
    // ISO 8601 format for server communication
    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String DISPLAY_DATE_FORMAT = "MMM dd, yyyy HH:mm";
    private static final String SIMPLE_DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "HH:mm";
    
    public static String getCurrentISOTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_DATE_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }
    
    public static String formatDateForDisplay(String isoDateString) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat(ISO_DATE_FORMAT, Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(isoDateString);
            
            SimpleDateFormat displayFormat = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault());
            return displayFormat.format(date);
        } catch (ParseException e) {
            return isoDateString; // Return original if parsing fails
        }
    }
    
    public static String formatDateSimple(String isoDateString) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat(ISO_DATE_FORMAT, Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(isoDateString);
            
            SimpleDateFormat simpleFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault());
            return simpleFormat.format(date);
        } catch (ParseException e) {
            return isoDateString;
        }
    }
    
    public static String formatTimeOnly(String isoDateString) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat(ISO_DATE_FORMAT, Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(isoDateString);
            
            SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
            return timeFormat.format(date);
        } catch (ParseException e) {
            return isoDateString;
        }
    }
    
    public static String getTimeAgo(String isoDateString) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat(ISO_DATE_FORMAT, Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(isoDateString);
            
            long diff = new Date().getTime() - date.getTime();
            long minutes = diff / (1000 * 60);
            long hours = diff / (1000 * 60 * 60);
            long days = diff / (1000 * 60 * 60 * 24);
            
            if (minutes < 60) {
                return minutes + " minutes ago";
            } else if (hours < 24) {
                return hours + " hours ago";
            } else {
                return days + " days ago";
            }
        } catch (ParseException e) {
            return "Unknown";
        }
    }
}
