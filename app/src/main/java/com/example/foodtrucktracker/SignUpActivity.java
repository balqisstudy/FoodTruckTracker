package com.example.foodtrucktracker; // Replace with your package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextSignUpEmail;
    private EditText editTextSignUpPassword;
    private EditText editTextConfirmPassword;
    private Button buttonSignUp;
    private TextView textViewLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements by finding them by their IDs
        editTextSignUpEmail = findViewById(R.id.editTextSignUpEmail);
        editTextSignUpPassword = findViewById(R.id.editTextSignUpPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLoginLink = findViewById(R.id.textViewLoginLink);

        // Set an OnClickListener for the Sign Up button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text input from the email, password, and confirm password fields
                String email = editTextSignUpEmail.getText().toString().trim();
                String password = editTextSignUpPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                // Perform basic validation checks
                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    // Show a toast message if any field is empty
                    Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    // Show a toast message if passwords do not match
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
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

        // Set an OnClickListener for the "Already have an account? Login" link
        textViewLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate the user to the LoginActivity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Finish the SignUpActivity
            }
        });
    }
}
