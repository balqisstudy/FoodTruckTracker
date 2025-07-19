package com.example.foodtrucktracker;

import androidx.fragment.app.FragmentActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.foodtrucktracker.models.FoodTruck;
import com.example.foodtrucktracker.network.ApiClient;
import com.example.foodtrucktracker.network.FoodTruckApiService;
import com.example.foodtrucktracker.utils.DateTimeUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.foodtrucktracker.databinding.ActivityMapsBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FoodTruckApiService apiService;
    private Map<Marker, FoodTruck> markerFoodTruckMap = new HashMap<>();
    // Store all loaded food trucks for filtering
    private List<FoodTruck> allFoodTrucks = new java.util.ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize API service
        apiService = ApiClient.getFoodTruckApiService();

        // Ensure the map fragment is present
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit();
        }
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Add click handler for the yellow FAB to show Arau, Perlis
        findViewById(R.id.fab_add_data).setOnClickListener(v -> {
            if (mMap != null) {
                LatLng arauPerlis = new LatLng(6.4241, 100.2716);
                mMap.addMarker(new MarkerOptions()
                        .position(arauPerlis)
                        .title("Arau, Perlis")
                        .snippet("Food Truck Spot in Arau, Perlis"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arauPerlis, 15));
            }
        });

        // Set up chip filters
        findViewById(R.id.chipMee).setOnClickListener(v -> filterMarkersByType("Mee Goreng"));
        findViewById(R.id.chipCoffee).setOnClickListener(v -> filterMarkersByType("Coffee"));
        findViewById(R.id.chipBBQ).setOnClickListener(v -> filterMarkersByType("BBQ"));
        // Reset filter when clicking outside chips (or you can add a 'Show All' chip/button if you want)
        findViewById(R.id.chipGroupFilters).setOnClickListener(v -> showAllMarkers());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set default location to Kuala Lumpur, Malaysia
        LatLng defaultLocation = new LatLng(3.139, 101.6869);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));

        // Enable location if permission is granted
        enableMyLocation();

        // Set up info window adapter
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public android.view.View getInfoWindow(Marker marker) {
                return null; // Use default frame
            }

            @Override
            public android.view.View getInfoContents(Marker marker) {
                // You can customize this layout in custom_info_window.xml
                return null;
            }
        });

        // Load food trucks from server
        loadFoodTrucks();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void loadFoodTrucks() {
        Call<List<FoodTruck>> call = apiService.getAllFoodTrucks();
        call.enqueue(new Callback<List<FoodTruck>>() {
            @Override
            public void onResponse(Call<List<FoodTruck>> call, Response<List<FoodTruck>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allFoodTrucks = response.body();
                    displayFoodTrucksOnMap(allFoodTrucks);
                    Log.d(TAG, "Loaded " + allFoodTrucks.size() + " food trucks");
                } else {
                    Log.e(TAG, "Failed to load food trucks: " + response.code());
                    Toast.makeText(MapsActivity.this, "Failed to load food trucks", Toast.LENGTH_SHORT).show();
                    loadSampleData();
                }
            }

            @Override
            public void onFailure(Call<List<FoodTruck>> call, Throwable t) {
                Log.e(TAG, "Error loading food trucks", t);
                Toast.makeText(MapsActivity.this, "Error connecting to server", Toast.LENGTH_SHORT).show();
                loadSampleData();
            }
        });
    }

    private void loadSampleData() {
        // Sample data for demonstration when server is not available
        java.util.List<FoodTruck> sampleTrucks = new java.util.ArrayList<>();

        sampleTrucks.add(new FoodTruck(1, "Mee Goreng Express", "Mee Goreng",
                "Delicious traditional Mee Goreng", 3.1390, 101.6869,
                "Ahmad", DateTimeUtils.getCurrentISOTime(), "", true));

        sampleTrucks.add(new FoodTruck(2, "Coffee on Wheels", "Coffee",
                "Fresh coffee and pastries", 3.1500, 101.7000,
                "Siti", DateTimeUtils.getCurrentISOTime(), "", true));

        sampleTrucks.add(new FoodTruck(3, "BBQ Paradise", "BBQ",
                "Grilled meat and vegetables", 3.1300, 101.6900,
                "Kumar", DateTimeUtils.getCurrentISOTime(), "", true));

        // Add more sample trucks for demonstration
        sampleTrucks.add(new FoodTruck(4, "Nasi Lemak King", "Nasi Lemak",
                "Best Nasi Lemak in town", 3.1450, 101.6950,
                "Aminah", DateTimeUtils.getCurrentISOTime(), "", true));
        sampleTrucks.add(new FoodTruck(5, "Dessert Delight", "Dessert",
                "Sweet treats and cakes", 3.1420, 101.6920,
                "Lim", DateTimeUtils.getCurrentISOTime(), "", true));
        sampleTrucks.add(new FoodTruck(6, "Drinks Hub", "Drinks",
                "Refreshing beverages", 3.1480, 101.6980,
                "Raj", DateTimeUtils.getCurrentISOTime(), "", true));

        allFoodTrucks = sampleTrucks;
        displayFoodTrucksOnMap(allFoodTrucks);
    }

    // Show all markers (reset filter)
    private void showAllMarkers() {
        displayFoodTrucksOnMap(allFoodTrucks);
    }

    private void displayFoodTrucksOnMap(List<FoodTruck> foodTrucks) {
        // Clear existing markers
        mMap.clear();
        markerFoodTruckMap.clear();

        for (FoodTruck truck : foodTrucks) {
            if (truck.isActive()) {
                LatLng position = new LatLng(truck.getLatitude(), truck.getLongitude());

                // Create custom marker based on food truck type
                BitmapDescriptor markerIcon = createCustomMarker(truck.getType());

                String snippet = "Type: " + truck.getType() + "\n" +
                        "Reported by: " + truck.getReportedBy() + "\n" +
                        "Time: " + DateTimeUtils.formatDateForDisplay(truck.getReportedAt());

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(truck.getName())
                        .snippet(snippet)
                        .icon(markerIcon));

                if (marker != null) {
                    markerFoodTruckMap.put(marker, truck);
                }
            }
        }
    }

    private BitmapDescriptor createCustomMarker(String foodType) {
        int color;
        switch (foodType.toLowerCase()) {
            case "mee goreng":
                color = Color.RED;
                break;
            case "coffee":
                color = Color.rgb(139, 69, 19); // Brown
                break;
            case "bbq":
                color = Color.rgb(255, 140, 0); // Orange
                break;
            case "dessert":
                color = Color.MAGENTA;
                break;
            case "drinks":
                color = Color.BLUE;
                break;
            default:
                color = Color.GREEN;
                break;
        }
        return BitmapDescriptorFactory.defaultMarker(getHueFromColor(color));
    }

    private float getHueFromColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv[0];
    }

    // Filter markers by type
    private void filterMarkersByType(String type) {
        if (mMap == null) return;
        mMap.clear();
        markerFoodTruckMap.clear();
        for (FoodTruck truck : allFoodTrucks) {
            if (truck.isActive() && truck.getType().equalsIgnoreCase(type)) {
                LatLng position = new LatLng(truck.getLatitude(), truck.getLongitude());
                BitmapDescriptor markerIcon = createCustomMarker(truck.getType());
                String snippet = "Type: " + truck.getType() + "\n" +
                        "Reported by: " + truck.getReportedBy() + "\n" +
                        "Time: " + DateTimeUtils.formatDateForDisplay(truck.getReportedAt());
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(truck.getName())
                        .snippet(snippet)
                        .icon(markerIcon));
                if (marker != null) {
                    markerFoodTruckMap.put(marker, truck);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding = null;
        }
    }
}