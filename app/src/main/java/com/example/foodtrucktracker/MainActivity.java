package com.example.foodtrucktracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Declare your CheckBox variables here
    private CheckBox checkboxAcceptsOffers1;
    private CheckBox checkboxAcceptsOffers2;
    private CheckBox checkboxAccessibility;
    private CheckBox checkboxBirthdayParties;
    private CheckBox checkboxBreakfast;
    private CheckBox checkboxBusTruckParking;
    private CheckBox checkboxChildrenParties;
    private CheckBox checkboxContactless;
    private CheckBox checkboxCreditCard;
    private CheckBox checkboxDebitCard;
    private CheckBox checkboxDriveThru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Links this Java code to your XML layout

        // 1. Find all the UI elements by their IDs from activity_main.xml
        ImageButton backButton = findViewById(R.id.backButton);
        Button clearButton = findViewById(R.id.clearButton);
        Button applyFilterButton = findViewById(R.id.applyFilterButton);

        // Initialize all checkbox variables
        checkboxAcceptsOffers1 = findViewById(R.id.checkbox_accepts_offers_1);
        checkboxAcceptsOffers2 = findViewById(R.id.checkbox_accepts_offers_2);
        checkboxAccessibility = findViewById(R.id.checkbox_accessibility);
        checkboxBirthdayParties = findViewById(R.id.checkbox_birthday_parties);
        checkboxBreakfast = findViewById(R.id.checkbox_breakfast);
        checkboxBusTruckParking = findViewById(R.id.checkbox_bus_truck_parking);
        checkboxChildrenParties = findViewById(R.id.checkbox_children_parties);
        checkboxContactless = findViewById(R.id.checkbox_contactless);
        checkboxCreditCard = findViewById(R.id.checkbox_credit_card);
        checkboxDebitCard = findViewById(R.id.checkbox_debit_card);
        checkboxDriveThru = findViewById(R.id.checkbox_drive_thru);

        // 2. Set up click listeners for the buttons

        backButton.setOnClickListener(v -> {
            // This simulates pressing the system back button
            onBackPressed();
        });

        clearButton.setOnClickListener(v -> {
            // Loop through all checkboxes and set them to unchecked
            checkboxAcceptsOffers1.setChecked(false);
            checkboxAcceptsOffers2.setChecked(false);
            checkboxAccessibility.setChecked(false);
            checkboxBirthdayParties.setChecked(false);
            checkboxBreakfast.setChecked(false); // Even if it was checked by default
            checkboxBusTruckParking.setChecked(false);
            checkboxChildrenParties.setChecked(false);
            checkboxContactless.setChecked(false);
            checkboxCreditCard.setChecked(false);
            checkboxDebitCard.setChecked(false);
            checkboxDriveThru.setChecked(false);

            Toast.makeText(MainActivity.this, "Filters Cleared!", Toast.LENGTH_SHORT).show();
        });

        applyFilterButton.setOnClickListener(v -> {
            // 3. Collect the state of all checkboxes

            List<String> selectedFilters = new ArrayList<>();

            if (checkboxAcceptsOffers1.isChecked()) selectedFilters.add(getString(R.string.accepts_offers));
            if (checkboxAcceptsOffers2.isChecked()) selectedFilters.add(getString(R.string.accepts_offers));
            if (checkboxAccessibility.isChecked()) selectedFilters.add(getString(R.string.accessibility));
            if (checkboxBirthdayParties.isChecked()) selectedFilters.add(getString(R.string.birthday_parties));
            if (checkboxBreakfast.isChecked()) selectedFilters.add(getString(R.string.breakfast));
            if (checkboxBusTruckParking.isChecked()) selectedFilters.add(getString(R.string.bus_truck_parking));
            if (checkboxChildrenParties.isChecked()) selectedFilters.add(getString(R.string.children_parties));
            if (checkboxContactless.isChecked()) selectedFilters.add(getString(R.string.contactless));
            if (checkboxCreditCard.isChecked()) selectedFilters.add(getString(R.string.credit_card));
            if (checkboxDebitCard.isChecked()) selectedFilters.add(getString(R.string.debit_card));
            if (checkboxDriveThru.isChecked()) selectedFilters.add(getString(R.string.drive_thru));

            // For demonstration, we'll just show a Toast message with selected filters
            if (!selectedFilters.isEmpty()) {
                Toast.makeText(MainActivity.this, "Applying filters: " + String.join(", ", selectedFilters), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "No filters selected.", Toast.LENGTH_SHORT).show();
            }

            // In a real app, you would likely:
            // 1. Pass these selectedFilters back to the previous Activity/Fragment
            //    (e.g., using Intents and setResult)
            // 2. Perform a search/filter operation based on the selections.
            // 3. Close this filter screen (optional, but common).
            // finish(); // Uncomment this line if you want to close the activity after applying filters
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Initialize buttons
        Button btnViewMap = findViewById(R.id.btn_view_map);
        Button btnAddTruck = findViewById(R.id.btn_add_truck);
        Button btnAbout = findViewById(R.id.btn_about);

        // Set button listeners
        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        btnAddTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InsertDataActivity.class));
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    //add menu to the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //add menuitem optionselected
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuMAp) {
            //open map activity
            startActivity(new Intent(this, MapsActivity.class));
            return true;
        } else if (itemId == R.id.menu_add_truck) {
            startActivity(new Intent(this, InsertDataActivity.class));
            return true;
        } else if (itemId == R.id.menu_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}