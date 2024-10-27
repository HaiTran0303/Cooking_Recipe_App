package com.haith.cookingrecipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

    }

    public void login(View view){
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
    }

    public void mainActivity(View view){
        startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
    }
}