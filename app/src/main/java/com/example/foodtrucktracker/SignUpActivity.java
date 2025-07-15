package com.example.foodtrucktracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextSignUpEmail;
    private EditText editTextSignUpPassword;
    private Button buttonSignUp;
    private Button buttonFacebook;
    private Button buttonGoogle;
    private CheckBox checkBoxPrivacy;
    private ImageButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements by finding them by their IDs
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextSignUpEmail = findViewById(R.id.editTextSignUpEmail);
        editTextSignUpPassword = findViewById(R.id.editTextSignUpPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonFacebook = findViewById(R.id.buttonFacebook);
        buttonGoogle = findViewById(R.id.buttonGoogle);
        checkBoxPrivacy = findViewById(R.id.checkBoxPrivacy);
        backButton = findViewById(R.id.backButton);


        // Set an OnClickListener for the Sign Up button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text input from the email, password, and confirm password fields
                String username = editTextUsername.getText().toString().trim();
                String email = editTextSignUpEmail.getText().toString().trim();
                String password = editTextSignUpPassword.getText().toString().trim();

                // Perform basic validation checks
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    // Show a toast message if any field is empty
                    Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!checkBoxPrivacy.isChecked()) {
                    // Show a toast message if privacy policy is not accepted
                    Toast.makeText(SignUpActivity.this, "Please accept the Privacy Policy", Toast.LENGTH_SHORT).show();
                } else {
                    // If validation passes, simulate a successful sign-up
                    // In a real application, you would send this data to your server
                    // to create a new user account (e.g., using Firebase Authentication).
                    Toast.makeText(SignUpActivity.this, "Sign Up Successful for " + email, Toast.LENGTH_LONG).show();

                    // After successful sign-up, navigate the user back to the LoginActivity
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Finish the SignUpActivity so it's removed from the back stack
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUpActivity.this, "Facebook Sign Up not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUpActivity.this, "Google Sign Up not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
