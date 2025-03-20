package com.progetto.model;

import java.util.List;
import java.util.Map;

public abstract class Exercise {
    private String macroTopic;
    // Mappa dei livelli: chiave = livello ("facile", "medio", "difficile"), valore = lista di domande
    private Map<String, List<Question>> levels;

    public Exercise() {
    }

    public Exercise(String macroTopic, Map<String, List<Question>> levels) {
        this.macroTopic = macroTopic;
        this.levels = levels;
    }

    public String getMacroTopic() { return macroTopic; }
    public void setMacroTopic(String macroTopic) { this.macroTopic = macroTopic; }

    public Map<String, List<Question>> getLevels() { return levels; }
    public void setLevels(Map<String, List<Question>> levels) { this.levels = levels; }

    // Metodo astratto: valutare la risposta a una domanda
    public abstract boolean evaluateAnswer(String questionId, String answer);
}
