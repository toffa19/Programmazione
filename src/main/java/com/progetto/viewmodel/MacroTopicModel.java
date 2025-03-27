package com.progetto.viewmodel;

public class MacroTopicModel {
    private String title;
    private String imageUrl;

    public MacroTopicModel(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
