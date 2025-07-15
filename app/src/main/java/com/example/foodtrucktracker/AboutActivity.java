package com.example.foodtrucktracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("About");
        }

        // Set up clickable GitHub URL
        TextView githubUrl = findViewById(R.id.tv_github_url);
        githubUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/balqisstudy/FoodTruckTracker";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        // Set up developer email links
        TextView developer1Email = findViewById(R.id.tv_developer1_email);
        developer1Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:developer1@example.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Food Truck Tracker App Inquiry");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        TextView developer2Email = findViewById(R.id.tv_developer2_email);
        developer2Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:developer2@example.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Food Truck Tracker App Inquiry");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
