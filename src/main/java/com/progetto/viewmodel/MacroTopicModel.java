package com.progetto.viewmodel;

public class MacroTopicModel {
    private String title;
    private String imageUrl;
    private boolean unlocked;

    public MacroTopicModel(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.unlocked = false;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}
