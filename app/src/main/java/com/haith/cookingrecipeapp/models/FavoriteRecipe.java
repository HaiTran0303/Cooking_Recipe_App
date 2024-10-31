package com.haith.cookingrecipeapp.models;

import java.util.List;

public class FavoriteRecipe {
    private int id;
    private int healthScore;
    private String title;
    private String image;

    // Constructor
    public FavoriteRecipe(int id, int healthScore,  String title, String image) {
        this.id = id;
        this.healthScore = healthScore;
        this.title = title;
        this.image = image;
    }
    // Empty constructor required for Firebase
    public FavoriteRecipe() {}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(int healthScore) {
        this.healthScore = healthScore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
