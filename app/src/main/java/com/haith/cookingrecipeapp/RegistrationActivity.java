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
import com.haith.cookingrecipeapp.dao.RetrofitClient;
import com.haith.cookingrecipeapp.dao.SpoonacularApiService;
import com.haith.cookingrecipeapp.models.ApiModels.ConnectUserResponse;
import com.haith.cookingrecipeapp.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SpoonacularApiService apiService;
    private String API_KEY;

    // UI components
    private EditText nameEditText, phoneEditText, emailEditText, passwordEditText, rePasswordEditText;
    private Button registerButton;
    private TextView loginText;

//    private final String API_KEY = getString(R.string.api_key);

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        apiService = RetrofitClient.getApiService();
        API_KEY = getString(R.string.api_key);
    }
    private void bindingView() {
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rePasswordEditText = findViewById(R.id.rePassword);
        registerButton = findViewById(R.id.registerButton);
        loginText = findViewById(R.id.signInText);
    }

    private void bindingAction() {
        registerButton.setOnClickListener(view -> registerUser());
        loginText.setOnClickListener(view -> navigateToLogin());
    }

    private void navigateToLogin() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializeFirebase();
        bindingView();
        bindingAction();
    }


    private boolean isValidPassword(String password) {
        // Regular expression for password validation
        String pattern = "^(?=.*[A-Z]).{6,}$"; // At least 6 characters and one uppercase
        return password.matches(pattern);
    }

    private boolean validateInputs(String name, String phone, String email, String password, String rePassword) {
        if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(rePassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            return false;
        }
        if (phone.isEmpty()) {
            phoneEditText.setError("Phone number is required");
            return false;
        }
        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password must be at least 6 characters and contain at least one uppercase letter", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String rePassword = rePasswordEditText.getText().toString();

        if (!validateInputs(name, phone, email, password, rePassword)) return;

        // Create a new user using Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            connectUserToSpoonacular(userId, name, phone, email);
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();                    }
                });
    }

    private void connectUserToSpoonacular(String userId, String name, String phone, String email) {
        // Call Spoonacular's Connect User endpoint
        Call<ConnectUserResponse> call = apiService.connectUser(API_KEY, userId);
        call.enqueue(new Callback<ConnectUserResponse>() {
            @Override
            public void onResponse(Call<ConnectUserResponse> call, Response<ConnectUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String spoonacularUsername = response.body().getUsername();
                    String spoonacularHash = response.body().getHash();
                    // Save all user data to Firestore including Spoonacular details
                    saveUserDataToFirestore(userId, name, phone, email, spoonacularUsername, spoonacularHash);
                } else {
                    Toast.makeText(RegistrationActivity.this, "Failed to connect with Spoonacular", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ConnectUserResponse> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void saveUserDataToFirestore(String userId, String name, String phone, String email, String spoonacularUsername, String spoonacularHash) {
        // Create user data map including Spoonacular info
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("phone", phone);
        user.put("email", email);
        user.put("spoonacularUsername", spoonacularUsername);
        user.put("spoonacularHash", spoonacularHash);

        // Save to Firestore
        db.collection("Users").document(userId).set(user)
                .addOnCompleteListener(dataTask -> {
                    if (dataTask.isSuccessful()) {
                        Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Failed to save user data: " + dataTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}

