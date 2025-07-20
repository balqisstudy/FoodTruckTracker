package com.example.foodtrucktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.foodtrucktracker.models.FoodTruck;
import com.example.foodtrucktracker.network.ApiClient;
import com.example.foodtrucktracker.network.FoodTruckApiService;
import com.example.foodtrucktracker.utils.DateTimeUtils;
import com.example.foodtrucktracker.adapters.CustomInfoWindowAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

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

        // Set up toolbar
        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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

        // Set up button click handlers
        setupButtonClickHandlers();

        // Set up chip filters
        findViewById(R.id.chipMee).setOnClickListener(v -> filterMarkersByType("Mee Goreng"));
        findViewById(R.id.chipCoffee).setOnClickListener(v -> filterMarkersByType("Coffee"));
        findViewById(R.id.chipBBQ).setOnClickListener(v -> filterMarkersByType("BBQ"));
        findViewById(R.id.chipSatay).setOnClickListener(v -> filterMarkersByType("Satay"));
        findViewById(R.id.chipDrinks).setOnClickListener(v -> filterMarkersByType("Drinks"));
        // Reset filter when clicking outside chips
        findViewById(R.id.chipGroupFilters).setOnClickListener(v -> showAllMarkers());

        // Set up legend toggle
        setupLegendToggle();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set default location to Kuala Lumpur, Malaysia
        LatLng defaultLocation = new LatLng(3.139, 101.6869);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));

        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        
        // Enable other map controls
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false); // We have our own FAB
        
        // Enable gesture controls
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        // Enable location if permission is granted
        enableMyLocation();

        // Set up custom info window adapter
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this, markerFoodTruckMap));

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

        // Central KL - Bukit Bintang Area
        sampleTrucks.add(new FoodTruck(1, "Mee Goreng Express", "Mee Goreng",
                "Delicious traditional Mee Goreng", 3.1390, 101.6869,
                "Ahmad", DateTimeUtils.getCurrentISOTime(), "", true,
                "Bukit Bintang", "Near Pavilion Mall", "Jalan Bukit Bintang", "6:00 PM - 11:00 PM", "012-3456789"));

        // KLCC Area
        sampleTrucks.add(new FoodTruck(2, "Coffee on Wheels", "Coffee",
                "Fresh coffee and pastries", 3.1500, 101.7000,
                "Siti", DateTimeUtils.getCurrentISOTime(), "", true,
                "KLCC", "Near Petronas Twin Towers", "Jalan Ampang", "7:00 AM - 3:00 PM", "012-3456790"));

        // Petaling Street Area
        sampleTrucks.add(new FoodTruck(3, "BBQ Paradise", "BBQ",
                "Grilled meat and vegetables", 3.1300, 101.6900,
                "Kumar", DateTimeUtils.getCurrentISOTime(), "", true,
                "Petaling Street", "Near Central Market", "Jalan Petaling", "6:00 PM - 12:00 AM", "012-3456791"));

        // Bangsar Area
        sampleTrucks.add(new FoodTruck(4, "Nasi Lemak King", "Nasi Lemak",
                "Best Nasi Lemak in town", 3.1450, 101.6950,
                "Aminah", DateTimeUtils.getCurrentISOTime(), "", true,
                "Bangsar", "Near Bangsar Village", "Jalan Telawi", "6:00 AM - 10:00 AM", "012-3456792"));
        
        // Damansara Area
        sampleTrucks.add(new FoodTruck(5, "Dessert Delight", "Dessert",
                "Sweet treats and cakes", 3.1420, 101.6920,
                "Lim", DateTimeUtils.getCurrentISOTime(), "", true,
                "Damansara", "Near One Utama", "Jalan Bandar Utama", "2:00 PM - 10:00 PM", "012-3456793"));
        
        // Mont Kiara Area
        sampleTrucks.add(new FoodTruck(6, "Drinks Hub", "Drinks",
                "Refreshing beverages", 3.1480, 101.6980,
                "Raj", DateTimeUtils.getCurrentISOTime(), "", true,
                "Mont Kiara", "Near Solaris Dutamas", "Jalan Solaris", "11:00 AM - 9:00 PM", "012-3456794"));

        // Cheras Area
        sampleTrucks.add(new FoodTruck(7, "Satay Master", "Satay",
                "Authentic Malaysian satay", 3.0800, 101.7200,
                "Hassan", DateTimeUtils.getCurrentISOTime(), "", true,
                "Cheras", "Near Taman Connaught", "Jalan Cheras", "5:00 PM - 11:00 PM", "012-3456795"));

        // Kepong Area
        sampleTrucks.add(new FoodTruck(8, "Rojak Delight", "Rojak",
                "Fresh fruit and vegetable rojak", 3.2100, 101.6400,
                "Mei Ling", DateTimeUtils.getCurrentISOTime(), "", true,
                "Kepong", "Near Kepong Mall", "Jalan Kepong", "4:00 PM - 10:00 PM", "012-3456796"));

        // Wangsa Maju Area
        sampleTrucks.add(new FoodTruck(9, "Ice Cream Paradise", "Ice Cream",
                "Homemade ice cream varieties", 3.1800, 101.7500,
                "Sarah", DateTimeUtils.getCurrentISOTime(), "", true,
                "Wangsa Maju", "Near Wangsa Walk", "Jalan Wangsa Delima", "1:00 PM - 9:00 PM", "012-3456797"));

        // Setapak Area
        sampleTrucks.add(new FoodTruck(10, "Nasi Lemak Express", "Nasi Lemak",
                "Traditional nasi lemak with sambal", 3.2000, 101.7000,
                "Zainab", DateTimeUtils.getCurrentISOTime(), "", true,
                "Setapak", "Near TAR University", "Jalan Genting Klang", "6:00 AM - 12:00 PM", "012-3456798"));

        // Gombak Area
        sampleTrucks.add(new FoodTruck(11, "Mamak Corner", "Other",
                "Indian Muslim cuisine", 3.2500, 101.6800,
                "Ravi", DateTimeUtils.getCurrentISOTime(), "", true,
                "Gombak", "Near Gombak LRT", "Jalan Gombak", "24 Hours", "012-3456799"));

        // Selayang Area
        sampleTrucks.add(new FoodTruck(12, "Durian King", "Other",
                "Fresh durian and local fruits", 3.2800, 101.6500,
                "Ah Chong", DateTimeUtils.getCurrentISOTime(), "", true,
                "Selayang", "Near Selayang Mall", "Jalan Selayang", "10:00 AM - 8:00 PM", "012-3456800"));

        // Ampang Area
        sampleTrucks.add(new FoodTruck(13, "Teh Tarik Corner", "Drinks",
                "Traditional Malaysian teh tarik", 3.1600, 101.7600,
                "Khalid", DateTimeUtils.getCurrentISOTime(), "", true,
                "Ampang", "Near Ampang Point", "Jalan Ampang", "7:00 AM - 11:00 PM", "012-3456801"));

        // Sri Hartamas Area
        sampleTrucks.add(new FoodTruck(14, "Sushi Express", "Other",
                "Fresh sushi and Japanese cuisine", 3.1700, 101.6500,
                "Yuki", DateTimeUtils.getCurrentISOTime(), "", true,
                "Sri Hartamas", "Near Hartamas Shopping Centre", "Jalan Sri Hartamas", "11:00 AM - 10:00 PM", "012-3456802"));

        // TTDI Area
        sampleTrucks.add(new FoodTruck(15, "Pasta Mobile", "Other",
                "Italian pasta and pizza", 3.1400, 101.6400,
                "Marco", DateTimeUtils.getCurrentISOTime(), "", true,
                "TTDI", "Near TTDI Park", "Jalan Tun Dr Ismail", "6:00 PM - 11:00 PM", "012-3456803"));

        // Mutiara Damansara Area
        sampleTrucks.add(new FoodTruck(16, "Burger Joint", "Other",
                "Gourmet burgers and fries", 3.1500, 101.5800,
                "Mike", DateTimeUtils.getCurrentISOTime(), "", true,
                "Mutiara Damansara", "Near The Curve", "Jalan PJU 7/3", "12:00 PM - 12:00 AM", "012-3456804"));

        // Bandar Utama Area
        sampleTrucks.add(new FoodTruck(17, "Smoothie Bar", "Drinks",
                "Fresh fruit smoothies", 3.1400, 101.5900,
                "Lisa", DateTimeUtils.getCurrentISOTime(), "", true,
                "Bandar Utama", "Near 1 Utama", "Jalan Bandar Utama", "10:00 AM - 8:00 PM", "012-3456805"));

        // Sunway Area
        sampleTrucks.add(new FoodTruck(18, "Korean BBQ", "BBQ",
                "Korean barbecue and kimchi", 3.0700, 101.6000,
                "Ji Eun", DateTimeUtils.getCurrentISOTime(), "", true,
                "Sunway", "Near Sunway Pyramid", "Jalan PJS 11/15", "5:00 PM - 11:00 PM", "012-3456806"));

        // Puchong Area
        sampleTrucks.add(new FoodTruck(19, "Dim Sum Express", "Other",
                "Steamed dim sum and tea", 3.0200, 101.6200,
                "Wong", DateTimeUtils.getCurrentISOTime(), "", true,
                "Puchong", "Near IOI Mall Puchong", "Jalan Puchong", "7:00 AM - 3:00 PM", "012-3456807"));

        // Seri Kembangan Area
        sampleTrucks.add(new FoodTruck(20, "Roti Canai Master", "Other",
                "Flaky roti canai with curry", 3.0300, 101.7000,
                "Maniam", DateTimeUtils.getCurrentISOTime(), "", true,
                "Seri Kembangan", "Near South City Plaza", "Jalan Serdang", "6:00 AM - 2:00 PM", "012-3456808"));

        // Kajang Area
        sampleTrucks.add(new FoodTruck(21, "Satay Kajang", "Satay",
                "Famous Kajang satay", 2.9900, 101.7900,
                "Pak Mat", DateTimeUtils.getCurrentISOTime(), "", true,
                "Kajang", "Near Kajang Town Centre", "Jalan Kajang", "5:00 PM - 12:00 AM", "012-3456809"));

        // Semenyih Area
        sampleTrucks.add(new FoodTruck(22, "Durian Farm", "Other",
                "Fresh durian from farm", 2.9500, 101.8500,
                "Ah Seng", DateTimeUtils.getCurrentISOTime(), "", true,
                "Semenyih", "Near Semenyih Town", "Jalan Semenyih", "9:00 AM - 6:00 PM", "012-3456810"));

        // Cyberjaya Area
        sampleTrucks.add(new FoodTruck(23, "Tech Food Hub", "Other",
                "Modern fusion cuisine", 2.9200, 101.6500,
                "Alex", DateTimeUtils.getCurrentISOTime(), "", true,
                "Cyberjaya", "Near Cyberjaya Town Centre", "Persiaran Cyberjaya", "11:00 AM - 9:00 PM", "012-3456811"));

        // Putrajaya Area
        sampleTrucks.add(new FoodTruck(24, "Government Canteen", "Other",
                "Local Malaysian cuisine", 2.9400, 101.6900,
                "Ahmad", DateTimeUtils.getCurrentISOTime(), "", true,
                "Putrajaya", "Near Putrajaya Centre", "Jalan Putrajaya", "7:00 AM - 5:00 PM", "012-3456812"));

        // Shah Alam Area
        sampleTrucks.add(new FoodTruck(25, "Shah Alam Delights", "Other",
                "Local Shah Alam specialties", 3.0800, 101.5200,
                "Siti", DateTimeUtils.getCurrentISOTime(), "", true,
                "Shah Alam", "Near Shah Alam City Centre", "Jalan Shah Alam", "6:00 PM - 11:00 PM", "012-3456813"));

        allFoodTrucks = sampleTrucks;
        displayFoodTrucksOnMap(allFoodTrucks);
    }

    // Show all markers (reset filter)
    private void showAllMarkers() {
        displayFoodTrucksOnMap(allFoodTrucks);
    }

    // Fit all markers in view
    private void fitAllMarkers() {
        if (allFoodTrucks.isEmpty()) {
            return;
        }

        // Create a LatLngBounds builder
        com.google.android.gms.maps.model.LatLngBounds.Builder builder = new com.google.android.gms.maps.model.LatLngBounds.Builder();
        
        // Add all food truck locations to the bounds
        for (FoodTruck truck : allFoodTrucks) {
            if (truck.isActive()) {
                builder.include(new LatLng(truck.getLatitude(), truck.getLongitude()));
            }
        }
        
        // Build the bounds
        com.google.android.gms.maps.model.LatLngBounds bounds = builder.build();
        
        // Animate camera to show all markers with padding
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        
        Toast.makeText(this, "Showing all food trucks", Toast.LENGTH_SHORT).show();
    }

    private void showSearchDialog() {
        // Create dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_search, null);
        builder.setView(dialogView);

        // Get dialog views
        com.google.android.material.textfield.TextInputEditText etSearch = dialogView.findViewById(R.id.et_search);
        com.google.android.material.chip.Chip chipMee = dialogView.findViewById(R.id.chip_search_mee);
        com.google.android.material.chip.Chip chipCoffee = dialogView.findViewById(R.id.chip_search_coffee);
        com.google.android.material.chip.Chip chipBBQ = dialogView.findViewById(R.id.chip_search_bbq);
        com.google.android.material.chip.Chip chipSatay = dialogView.findViewById(R.id.chip_search_satay);
        com.google.android.material.chip.Chip chipDrinks = dialogView.findViewById(R.id.chip_search_drinks);
        com.google.android.material.chip.Chip chipBukitBintang = dialogView.findViewById(R.id.chip_search_bukit_bintang);
        com.google.android.material.chip.Chip chipKLCC = dialogView.findViewById(R.id.chip_search_klcc);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSearch = dialogView.findViewById(R.id.btn_search);

        // Create dialog
        android.app.AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        // Set up chip click listeners
        chipMee.setOnClickListener(v -> {
            etSearch.setText("Mee Goreng");
            chipMee.setChecked(true);
            chipCoffee.setChecked(false);
            chipBBQ.setChecked(false);
            chipSatay.setChecked(false);
            chipDrinks.setChecked(false);
            chipBukitBintang.setChecked(false);
            chipKLCC.setChecked(false);
        });

        chipCoffee.setOnClickListener(v -> {
            etSearch.setText("Coffee");
            chipMee.setChecked(false);
            chipCoffee.setChecked(true);
            chipBBQ.setChecked(false);
            chipSatay.setChecked(false);
            chipDrinks.setChecked(false);
            chipBukitBintang.setChecked(false);
            chipKLCC.setChecked(false);
        });

        chipBBQ.setOnClickListener(v -> {
            etSearch.setText("BBQ");
            chipMee.setChecked(false);
            chipCoffee.setChecked(false);
            chipBBQ.setChecked(true);
            chipSatay.setChecked(false);
            chipDrinks.setChecked(false);
            chipBukitBintang.setChecked(false);
            chipKLCC.setChecked(false);
        });

        chipSatay.setOnClickListener(v -> {
            etSearch.setText("Satay");
            chipMee.setChecked(false);
            chipCoffee.setChecked(false);
            chipBBQ.setChecked(false);
            chipSatay.setChecked(true);
            chipDrinks.setChecked(false);
            chipBukitBintang.setChecked(false);
            chipKLCC.setChecked(false);
        });

        chipDrinks.setOnClickListener(v -> {
            etSearch.setText("Drinks");
            chipMee.setChecked(false);
            chipCoffee.setChecked(false);
            chipBBQ.setChecked(false);
            chipSatay.setChecked(false);
            chipDrinks.setChecked(true);
            chipBukitBintang.setChecked(false);
            chipKLCC.setChecked(false);
        });

        chipBukitBintang.setOnClickListener(v -> {
            etSearch.setText("Bukit Bintang");
            chipMee.setChecked(false);
            chipCoffee.setChecked(false);
            chipBBQ.setChecked(false);
            chipSatay.setChecked(false);
            chipDrinks.setChecked(false);
            chipBukitBintang.setChecked(true);
            chipKLCC.setChecked(false);
        });

        chipKLCC.setOnClickListener(v -> {
            etSearch.setText("KLCC");
            chipMee.setChecked(false);
            chipCoffee.setChecked(false);
            chipBBQ.setChecked(false);
            chipSatay.setChecked(false);
            chipDrinks.setChecked(false);
            chipBukitBintang.setChecked(false);
            chipKLCC.setChecked(true);
        });

        // Set up button click listeners
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSearch.setOnClickListener(v -> {
            String searchQuery = etSearch.getText().toString().trim();
            if (!searchQuery.isEmpty()) {
                performSearch(searchQuery);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });

        // Show dialog
        dialog.show();
    }

    private void performSearch(String query) {
        if (allFoodTrucks.isEmpty()) {
            Toast.makeText(this, "No food trucks available to search", Toast.LENGTH_SHORT).show();
            return;
        }

        List<FoodTruck> searchResults = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (FoodTruck truck : allFoodTrucks) {
            if (truck.isActive()) {
                // Search in name
                if (truck.getName().toLowerCase().contains(lowerQuery)) {
                    searchResults.add(truck);
                    continue;
                }
                
                // Search in type
                if (truck.getType().toLowerCase().contains(lowerQuery)) {
                    searchResults.add(truck);
                    continue;
                }
                
                // Search in area
                if (truck.getArea() != null && truck.getArea().toLowerCase().contains(lowerQuery)) {
                    searchResults.add(truck);
                    continue;
                }
                
                // Search in landmark
                if (truck.getLandmark() != null && truck.getLandmark().toLowerCase().contains(lowerQuery)) {
                    searchResults.add(truck);
                    continue;
                }
                
                // Search in description
                if (truck.getDescription().toLowerCase().contains(lowerQuery)) {
                    searchResults.add(truck);
                    continue;
                }
            }
        }

        if (searchResults.isEmpty()) {
            Toast.makeText(this, "No food trucks found for: " + query, Toast.LENGTH_SHORT).show();
            // Show all markers when no results found
            displayFoodTrucksOnMap(allFoodTrucks);
        } else {
            displayFoodTrucksOnMap(searchResults);
            Toast.makeText(this, "Found " + searchResults.size() + " food truck(s) for: " + query, Toast.LENGTH_SHORT).show();
            
            // Fit search results in view
            if (mMap != null && !searchResults.isEmpty()) {
                fitSearchResultsInView(searchResults);
            }
        }
    }

    private void fitSearchResultsInView(List<FoodTruck> searchResults) {
        if (searchResults.isEmpty()) {
            return;
        }

        // Create a LatLngBounds builder
        com.google.android.gms.maps.model.LatLngBounds.Builder builder = new com.google.android.gms.maps.model.LatLngBounds.Builder();
        
        // Add all search result locations to the bounds
        for (FoodTruck truck : searchResults) {
            if (truck.isActive()) {
                builder.include(new LatLng(truck.getLatitude(), truck.getLongitude()));
            }
        }
        
        // Build the bounds
        com.google.android.gms.maps.model.LatLngBounds bounds = builder.build();
        
        // Animate camera to show search results with padding
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
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

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(truck.getName())
                        .icon(markerIcon));

                if (marker != null) {
                    markerFoodTruckMap.put(marker, truck);
                }
            }
        }
    }

    private BitmapDescriptor createCustomMarker(String foodType) {
        int color;
        
        // Simplified Food Types with Marker Colors
        // Fried Dishes - Yellow #FFD700
        if (foodType.toLowerCase().contains("mee goreng") ||
            foodType.toLowerCase().contains("maggi goreng") ||
            foodType.toLowerCase().contains("laksa") ||
            foodType.toLowerCase().contains("mee rebus") ||
            foodType.toLowerCase().contains("curry mee") ||
            foodType.toLowerCase().contains("seafood noodle") ||
            foodType.toLowerCase().contains("nasi lemak") ||
            foodType.toLowerCase().contains("nasi goreng") ||
            foodType.toLowerCase().contains("nasi ayam") ||
            foodType.toLowerCase().contains("bento bowl") ||
            foodType.toLowerCase().contains("nasi tomato") ||
            foodType.toLowerCase().contains("nasi hujan panas")) {
            color = Color.parseColor("#FFD700"); // Yellow for fried dishes
        }
        // Grilled / BBQ - Red #FF4500
        else if (foodType.toLowerCase().contains("satay") ||
                 foodType.toLowerCase().contains("bbq stick") ||
                 foodType.toLowerCase().contains("yakitori") ||
                 foodType.toLowerCase().contains("grilled vegetable")) {
            color = Color.parseColor("#FF4500"); // Red for grilled/BBQ
        }
        // Western Food - Dark Red #8B0000
        else if (foodType.toLowerCase().contains("pizza") ||
                 foodType.toLowerCase().contains("chicken wings") ||
                 foodType.toLowerCase().contains("fries") ||
                 foodType.toLowerCase().contains("nuggets") ||
                 foodType.toLowerCase().contains("pasta") ||
                 foodType.toLowerCase().contains("burger") ||
                 foodType.toLowerCase().contains("slider") ||
                 foodType.toLowerCase().contains("wrap") ||
                 foodType.toLowerCase().contains("shawarma") ||
                 foodType.toLowerCase().contains("sandwich") ||
                 foodType.toLowerCase().contains("grilled cheese")) {
            color = Color.parseColor("#8B0000"); // Dark red for western food
        }
        // Asian Cuisine - Purple #9370DB
        else if (foodType.toLowerCase().contains("korean fried chicken") ||
                 foodType.toLowerCase().contains("middle eastern") ||
                 foodType.toLowerCase().contains("thai fusion") ||
                 foodType.toLowerCase().contains("indonesian fusion") ||
                 foodType.toLowerCase().contains("takoyaki") ||
                 foodType.toLowerCase().contains("onigiri") ||
                 foodType.toLowerCase().contains("gyoza") ||
                 foodType.toLowerCase().contains("tteokbokki") ||
                 foodType.toLowerCase().contains("spring roll")) {
            color = Color.parseColor("#9370DB"); // Purple for Asian cuisine
        }
        // Traditional / Local - Dark Orange #FF8C00
        else if (foodType.toLowerCase().contains("lontong") ||
                 foodType.toLowerCase().contains("soto") ||
                 foodType.toLowerCase().contains("laksa johor") ||
                 foodType.toLowerCase().contains("ayam percik") ||
                 foodType.toLowerCase().contains("keropok lekor") ||
                 foodType.toLowerCase().contains("cendol") ||
                 foodType.toLowerCase().contains("ais kacang")) {
            color = Color.parseColor("#FF8C00"); // Dark orange for traditional/local
        }
        // Desserts & Sweets - Pink #FF69B4
        else if (foodType.toLowerCase().contains("cake") ||
                 foodType.toLowerCase().contains("cupcake") ||
                 foodType.toLowerCase().contains("cheesecake") ||
                 foodType.toLowerCase().contains("brownie") ||
                 foodType.toLowerCase().contains("donut") ||
                 foodType.toLowerCase().contains("waffle") ||
                 foodType.toLowerCase().contains("crepe") ||
                 foodType.toLowerCase().contains("ice cream") ||
                 foodType.toLowerCase().contains("sundae")) {
            color = Color.parseColor("#FF69B4"); // Pink for desserts & sweets
        }
        // Fruits - Green #32CD32
        else if (foodType.toLowerCase().contains("healthy bowl") ||
                 foodType.toLowerCase().contains("vegetarian") ||
                 foodType.toLowerCase().contains("vegan snack") ||
                 foodType.toLowerCase().contains("fresh juice") ||
                 foodType.toLowerCase().contains("smoothie") ||
                 foodType.toLowerCase().contains("detox") ||
                 foodType.toLowerCase().contains("rojak")) {
            color = Color.parseColor("#32CD32"); // Green for fruits
        }
        // Seafood - Teal #20B2AA
        else if (foodType.toLowerCase().contains("seafood")) {
            color = Color.parseColor("#20B2AA"); // Teal for seafood
        }
        // Street Food - Deep Blue #4169E1
        else if (foodType.toLowerCase().contains("mamak") ||
                 foodType.toLowerCase().contains("street food") ||
                 foodType.toLowerCase().contains("food combo") ||
                 foodType.toLowerCase().contains("family meal") ||
                 foodType.toLowerCase().contains("mini snack") ||
                 foodType.toLowerCase().contains("fusion menu")) {
            color = Color.parseColor("#4169E1"); // Deep blue for street food
        }
        // Drink Types with Marker Colors
        // Coffee - Coffee Brown #6F4E37
        else if (foodType.toLowerCase().contains("coffee") ||
                 foodType.toLowerCase().contains("espresso") ||
                 foodType.toLowerCase().contains("latte") ||
                 foodType.toLowerCase().contains("cappuccino") ||
                 foodType.toLowerCase().contains("mocha") ||
                 foodType.toLowerCase().contains("cold brew")) {
            color = Color.parseColor("#6F4E37"); // Coffee brown for coffee
        }
        // Non-Coffee & Tea - Matcha Green #98FB98
        else if (foodType.toLowerCase().contains("matcha") ||
                 foodType.toLowerCase().contains("tea") ||
                 foodType.toLowerCase().contains("iced lemon tea") ||
                 foodType.toLowerCase().contains("fruit tea")) {
            color = Color.parseColor("#98FB98"); // Matcha green for non-coffee & tea
        }
        // Beverage - Sky Blue #00BFFF
        else if (foodType.toLowerCase().contains("boba tea") ||
                 foodType.toLowerCase().contains("fizzy drinks") ||
                 foodType.toLowerCase().contains("soda") ||
                 foodType.toLowerCase().contains("coconut shake") ||
                 foodType.toLowerCase().contains("juice")) {
            color = Color.parseColor("#00BFFF"); // Sky blue for beverage
        }
        // Default
        else {
            color = Color.GRAY;
        }
        
        return BitmapDescriptorFactory.defaultMarker(getHueFromColor(color));
    }

    private float getHueFromColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv[0];
    }

    private void filterMarkersByType(String type) {
        if (mMap == null) return;
        mMap.clear();
        markerFoodTruckMap.clear();
        for (FoodTruck truck : allFoodTrucks) {
            if (truck.isActive() && truck.getType().equalsIgnoreCase(type)) {
                LatLng position = new LatLng(truck.getLatitude(), truck.getLongitude());
                BitmapDescriptor markerIcon = createCustomMarker(truck.getType());
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(truck.getName())
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

    private void setupButtonClickHandlers() {
        // Yellow "+" button - Add Food Truck (now on left side)
        findViewById(R.id.fab_add_data).setOnClickListener(v -> {
            Intent intent = new Intent(MapsActivity.this, InsertDataActivity.class);
            startActivity(intent);
        });
    }

    private void setupLegendToggle() {
        View legendToggle = findViewById(R.id.legend_toggle);
        View legendContent = findViewById(R.id.legend_content);
        
        legendToggle.setOnClickListener(v -> {
            if (legendContent.getVisibility() == View.VISIBLE) {
                // Collapse legend with animation
                Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        legendContent.setVisibility(View.GONE);
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                legendContent.startAnimation(fadeOut);
                legendToggle.setBackgroundResource(R.drawable.ic_arrow_down);
            } else {
                // Expand legend with animation
                legendContent.setVisibility(View.VISIBLE);
                Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                legendContent.startAnimation(fadeIn);
                legendToggle.setBackgroundResource(R.drawable.ic_arrow_up);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == android.R.id.home) {
            // Back button - go to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId == R.id.action_search) {
            // Show search dialog
            showSearchDialog();
            return true;
        } else if (itemId == R.id.action_refresh) {
            // Refresh food trucks
            loadFoodTrucks();
            Toast.makeText(this, "Refreshing food trucks...", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_settings) {
            // Settings - you can implement settings here
            Toast.makeText(this, "Settings coming soon", Toast.LENGTH_SHORT).show();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding = null;
        }
    }
}