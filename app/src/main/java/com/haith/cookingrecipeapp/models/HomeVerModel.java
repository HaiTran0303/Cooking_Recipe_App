package com.haith.cookingrecipeapp.models;

public class HomeVerModel {

    String image;  // Updated from int to String to handle URLs
    String name;
    String servings;
    String aggregateLikes;
    String readyInMinutes;
    int recipeId;  // New field for recipe ID

    public HomeVerModel(String image, String name) {
        this.image = image;
        this.name = name;
    }
    public HomeVerModel(String image, String name, String servings, String aggregateLikes, String readyInMinutes, int recipeId) {
        this.image = image;
        this.name = name;
        this.servings = servings;
        this.aggregateLikes = aggregateLikes;
        this.readyInMinutes = readyInMinutes;
        this.recipeId = recipeId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getAggregateLikes() {
        return aggregateLikes;
    }

    public void setAggregateLikes(String aggregateLikes) {
        this.aggregateLikes = aggregateLikes;
    }

    public String getReadyInMinutes() {
        return readyInMinutes;
    }
    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setReadyInMinutes(String readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    //    public String getRating() {
//        return rating;
//    }
//
//    public void setRating(String rating) {
//        this.rating = rating;
//    }
}
