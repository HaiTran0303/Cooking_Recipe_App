package com.haith.cookingrecipeapp.models.ApiModels;

public class RecipeSuggestion {
        private int id;
        private String title;
        private String imageType;

        public RecipeSuggestion(int id, String title, String imageType) {
            this.id = id;
            this.title = title;
            this.imageType = imageType;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImageType() {
            return imageType;
        }
}
