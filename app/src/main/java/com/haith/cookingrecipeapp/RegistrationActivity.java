package com.haith.cookingrecipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, rePasswordEditText;
    private Button registerButton;
    private TextView loginText;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rePasswordEditText = findViewById(R.id.rePassword);
        registerButton = findViewById(R.id.registerButton);
        loginText = findViewById(R.id.signupText);

        // Set up Register button listener
        registerButton.setOnClickListener(view -> registerUser());
        // Navigate to Login activity when clicking on the "Login here" text
        loginText.setOnClickListener(view -> {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }


    private boolean isValidPassword(String password) {
        // Regular expression for password validation
        String pattern = "^(?=.*[A-Z]).{6,}$"; // At least 6 characters and one uppercase
        return password.matches(pattern);
    }

    private void registerUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String rePassword = rePasswordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(rePassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validate password strength
        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password must be at least 6 characters and contain at least one uppercase letter", Toast.LENGTH_LONG).show();
            return;
        }
        // Create a new user using Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        // Navigate to the login screen
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}

