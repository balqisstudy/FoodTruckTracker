package com.example.foodtrucktracker.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationHelper {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private Context context;

    public interface LocationListener {
        void onLocationReceived(double latitude, double longitude);
        void onLocationError(String error);
    }

    public LocationHelper(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED &&
               ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    public void getCurrentLocation(LocationListener listener) {
        if (!checkLocationPermission()) {
            listener.onLocationError("Location permission not granted");
            return;
        }

        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                listener.onLocationReceived(location.getLatitude(), location.getLongitude());
                            } else {
                                // Try requesting a fresh location if last location is null
                                requestFreshLocation(listener);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        listener.onLocationError("Error getting location: " + e.getMessage());
                        // Fallback to default location (Kuala Lumpur)
                        listener.onLocationReceived(3.139, 101.6869);
                    });
        } catch (SecurityException e) {
            listener.onLocationError("Security exception: " + e.getMessage());
            // Fallback to default location
            listener.onLocationReceived(3.139, 101.6869);
        }
    }

    private void requestFreshLocation(LocationListener listener) {
        // If no last known location, use default Kuala Lumpur coordinates
        listener.onLocationReceived(3.139, 101.6869);
        listener.onLocationError("Using default location (Kuala Lumpur). Please enable GPS for accurate location.");
    }

    public static int getLocationPermissionRequestCode() {
        return LOCATION_PERMISSION_REQUEST_CODE;
    }
}
