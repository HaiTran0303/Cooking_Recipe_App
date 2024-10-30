package com.haith.cookingrecipeapp.models;

public class DetailedDailyModel {
    String name;
    int image;
    String description;
    String rating;
    String timing;

    public DetailedDailyModel(String name, int image, String description, String rating, String timing) {
        this.name = name;
        this.image = image;
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
