package com.progetto.repository;

import com.progetto.model.ExercisesWrapper;
import com.progetto.model.Levels;
import com.progetto.model.MacroTopicEntry;
import com.progetto.model.MultipleChoiceExercise;
import com.progetto.util.Config;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExerciseRepository {

    private ExercisesWrapper wrapper;

    public ExerciseRepository() {
        loadExercises();
    }

    public void addTopic(String macroTopic) {
        // evita duplicati
        boolean exists = wrapper.getExercises().stream()
                .anyMatch(e -> e.getMacroTopic().equals(macroTopic));
        if (!exists) {
            wrapper.getExercises().add(new MacroTopicEntry(macroTopic, new Levels()));
            saveExercises();
        }
    }
    private void loadExercises() {
        // 1) Provo a caricare il JSON di default dal classpath
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("view/exercises.json")) {
            if (is != null) {
                wrapper = Config.MAPPER.readValue(is, ExercisesWrapper.class);
                System.out.println("[LOG] Loaded exercises from classpath");
            } else {
                System.out.println("[WARN] Classpath exercises.json not found");
                wrapper = loadExternalOrCreateEmpty();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Error parsing classpath exercises.json");
            e.printStackTrace();
            wrapper = loadExternalOrCreateEmpty();
        }

        // 2) Propago macroTopic e level su ogni esercizio
        for (MacroTopicEntry entry : wrapper.getExercises()) {
            String topic = entry.getMacroTopic();
            Levels lvls = entry.getLevels();

            // FACILE
            for (MultipleChoiceExercise ex : lvls.getFacile()) {
                ex.setMacroTopic(topic);
                ex.setLevel("facile");
            }
            // MEDIO
            for (MultipleChoiceExercise ex : lvls.getMedio()) {
                ex.setMacroTopic(topic);
                ex.setLevel("medio");
            }
            // DIFFICILE
            for (MultipleChoiceExercise ex : lvls.getDifficile()) {
                ex.setMacroTopic(topic);
                ex.setLevel("difficile");
            }
        }
    }

    // helper usato sopra
    private ExercisesWrapper loadExternalOrCreateEmpty() {
        File ext = new File(Config.EXERCISES_JSON_PATH);
        if (ext.exists()) {
            try {
                System.out.println("[LOG] Loaded exercises from external file");
                return Config.MAPPER.readValue(ext, ExercisesWrapper.class);
            } catch (Exception e) {
                System.out.println("[ERROR] Error parsing external exercises.json");
                e.printStackTrace();
            }
        }
        // se non c'è o fallisce, creo un wrapper vuoto e lo salvo
        wrapper = new ExercisesWrapper(new ArrayList<>());
        saveExercises();  // salva su file esterno
        System.out.println("[LOG] Created new external exercises.json");
        return wrapper;
    }

    public void removeExercise(String topic, String level, String qid) {
        for (MacroTopicEntry e : getAllEntries()) {
            if (e.getMacroTopic().equals(topic)) {
                List<MultipleChoiceExercise> list = switch(level) {
                    case "facile"    -> e.getLevels().getFacile();
                    case "medio"     -> e.getLevels().getMedio();
                    case "difficile" -> e.getLevels().getDifficile();
                    default          -> throw new IllegalArgumentException("Livello non valido: " + level);
                };
                list.removeIf(x -> x.getQuestionId().equals(qid));
                saveExercises(); // serializza di nuovo il file exercises.json
                break;
            }
        }
    }


    public  void saveExercises() {
        try {
            File ext = new File(Config.EXERCISES_JSON_PATH);
            Config.MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValue(ext, wrapper);
            System.out.println("[LOG] Saved exercises to external file");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed writing external exercises.json");
            e.printStackTrace();
        }
    }

    /** Tutti i topic + livelli */
    public List<MacroTopicEntry> getAllEntries() {
        return wrapper.getExercises();
    }

    /** Solo i nomi dei topic */
    public List<String> getAllTopics() {
        return getAllEntries().stream()
                .map(MacroTopicEntry::getMacroTopic)
                .collect(Collectors.toList());
    }

    /** Aggiunge un esercizio al livello corretto */
    public void addExercise(MultipleChoiceExercise ex) {
        for (MacroTopicEntry entry : getAllEntries()) {
            if (entry.getMacroTopic().equals(ex.getMacroTopic())) {
                entry.getLevels().addExercise(ex);
                saveExercises();
                return;
            }
        }
        throw new IllegalArgumentException("Topic non trovato: " + ex.getMacroTopic());
    }

    /** Modifica un esercizio esistente */
    public void updateExercise(MultipleChoiceExercise updated) {
        for (MacroTopicEntry entry : getAllEntries()) {
            if (entry.getMacroTopic().equals(updated.getMacroTopic())) {
                boolean ok = entry.getLevels().replaceExercise(updated);
                if (ok) saveExercises();
                return;
            }
        }
        throw new IllegalArgumentException("Esercizio non trovato: " + updated.getQuestionId());
    }
}
