package com.haith.cookingrecipeapp.listener;

import com.haith.cookingrecipeapp.models.ApiModels.RecipeResponse;

public interface RandomRecipesResponseListener {
    void didFetch(RecipeResponse response, String message);
    void didError(String message);

}
