package com.haith.cookingrecipeapp.models;

public class DailyMealModel {
    int image;
    String title;
    String description;
    String type;

    public DailyMealModel(int image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public DailyMealModel(String title, String description, int image, String type) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.type = type;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
