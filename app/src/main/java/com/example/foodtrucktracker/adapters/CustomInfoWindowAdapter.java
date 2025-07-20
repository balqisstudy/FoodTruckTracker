package com.example.foodtrucktracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.foodtrucktracker.R;
import com.example.foodtrucktracker.models.FoodTruck;
import com.example.foodtrucktracker.utils.DateTimeUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Map;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    
    private Context context;
    private Map<Marker, FoodTruck> markerFoodTruckMap;
    
    public CustomInfoWindowAdapter(Context context, Map<Marker, FoodTruck> markerFoodTruckMap) {
        this.context = context;
        this.markerFoodTruckMap = markerFoodTruckMap;
    }
    
    @Override
    public View getInfoWindow(Marker marker) {
        return null; // Use default frame
    }
    
    @Override
    public View getInfoContents(Marker marker) {
        // Inflate the custom info window layout
        View infoWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
        
        // Get the food truck data for this marker
        FoodTruck foodTruck = markerFoodTruckMap.get(marker);
        if (foodTruck == null) {
            return null;
        }
        
        // Initialize TextViews
        TextView tvTruckName = infoWindow.findViewById(R.id.tv_truck_name);
        TextView tvTruckType = infoWindow.findViewById(R.id.tv_truck_type);
        TextView tvDescription = infoWindow.findViewById(R.id.tv_description);
        TextView tvArea = infoWindow.findViewById(R.id.tv_area);
        TextView tvLandmark = infoWindow.findViewById(R.id.tv_landmark);
        TextView tvStreetAddress = infoWindow.findViewById(R.id.tv_street_address);
        TextView tvOperatingHours = infoWindow.findViewById(R.id.tv_operating_hours);
        TextView tvContactNumber = infoWindow.findViewById(R.id.tv_contact_number);
        TextView tvReportedBy = infoWindow.findViewById(R.id.tv_reported_by);
        TextView tvReportedTime = infoWindow.findViewById(R.id.tv_reported_time);
        
        // Set the data
        tvTruckName.setText(foodTruck.getName());
        tvTruckType.setText("Type: " + foodTruck.getType());
        tvDescription.setText("Description: " + foodTruck.getDescription());
        
        // Location details
        if (foodTruck.getArea() != null && !foodTruck.getArea().isEmpty()) {
            tvArea.setText("Area: " + foodTruck.getArea());
            tvArea.setVisibility(View.VISIBLE);
        } else {
            tvArea.setVisibility(View.GONE);
        }
        
        if (foodTruck.getLandmark() != null && !foodTruck.getLandmark().isEmpty()) {
            tvLandmark.setText("Landmark: " + foodTruck.getLandmark());
            tvLandmark.setVisibility(View.VISIBLE);
        } else {
            tvLandmark.setVisibility(View.GONE);
        }
        
        if (foodTruck.getStreetAddress() != null && !foodTruck.getStreetAddress().isEmpty()) {
            tvStreetAddress.setText("Address: " + foodTruck.getStreetAddress());
            tvStreetAddress.setVisibility(View.VISIBLE);
        } else {
            tvStreetAddress.setVisibility(View.GONE);
        }
        
        if (foodTruck.getOperatingHours() != null && !foodTruck.getOperatingHours().isEmpty()) {
            tvOperatingHours.setText("🕒 Hours: " + foodTruck.getOperatingHours());
            tvOperatingHours.setVisibility(View.VISIBLE);
        } else {
            tvOperatingHours.setVisibility(View.GONE);
        }
        
        if (foodTruck.getContactNumber() != null && !foodTruck.getContactNumber().isEmpty()) {
            tvContactNumber.setText("📞 Contact: " + foodTruck.getContactNumber());
            tvContactNumber.setVisibility(View.VISIBLE);
        } else {
            tvContactNumber.setVisibility(View.GONE);
        }
        
        // Report information
        tvReportedBy.setText("Reported by: " + foodTruck.getReportedBy());
        tvReportedTime.setText("Time: " + DateTimeUtils.formatDateForDisplay(foodTruck.getReportedAt()));
        
        return infoWindow;
    }
} 