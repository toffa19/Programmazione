package com.progetto.viewmodel;

import com.progetto.model.MultipleChoiceExercise;
import com.progetto.repository.ExerciseRepository;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableListBase;

import java.util.ArrayList;

public class CreateExerciseViewModel {
    private final ExerciseRepository repo = new ExerciseRepository();

    private final ObservableList<String> topics = FXCollections.observableArrayList();
    private final ObservableList<String> levels = FXCollections.observableArrayList("facile", "medio", "difficile");

    private final StringProperty selectedTopic   = new SimpleStringProperty();
    private final StringProperty selectedLevel   = new SimpleStringProperty();
    private final StringProperty questionId      = new SimpleStringProperty();
    private final StringProperty questionText    = new SimpleStringProperty();
    private final ListProperty<String> options   = new javafx.beans.property.SimpleListProperty<>(FXCollections.observableArrayList("", "", "", ""));
    private final StringProperty correctAnswer   = new SimpleStringProperty();

    public CreateExerciseViewModel() {
        loadTopics();
    }

    private void loadTopics() {
        topics.setAll(repo.getAllTopics());
    }

    public ObservableList<String> getTopics() {
        return topics;
    }

    public ObservableList<String> getLevels() {
        return levels;
    }

    public StringProperty selectedTopicProperty() {
        return selectedTopic;
    }

    public StringProperty selectedLevelProperty() {
        return selectedLevel;
    }

    public StringProperty questionIdProperty() {
        return questionId;
    }

    public StringProperty questionTextProperty() {
        return questionText;
    }

    public ListProperty<String> optionsProperty() {
        return options;
    }

    public StringProperty correctAnswerProperty() {
        return correctAnswer;
    }

    /**
     * Crea un esercizio; se keepTopic=false resetta anche topic/level
     */
    private void createExercise(boolean keepTopic) {
        String topic = selectedTopic.get();
        String level = selectedLevel.get();
        if (topic == null || level == null || questionId.get().isEmpty()) {
            return;
        }

        // Se il topic non esiste, lo creo
        if (!topics.contains(topic)) {
            repo.addTopic(topic);
            topics.add(topic);
        }

        // Costruisco e salvo l'esercizio
        MultipleChoiceExercise ex = new MultipleChoiceExercise();
        ex.setMacroTopic(topic);
        ex.setLevel(level);
        ex.setQuestionId(questionId.get());
        ex.setQuestion(questionText.get());
        ex.setOptions(new ArrayList<>(options.get()));
        ex.setCorrectAnswer(correctAnswer.get());

        repo.addExercise(ex);

        // Reset campi tranne topic/level se keepTopic==true
        questionId.set("");
        questionText.set("");
        options.set(FXCollections.observableArrayList("", "", "", ""));
        correctAnswer.set("");
        if (!keepTopic) {
            selectedTopic.set(null);
            selectedLevel.set(null);
        }
    }

    /** Salva ed esce (ritorna alla Home o chiude) */
    public void createAndExit() {
        createExercise(false);
    }

    /** Salva e rimane sul form per inserire un altro esercizio */
    public void createAndContinue() {
        createExercise(true);
    }
}
