package com.haith.cookingrecipeapp.models.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipeResponse {
    public List<Recipe> results;  // This maps to the "results" array in the JSON
    public int offset;
    public int number;
    public int totalResults;
}
