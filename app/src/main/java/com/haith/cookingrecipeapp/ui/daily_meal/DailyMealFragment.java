package com.haith.cookingrecipeapp.ui.daily_meal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haith.cookingrecipeapp.R;
import com.haith.cookingrecipeapp.adapters.DailyMealAdapter;
import com.haith.cookingrecipeapp.dao.RetrofitClient;
import com.haith.cookingrecipeapp.models.DailyMealModel;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class DailyMealFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<DailyMealModel> dailyMealModels;
    private DailyMealAdapter dailyMealAdapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_daily_meal, container, false);
        initializeFirebase();

        bindingView(root);
        bindingAction();

        return root;
    }

    private void bindingView(View root) {
        // Set up RecyclerView and adapter
        recyclerView = root.findViewById(R.id.daily_meal_rec);
        dailyMealModels = new ArrayList<>();

        // Initialize adapter with empty list and set it to RecyclerView
        dailyMealAdapter = new DailyMealAdapter(requireContext(), dailyMealModels);
        recyclerView.setAdapter(dailyMealAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

    }
    private void bindingAction() {
        // Load favorite recipes from Firebase and update the RecyclerView
        loadFavoriteRecipes();
    }

    private void loadFavoriteRecipes() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();

            db.collection("Users").document(userId)
                    .collection("Favorites")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        dailyMealModels.clear(); // Clear list before adding new data
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            try {
                                // Extract data from Firestore
                                String title = document.getString("title");
                                Long healthScoreLong = document.getLong("healthScore"); // Retrieve healthScore as Long
                                Long idLong = document.getLong("id"); // Retrieve id as Long
                                String image = document.getString("image");

                                // Ensure fields are not null
                                if (title != null && healthScoreLong != null && idLong != null) {
                                    int healthScore = healthScoreLong.intValue(); // Convert Long to int for healthScore
                                    int id = idLong.intValue(); // Convert Long to int for id

                                    // Create a new DailyMealModel and add it to the list
                                    DailyMealModel dailyMealModel = new DailyMealModel(image, String.valueOf(id), title, healthScore);
                                    dailyMealModels.add(dailyMealModel);
                                } else {
                                    // Log or show a message if fields are missing
                                    Toast.makeText(getContext(), "Missing fields in document: " + document.getId(), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Error processing document: " + document.getId(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        dailyMealAdapter.notifyDataSetChanged(); // Notify adapter of data change

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to load favorites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

}
