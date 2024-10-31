package com.haith.cookingrecipeapp.models;

public class DetailedDailyModel {
    String image;
    String name;
    String amount;


    public DetailedDailyModel(String image, String name, String amount)  {
        this.image = image;
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.image = image;
    }

}
