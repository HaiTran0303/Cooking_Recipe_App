package com.haith.cookingrecipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    ImageView imageView, imageDetailView;
    TextView customTitle;

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
//        Pass the title
        customTitle = findViewById(R.id.custom_title);
        String title = getIntent().getStringExtra("title");
        if (title != null || title != "") {
            customTitle.setText(title);
        }
//      Pass the image
        imageDetailView = findViewById(R.id.detailed_image);
        int imageDetail = getIntent().getIntExtra("imageView", R.drawable.breakfast);
        imageDetailView.setImageResource(imageDetail);
        recyclerView = findViewById(R.id.detailed_rec);
        imageView = findViewById(R.id.detailed_image);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailedDailyModels = new ArrayList<>();
        detailedDailyAdapter = new DetailedDailyAdapter(detailedDailyModels);
        recyclerView.setAdapter(detailedDailyAdapter);
//        Show list of lunch recipe
        if (type != null && type.equalsIgnoreCase("lunch")) {
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.fav1, "Mixed Cereal", "description", "4.4", "60min"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.fav2, "Cheese Hamburger", "description", "4.4", "20min"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.fav3, "Spaghetti", "description", "4.4", "30min"));
            detailedDailyAdapter.notifyDataSetChanged();
        }
//        Show the list of breakfast recipe
        if (type != null && type.equalsIgnoreCase("breakfast")) {
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.ver1, "Strawberry Cereal", "description", "5.5", "20min"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.ver2, "Omelette", "description", "5.5", "20min"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.ver3, "Pancake", "description", "5.5", "20min"));
        }
//        Show the list of sweet recipe
        if (type != null && type.equalsIgnoreCase("sweet")) {
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.s1, "Chocolate", "description", "4.4", "60min"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.s2, "Chocolate Donut", "description", "4.4", "60min"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.s3, "Ice-cream", "description", "4.4", "60min"));
            detailedDailyAdapter.notifyDataSetChanged();
        }

    }

    public void showDetail(View view) {
        startActivity(new Intent(DetailDailyRecipeActivity.this, MainActivity.class));
    }

}