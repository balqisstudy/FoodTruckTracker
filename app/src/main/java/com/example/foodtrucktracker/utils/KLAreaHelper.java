package com.example.foodtrucktracker.utils;

import java.util.Arrays;
import java.util.List;

public class KLAreaHelper {
    
    // Popular Kuala Lumpur areas for food trucks
    public static final List<String> KL_AREAS = Arrays.asList(
        "Bukit Bintang",
        "KLCC (Kuala Lumpur City Centre)",
        "Petaling Street (Chinatown)",
        "Bangsar",
        "Damansara",
        "Subang Jaya",
        "PJ (Petaling Jaya)",
        "Ampang",
        "Cheras",
        "Kepong",
        "Wangsa Maju",
        "Setapak",
        "Gombak",
        "Selayang",
        "Kuala Selangor",
        "Shah Alam",
        "Cyberjaya",
        "Putrajaya",
        "Mont Kiara",
        "Sri Hartamas",
        "Taman Tun Dr Ismail (TTDI)",
        "Mutiara Damansara",
        "Bandar Utama",
        "Sunway",
        "Puchong",
        "Seri Kembangan",
        "Kajang",
        "Semenyih",
        "Hulu Langat",
        "Sepang"
    );
    
    // Common landmarks in Kuala Lumpur
    public static final List<String> KL_LANDMARKS = Arrays.asList(
        "KLCC Twin Towers",
        "Pavilion Mall",
        "Suria KLCC",
        "Bukit Bintang Walk",
        "Petronas Twin Towers",
        "KL Tower",
        "Merdeka Square",
        "Central Market",
        "Petaling Street",
        "Batu Caves",
        "National Mosque",
        "Sultan Abdul Samad Building",
        "KL Sentral",
        "Mid Valley Megamall",
        "The Gardens Mall",
        "One Utama",
        "Sunway Pyramid",
        "IOI City Mall",
        "Avenue K",
        "Lot 10",
        "Fahrenheit 88",
        "Starhill Gallery",
        "Plaza Low Yat",
        "Berjaya Times Square",
        "Sungei Wang Plaza",
        "Lot 10",
        "Pertama Complex",
        "Sogo KL",
        "KLCC Park",
        "Lake Gardens",
        "Titiwangsa Lake",
        "KL Bird Park",
        "Aquaria KLCC",
        "KL Tower Mini Zoo",
        "National Planetarium",
        "Islamic Arts Museum",
        "National Museum",
        "Royal Museum",
        "National Library",
        "KL Convention Centre",
        "Putra World Trade Centre"
    );
    
    // Common street types in Kuala Lumpur
    public static final List<String> KL_STREET_TYPES = Arrays.asList(
        "Jalan",
        "Lorong",
        "Taman",
        "Persiaran",
        "Lebuh",
        "Lingkaran",
        "Lebuhraya",
        "Jalan Besar",
        "Jalan Raja",
        "Jalan Sultan",
        "Jalan Tun",
        "Jalan Datuk",
        "Jalan Tan Sri",
        "Jalan Dato'",
        "Jalan Tengku",
        "Jalan Raja Muda",
        "Jalan Raja Chulan",
        "Jalan Raja Laut",
        "Jalan Tuanku Abdul Rahman",
        "Jalan Tuanku Abdul Halim"
    );
    
    // Operating hours suggestions
    public static final List<String> OPERATING_HOURS_SUGGESTIONS = Arrays.asList(
        "6:00 AM - 10:00 AM (Breakfast)",
        "11:00 AM - 3:00 PM (Lunch)",
        "6:00 PM - 11:00 PM (Dinner)",
        "10:00 PM - 2:00 AM (Late Night)",
        "24 Hours",
        "Weekdays: 7:00 AM - 9:00 PM",
        "Weekends: 8:00 AM - 10:00 PM",
        "Monday - Friday: 8:00 AM - 6:00 PM",
        "Saturday - Sunday: 9:00 AM - 8:00 PM",
        "Daily: 12:00 PM - 12:00 AM"
    );
    
    /**
     * Get area suggestions based on input
     */
    public static List<String> getAreaSuggestions(String input) {
        if (input == null || input.trim().isEmpty()) {
            return KL_AREAS;
        }
        
        String lowerInput = input.toLowerCase();
        return KL_AREAS.stream()
                .filter(area -> area.toLowerCase().contains(lowerInput))
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Get landmark suggestions based on input
     */
    public static List<String> getLandmarkSuggestions(String input) {
        if (input == null || input.trim().isEmpty()) {
            return KL_LANDMARKS;
        }
        
        String lowerInput = input.toLowerCase();
        return KL_LANDMARKS.stream()
                .filter(landmark -> landmark.toLowerCase().contains(lowerInput))
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Get operating hours suggestions
     */
    public static List<String> getOperatingHoursSuggestions() {
        return OPERATING_HOURS_SUGGESTIONS;
    }
    
    /**
     * Format area name for display
     */
    public static String formatAreaName(String area) {
        if (area == null || area.trim().isEmpty()) {
            return "";
        }
        
        // Capitalize first letter of each word
        String[] words = area.trim().split("\\s+");
        StringBuilder formatted = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) formatted.append(" ");
            if (words[i].length() > 0) {
                formatted.append(words[i].substring(0, 1).toUpperCase())
                        .append(words[i].substring(1).toLowerCase());
            }
        }
        
        return formatted.toString();
    }
    
    /**
     * Validate if coordinates are within Kuala Lumpur area
     */
    public static boolean isWithinKLArea(double latitude, double longitude) {
        // Approximate KL boundaries
        double minLat = 2.8;  // Southern boundary
        double maxLat = 3.4;  // Northern boundary
        double minLng = 101.4; // Western boundary
        double maxLng = 101.9; // Eastern boundary
        
        return latitude >= minLat && latitude <= maxLat &&
               longitude >= minLng && longitude <= maxLng;
    }
} 