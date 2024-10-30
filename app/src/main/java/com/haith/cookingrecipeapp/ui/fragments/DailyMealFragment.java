package com.haith.cookingrecipeapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haith.cookingrecipeapp.R;
import com.haith.cookingrecipeapp.adapters.DailyMealAdapter;
import com.haith.cookingrecipeapp.models.DailyMealModel;

import java.util.ArrayList;
import java.util.List;

public class DailyMealFragment extends Fragment {
    RecyclerView recyclerView;
    List<DailyMealModel> dailyMealModels;
    DailyMealAdapter dailyMealAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_daily_meal, container, false);
        recyclerView = root.findViewById(R.id.daily_meal_rec);
        dailyMealModels = new ArrayList<>();

        dailyMealModels.add(new DailyMealModel(R.drawable.breakfast, "Breakfast", "Description right here"));
        dailyMealModels.add(new DailyMealModel(R.drawable.dinner, "Dinner", "Description right here"));
        dailyMealModels.add(new DailyMealModel(R.drawable.lunch, "Lunch", "Description right here"));
        dailyMealModels.add(new DailyMealModel(R.drawable.coffe, "Coffee", "Description right here"));

        dailyMealAdapter = new DailyMealAdapter(dailyMealModels, getContext());
        recyclerView.setAdapter(dailyMealAdapter);

        return root;
    }
}