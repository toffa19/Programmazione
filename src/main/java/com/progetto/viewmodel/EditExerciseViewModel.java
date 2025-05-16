package com.progetto.viewmodel;

import com.progetto.model.MacroTopicEntry;
import com.progetto.model.MultipleChoiceExercise;
import com.progetto.repository.ExerciseRepository;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * ViewModel per la schermata di modifica esercizio.
 */
public class EditExerciseViewModel {

    private final ExerciseRepository repo = new ExerciseRepository();

    private final ObservableList<String> topics      = FXCollections.observableArrayList();
    private final ObservableList<String> levels      = FXCollections.observableArrayList("facile","medio","difficile");
    private final ObservableList<String> questionIds = FXCollections.observableArrayList();

    private final StringProperty selectedTopic      = new SimpleStringProperty();
    private final StringProperty selectedLevel      = new SimpleStringProperty();
    private final StringProperty selectedQuestionId = new SimpleStringProperty();

    private final StringProperty questionText    = new SimpleStringProperty();
    private final ListProperty<String> options   = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty correctAnswer   = new SimpleStringProperty();

    private MultipleChoiceExercise current;

    public EditExerciseViewModel() {
        topics.setAll(repo.getAllTopics());
    }

    public ObservableList<String> getTopics()      { return topics; }
    public ObservableList<String> getLevels()      { return levels; }
    public ObservableList<String> getQuestionIds() { return questionIds; }

    public StringProperty selectedTopicProperty()      { return selectedTopic; }
    public StringProperty selectedLevelProperty()      { return selectedLevel; }
    public StringProperty selectedQuestionIdProperty() { return selectedQuestionId; }

    public StringProperty questionTextProperty()  { return questionText; }
    public ListProperty<String> optionsProperty() { return options; }
    public StringProperty correctAnswerProperty() { return correctAnswer; }

    public void loadQuestionIds() {
        questionIds.clear();
        String t = selectedTopic.get(), l = selectedLevel.get();
        if (t == null || l == null) return;
        for (MacroTopicEntry e : repo.getAllEntries()) {
            if (e.getMacroTopic().equals(t)) {
                switch (l) {
                    case "facile"   -> e.getLevels().getFacile().forEach(q-> questionIds.add(q.getQuestionId()));
                    case "medio"    -> e.getLevels().getMedio().forEach(q-> questionIds.add(q.getQuestionId()));
                    case "difficile"-> e.getLevels().getDifficile().forEach(q-> questionIds.add(q.getQuestionId()));
                }
                break;
            }
        }
    }

    public void loadQuestion() {
        String t = selectedTopic.get(), l = selectedLevel.get(), qid = selectedQuestionId.get();
        if (t == null || l == null || qid == null) return;
        for (MacroTopicEntry e : repo.getAllEntries()) {
            if (e.getMacroTopic().equals(t)) {
                List<MultipleChoiceExercise> list = switch (l) {
                    case "facile"   -> e.getLevels().getFacile();
                    case "medio"    -> e.getLevels().getMedio();
                    default         -> e.getLevels().getDifficile();
                };
                for (MultipleChoiceExercise ex : list) {
                    if (ex.getQuestionId().equals(qid)) {
                        current = ex;
                        questionText.set(ex.getQuestion());
                        options.set(FXCollections.observableArrayList(ex.getOptions()));
                        correctAnswer.set(ex.getCorrectAnswer());
                        return;
                    }
                }
            }
        }
    }

    public void saveEdited() {
        if (current != null) {
            current.setQuestion(questionText.get());
            current.setOptions(options.get());
            current.setCorrectAnswer(correctAnswer.get());
            repo.updateExercise(current);
        }
    }
}
