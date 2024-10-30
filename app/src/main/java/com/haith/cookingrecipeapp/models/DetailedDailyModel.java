package com.haith.cookingrecipeapp.models;

public class DetailedDailyModel {
    int image;
    String name;
    String description;
    String rating;
    String timing;

    public DetailedDailyModel(int image, String name, String description, String rating, String timing) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.timing = timing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
}
