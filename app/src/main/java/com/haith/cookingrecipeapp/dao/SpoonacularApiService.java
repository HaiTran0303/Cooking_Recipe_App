package com.haith.cookingrecipeapp.dao;

import com.haith.cookingrecipeapp.models.ApiModels.AnalyzedInstruction;
import com.haith.cookingrecipeapp.models.ApiModels.ConnectUserResponse;
import com.haith.cookingrecipeapp.models.ApiModels.Recipe;
import com.haith.cookingrecipeapp.models.ApiModels.RecipeResponse;
import com.haith.cookingrecipeapp.models.ApiModels.RecipeSuggestion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpoonacularApiService {
    @GET("recipes/complexSearch")
    Call<RecipeResponse> getRecipesByType(
            @Query("type") String type,
            @Query("apiKey") String apiKey,
            @Query("offset") int offset,                     // Added offset for pagination
            @Query("number") int number,                     // Set page size
            @Query("addRecipeInformation") boolean addRecipeInformation,
            @Query("sort") String sort,
            @Query("sortDirection") String sortDirection

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
    @POST("users/connect")
    Call<ConnectUserResponse> connectUser(
            @Query("apiKey") String apiKey,
            @Query("username") String username
    );

    // Endpoint to get detailed recipe information by ID
    @GET("recipes/{id}/information")
    Call<Recipe> getRecipeInformation(
            @Path("id") int recipeId,
            @Query("apiKey") String apiKey,
            @Query("includeNutrition") boolean includeNutrition

    );
    // New Endpoint to get analyzed instructions by recipe ID
    @GET("recipes/{id}/analyzedInstructions")
    Call<List<AnalyzedInstruction>> getAnalyzedInstructions(
            @Path("id") int recipeId,
            @Query("apiKey") String apiKey
    );

}
