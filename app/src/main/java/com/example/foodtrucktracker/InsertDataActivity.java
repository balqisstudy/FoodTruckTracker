package com.example.foodtrucktracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.foodtrucktracker.models.FoodTruck;
import com.example.foodtrucktracker.network.ApiClient;
import com.example.foodtrucktracker.network.FoodTruckApiService;
import com.example.foodtrucktracker.utils.DateTimeUtils;
import com.example.foodtrucktracker.utils.LocationHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertDataActivity extends AppCompatActivity implements LocationHelper.LocationListener {

    private EditText etName, etDescription, etLatitude, etLongitude, etReportedBy;
    private EditText etArea, etLandmark, etStreetAddress, etOperatingHours, etContactNumber;
    private Spinner spinnerType;
    private Button btnGetLocation, btnSubmit;
    private LocationHelper locationHelper;
    private FoodTruckApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Food Truck");
        }

        // Initialize views
        initViews();

        // Initialize helpers and services
        locationHelper = new LocationHelper(this);
        apiService = ApiClient.getFoodTruckApiService();

        // Set up listeners
        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFoodTruck();
            }
        });
    }

    private void initViews() {
        etName = findViewById(R.id.et_name);
        etDescription = findViewById(R.id.et_description);
        etLatitude = findViewById(R.id.et_latitude);
        etLongitude = findViewById(R.id.et_longitude);
        etReportedBy = findViewById(R.id.et_reported_by);
        etArea = findViewById(R.id.et_area);
        etLandmark = findViewById(R.id.et_landmark);
        etStreetAddress = findViewById(R.id.et_street_address);
        etOperatingHours = findViewById(R.id.et_operating_hours);
        etContactNumber = findViewById(R.id.et_contact_number);
        spinnerType = findViewById(R.id.spinner_type);
        btnGetLocation = findViewById(R.id.btn_get_location);
        btnSubmit = findViewById(R.id.btn_submit);

        // Set up spinner with food truck types
        String[] foodTypes = {"Mee Goreng", "Coffee", "BBQ", "Dessert", "Drinks", "Nasi Lemak", "Rojak", "Satay", "Ice Cream", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, foodTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    private void getCurrentLocation() {
        if (locationHelper.checkLocationPermission()) {
            locationHelper.getCurrentLocation(this);
        } else {
            locationHelper.requestLocationPermission(this);
        }
    }

    @Override
    public void onLocationReceived(double latitude, double longitude) {
        etLatitude.setText(String.valueOf(latitude));
        etLongitude.setText(String.valueOf(longitude));
        Toast.makeText(this, "Location updated: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationError(String error) {
        Toast.makeText(this, "Location error: " + error, Toast.LENGTH_LONG).show();
        // Set default location (Kuala Lumpur) as fallback
        etLatitude.setText("3.139");
        etLongitude.setText("101.6869");
        Toast.makeText(this, "Using default location (Kuala Lumpur). You can manually edit coordinates.", Toast.LENGTH_LONG).show();
    }

    private void submitFoodTruck() {
        // Validate input
        if (!validateInput()) {
            return;
        }

        // Create FoodTruck object
        FoodTruck foodTruck = new FoodTruck();
        foodTruck.setName(etName.getText().toString().trim());
        foodTruck.setType(spinnerType.getSelectedItem().toString());
        foodTruck.setDescription(etDescription.getText().toString().trim());
        foodTruck.setLatitude(Double.parseDouble(etLatitude.getText().toString().trim()));
        foodTruck.setLongitude(Double.parseDouble(etLongitude.getText().toString().trim()));
        foodTruck.setReportedBy(etReportedBy.getText().toString().trim());
        foodTruck.setReportedAt(DateTimeUtils.getCurrentISOTime());
        foodTruck.setActive(true);
        foodTruck.setArea(etArea.getText().toString().trim());
        foodTruck.setLandmark(etLandmark.getText().toString().trim());
        foodTruck.setStreetAddress(etStreetAddress.getText().toString().trim());
        foodTruck.setOperatingHours(etOperatingHours.getText().toString().trim());
        foodTruck.setContactNumber(etContactNumber.getText().toString().trim());

        // Submit to server
        Call<FoodTruck> call = apiService.createFoodTruck(foodTruck);
        call.enqueue(new Callback<FoodTruck>() {
            @Override
            public void onResponse(Call<FoodTruck> call, Response<FoodTruck> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InsertDataActivity.this, "Food truck added successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity
                } else {
                    Toast.makeText(InsertDataActivity.this, "Failed to add food truck", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FoodTruck> call, Throwable t) {
                Toast.makeText(InsertDataActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateInput() {
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Name is required");
            return false;
        }

        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Description is required");
            return false;
        }

        if (etLatitude.getText().toString().trim().isEmpty()) {
            etLatitude.setError("Latitude is required");
            return false;
        }

        if (etLongitude.getText().toString().trim().isEmpty()) {
            etLongitude.setError("Longitude is required");
            return false;
        }

        if (etReportedBy.getText().toString().trim().isEmpty()) {
            etReportedBy.setError("Reporter name is required");
            return false;
        }

        if (etArea.getText().toString().trim().isEmpty()) {
            etArea.setError("Area is required");
            return false;
        }

        if (etLandmark.getText().toString().trim().isEmpty()) {
            etLandmark.setError("Landmark is required");
            return false;
        }

        if (etStreetAddress.getText().toString().trim().isEmpty()) {
            etStreetAddress.setError("Street address is required");
            return false;
        }

        if (etOperatingHours.getText().toString().trim().isEmpty()) {
            etOperatingHours.setError("Operating hours are required");
            return false;
        }

        try {
            Double.parseDouble(etLatitude.getText().toString().trim());
            Double.parseDouble(etLongitude.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid latitude or longitude format", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationHelper.getLocationPermissionRequestCode()) {
            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
