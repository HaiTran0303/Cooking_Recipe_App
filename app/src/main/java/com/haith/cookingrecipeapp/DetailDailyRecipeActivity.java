package com.haith.cookingrecipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.haith.cookingrecipeapp.adapters.DetailedDailyAdapter;
import com.haith.cookingrecipeapp.adapters.InstructionAdapter;
import com.haith.cookingrecipeapp.dao.RetrofitClient;
import com.haith.cookingrecipeapp.dao.SpoonacularApiService;
import com.haith.cookingrecipeapp.models.ApiModels.AnalyzedInstruction;
import com.haith.cookingrecipeapp.models.ApiModels.Nutrient;
import com.haith.cookingrecipeapp.models.ApiModels.Recipe;
import com.haith.cookingrecipeapp.models.DetailedDailyModel;
import com.haith.cookingrecipeapp.models.FavoriteRecipe;
import com.haith.cookingrecipeapp.models.InstructionStepModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
public class DetailDailyRecipeActivity extends AppCompatActivity {
    // UI components
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private FloatingActionButton fabBack, fabFavorite;
    private ImageView detailedImage;
    private TextView customTitle, healthScore, nutrition,pricePerServing;

    // Data and API-related
    private int recipeId; // Assume this is passed from the previous activity
    private String API_KEY; // Replace with your Spoonacular API key
    private SpoonacularApiService apiService;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Data lists
    private List<DetailedDailyModel> ingredientsList;
    private List<InstructionStepModel> instructionsList;

    // Adapters
    private DetailedDailyAdapter ingredientsAdapter;
    private InstructionAdapter instructionsAdapter;

    private String recipeImageUrl;
    private String recipeTitle;
    private int recipeHealthScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_recipe);

        initializeApiService();
        initializeFirebase();
        bindingView();
        setupTabLayout();

        recipeId = getIntent().getIntExtra("recipeId", -1);
        setupListeners();
        loadIngredientsData(); // Default tab content

    }

    private void initializeFirebase() {
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    private void initializeApiService() {
        apiService = RetrofitClient.getApiService();
        API_KEY = getString(R.string.api_key);
    }
    private void bindingView() {
        tabLayout = findViewById(R.id.tab_layout);
        recyclerView = findViewById(R.id.detailed_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailedImage = findViewById(R.id.detailed_image);
        customTitle = findViewById(R.id.custom_title);
        healthScore = findViewById(R.id.healthScore);
        pricePerServing = findViewById(R.id.pricePerServing);
        nutrition = findViewById(R.id.nutrition);

        fabBack = findViewById(R.id.fab_back);
        fabFavorite = findViewById(R.id.fab_favorite);

        ingredientsList = new ArrayList<>();
        instructionsList = new ArrayList<>();

        ingredientsAdapter = new DetailedDailyAdapter(this, ingredientsList);
        instructionsAdapter = new InstructionAdapter(this, instructionsList);
    }
        private void setupListeners() {
            fabBack.setOnClickListener(v -> finish());
            checkIfRecipeIsFavorite(); // Call this to check if recipe is already a favorite

            fabFavorite.setOnClickListener(v -> {
                addRecipeToFavorites(); // Call the method to add the recipe
            });
        }

    private void checkIfRecipeIsFavorite() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();

            // Reference to the recipe in the Favorites collection
            db.collection("Users").document(userId)
                    .collection("Favorites")
                    .document(String.valueOf(recipeId))
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Recipe is already in favorites
                            fabFavorite.setEnabled(false); // Disable the button
                            Toast.makeText(this, "Recipe is already in your Favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            // Recipe is not in favorites
                            fabFavorite.setEnabled(true); // Enable the button
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to check favorites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void addRecipeToFavorites() {
        if(fabFavorite.isEnabled()) {
            if(mAuth.getCurrentUser() != null) {
                String userId = mAuth.getCurrentUser().getUid();

                FavoriteRecipe favoriteRecipe = new FavoriteRecipe(
                        recipeId,
                        recipeHealthScore,
                        recipeTitle,
                        recipeImageUrl
                );
                // Save to Firebase Firestore under the user's Favorites collection
                db.collection("Users").document(userId)
                        .collection("Favorites")
                        .document(String.valueOf(recipeId)) // Use recipeId as document ID to avoid duplicates
                        .set(favoriteRecipe)
                        .addOnSuccessListener(aVoid -> {
                            fabFavorite.setEnabled(false); // Disable button after adding
                            Toast.makeText(this, "Recipe added to Favorites", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to add to favorites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();

            }
        }
    }


    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    loadIngredientsData();
                } else if (tab.getPosition() == 1) {
                    loadInstructionsData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    private void loadIngredientsData() {
        Call<Recipe> call = apiService.getRecipeInformation(recipeId, API_KEY,true);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayIngredients(response.body());
                } else {
                    Toast.makeText(DetailDailyRecipeActivity.this, "Failed to load ingredients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(DetailDailyRecipeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadInstructionsData() {
        Call<List<AnalyzedInstruction>> call = apiService.getAnalyzedInstructions(recipeId, API_KEY);
        call.enqueue(new Callback<List<AnalyzedInstruction>>() {
            @Override
            public void onResponse(Call<List<AnalyzedInstruction>> call, Response<List<AnalyzedInstruction>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    displayInstructions(response.body());
                } else {
                    Toast.makeText(DetailDailyRecipeActivity.this, "No instructions available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AnalyzedInstruction>> call, Throwable t) {
                Toast.makeText(DetailDailyRecipeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayIngredients(Recipe recipe) {
        ingredientsList.clear();
        recipe.extendedIngredients.forEach(ingredient -> {
            String name = ingredient.name;
            String amount = ingredient.amount + " " + ingredient.unit;
            ingredientsList.add(new DetailedDailyModel(ingredient.image, name, amount));
        });
        recyclerView.setAdapter(ingredientsAdapter);
        ingredientsAdapter.notifyDataSetChanged();
        // Store recipe data in member variables
        recipeTitle = recipe.title;
        recipeHealthScore = recipe.healthScore;
        recipeImageUrl = recipe.image;

        // Set image
        Glide.with(this)
                .load(recipe.image)
                .placeholder(R.drawable.icon_main_course) // Add a placeholder
                .into(detailedImage);

        // Set title and description
        customTitle.setText(recipe.title);
        healthScore.setText("Health Score: "+ recipe.healthScore); // Displaying summary instead of description
        pricePerServing.setText("Price per Serving: "+recipe.pricePerServing + "$"); // Displaying summary instead of description

        // Set nutrition info
        if (recipe.nutrition != null && recipe.nutrition.nutrients != null) {
            StringBuilder result = new StringBuilder();
            result.append("Nutrition: ");
            for (Nutrient nutrient : recipe.nutrition.nutrients.subList(0,10)) {
                result.append(nutrient.name).append(" ").append(nutrient.amount).append(" ").append(nutrient.unit).append(" ").append(", ");
            }
            nutrition.setText(result.toString());
        } else {
            nutrition.setText("No nutrition information available");
        }
    }

    private void displayInstructions(List<AnalyzedInstruction> instructions) {
        instructionsList.clear();
        instructions.get(0).steps.forEach(step -> {
            instructionsList.add(new InstructionStepModel(step.number, step.step));
        });
        recyclerView.setAdapter(instructionsAdapter);
        instructionsAdapter.notifyDataSetChanged();
    }
    public void showDetail(View view) {
        startActivity(new Intent(DetailDailyRecipeActivity.this, MainActivity.class));
    }

}