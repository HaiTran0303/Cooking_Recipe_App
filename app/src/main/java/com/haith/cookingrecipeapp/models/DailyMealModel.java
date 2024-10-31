package com.haith.cookingrecipeapp.models;

public class DailyMealModel {
    int healScore;
    String image;
    String id;
    String description;

    public DailyMealModel(String image, String id, String description,int healScore) {
        this.image = image;
        this.id = id;
        this.description = description;
        this.healScore = healScore;
    }

    public int getHealScore() {
        return healScore;
    }

    public void setHealScore(int healScore) {
        this.healScore = healScore;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
