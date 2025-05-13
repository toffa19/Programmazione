package com.progetto.viewmodel;

public class MacroTopicModel {
    private final String title;
    private final String imageUrl;  // resource path, e.g. "/assets/images/Basics.png"
    private boolean unlocked;
    private boolean passed;

    public MacroTopicModel(String title, String imageUrl) {
        this.title    = title;
        this.imageUrl = imageUrl;
        this.unlocked = false;
        this.passed   = false;
    }

    public String getTitle()     { return title; }
    public String getImageUrl()  { return imageUrl; }
    public boolean isUnlocked()  { return unlocked; }
    public void setUnlocked(boolean u) { unlocked = u; }
    public boolean isPassed()    { return passed; }
    public void setPassed(boolean p)   { passed = p; }
}
