package com.haith.cookingrecipeapp.models;

public class HomeHorModel {
    private String typeName; // e.g., "main course", "dessert"
    private int image; // Optional: Set a default image or icons for each type
//    int image;
//    String name;

    public HomeHorModel(String typeName, int image) {
        this.typeName = typeName;
        this.image = image;
    }
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
