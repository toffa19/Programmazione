package com.progetto.viewmodel;

public class TopicModel {
    private String name;
    private String description;
    private int lessonCount;
    private int practiceCount;
    private boolean unlocked;

    public TopicModel(String name, String description, int lessonCount, int practiceCount, boolean unlocked) {
        this.name = name;
        this.description = description;
        this.lessonCount = lessonCount;
        this.practiceCount = practiceCount;
        this.unlocked = unlocked;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLessonCount() {
        return lessonCount;
    }

    public int getPracticeCount() {
        return practiceCount;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}
