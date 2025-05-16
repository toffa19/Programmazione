package com.progetto.model;

public class MacroTopicEntry {
    private String macroTopic;
    private Levels levels;

    public MacroTopicEntry() {
    }

    public MacroTopicEntry(String macroTopic, Levels levels) {
        this.macroTopic = macroTopic;
        this.levels = levels;
    }

    public String getMacroTopic() {
        return macroTopic;
    }

    public void setMacroTopic(String macroTopic) {
        this.macroTopic = macroTopic;
    }

    public Levels getLevels() {
        return levels;
    }

    public void setLevels(Levels levels) {
        this.levels = levels;
    }
}
