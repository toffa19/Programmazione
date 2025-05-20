package com.progetto.viewmodel;

import com.progetto.model.MacroTopicEntry;
import com.progetto.repository.ExerciseRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import com.progetto.model.Levels;
import java.util.List;

public class DeleteExerciseViewModel {
    private final ExerciseRepository repo = new ExerciseRepository();
    private final Levels deleteLevel = new Levels();
    public final ObservableList<String> topics      = FXCollections.observableArrayList();
    public final ObservableList<String> levels      = FXCollections.observableArrayList("facile","medio","difficile");
    public final ObservableList<String> questionIds = FXCollections.observableArrayList();

    private final StringProperty selectedTopic      = new SimpleStringProperty();
    private final StringProperty selectedLevel      = new SimpleStringProperty();
    private final StringProperty selectedQuestionId = new SimpleStringProperty();

    public DeleteExerciseViewModel() {
        topics.setAll(repo.getAllTopics());
    }

    public void loadQuestionIds() {
        questionIds.clear();
        String t = selectedTopic.get(), l = selectedLevel.get();
        if (t == null || l == null) return;
        for (MacroTopicEntry e : repo.getAllEntries()) {
            if (e.getMacroTopic().equals(t)) {
                List<String> ids = switch (l) {
                    case "facile"   -> e.getLevels().getFacile().stream().map(x->x.getQuestionId()).toList();
                    case "medio"    -> e.getLevels().getMedio().stream().map(x->x.getQuestionId()).toList();
                    default         -> e.getLevels().getDifficile().stream().map(x->x.getQuestionId()).toList();
                };
                questionIds.addAll(ids);
                break;
            }
        }
    }

    public void deleteSelected() {
        String topic = selectedTopic.get();
        String level = selectedLevel.get();
        String qid   = selectedQuestionId.get();
        if (topic==null || level==null || qid==null) return;
        // rimuove dall’array interno e salva su JSON
        repo.removeExercise(topic, level, qid);
    }

    // — getter per binding —
    public ObservableList<String> getTopics()      { return topics;      }
    public ObservableList<String> getLevels()      { return levels;      }
    public ObservableList<String> getQuestionIds() { return questionIds; }

    public StringProperty selectedTopicProperty()      { return selectedTopic;      }
    public StringProperty selectedLevelProperty()      { return selectedLevel;      }
    public StringProperty selectedQuestionIdProperty() { return selectedQuestionId; }
}
