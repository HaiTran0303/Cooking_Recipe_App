package com.haith.cookingrecipeapp.dao;

import com.haith.cookingrecipeapp.models.ApiModels.RecipeResponse;
import com.haith.cookingrecipeapp.models.ApiModels.RecipeSuggestion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpoonacularApiService {
    @GET("recipes/complexSearch")
    Call<RecipeResponse> getRecipesByType(
            @Query("type") String type,
            @Query("apiKey") String apiKey,
            @Query("offset") int offset,                     // Added offset for pagination
            @Query("number") int number,                     // Set page size
            @Query("addRecipeInformation") boolean addRecipeInformation
    );

    @GET("recipes/complexSearch")
    Call<RecipeResponse> getPopularRecipes(
            @Query("apiKey") String apiKey,
            @Query("sort") String sort,
            @Query("sortDirection") String sortDirection,
            @Query("offset") int offset,                     // Added offset for pagination
            @Query("number") int number,                     // Set page size
            @Query("addRecipeInformation") boolean addRecipeInformation
    );
    @GET("recipes/complexSearch")
    Call<RecipeResponse> searchRecipes(
            @Query("apiKey") String apiKey,
            @Query("query") String query,
            @Query("cuisine") String cuisine,
            @Query("diet") String diet,
            @Query("type") String type,
            @Query("sort") String sort,
            @Query("sortDirection") String sortDirection,
            @Query("offset") int offset,                     // Added offset for pagination
            @Query("number") int number,                     // Set page size
            @Query("addRecipeInformation") boolean addRecipeInformation
    );

    @GET("recipes/autocomplete")
    Call<List<RecipeSuggestion>> getAutocompleteRecipeNames(
      @Query("apiKey") String apiKey,
      @Query("query") String query,
      @Query("number") int number
    );

    @GET("recipes/complexSearch")
    Call<RecipeResponse> searchRecipesByQuery(
            @Query("apiKey") String apiKey,
            @Query("query") String query,
            @Query("number") int number,
            @Query("offset") int offset,
            @Query("addRecipeInformation") boolean addRecipeInformation

    );

}
