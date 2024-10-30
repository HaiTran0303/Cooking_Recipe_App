package com.haith.cookingrecipeapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haith.cookingrecipeapp.adapters.DetailedDailyAdapter;
import com.haith.cookingrecipeapp.models.DetailedDailyModel;

import java.util.ArrayList;
import java.util.List;

public class DetailDailyRecipeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<DetailedDailyModel> detailedDailyModels;
    DetailedDailyAdapter detailedDailyAdapter;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_daily_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String type = getIntent().getStringExtra("type");
        recyclerView = findViewById(R.id.detailed_rec);
        imageView = findViewById(R.id.detailed_image);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailedDailyModels = new ArrayList<>();
        detailedDailyAdapter = new DetailedDailyAdapter(detailedDailyModels);
        recyclerView.setAdapter(detailedDailyAdapter);
        if (type != null && type.equalsIgnoreCase("breakfast")) {
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.fav1, "breakfast", "description", "4.4", "60min"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.fav2, "breakfast", "description", "4.4", "60min"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.fav3, "breakfast", "description", "4.4", "60min"));
            detailedDailyAdapter.notifyDataSetChanged();
        }
        if (type != null && type.equalsIgnoreCase("sweets")) {
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.s1, "breakfast", "description", "4.4", "60min"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.s2, "breakfast", "description", "4.4", "60min"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.s3, "breakfast", "description", "4.4", "60min"));
            detailedDailyAdapter.notifyDataSetChanged();
        }
    }
}