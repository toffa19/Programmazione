package com.progetto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Progress {
    private String macroTopic;
    private List<QuestionResult> questionResults;
    private int score;
    private String time;
    private boolean passed;

    public String getMacroTopic() { return macroTopic; }
    public void setMacroTopic(String macroTopic) { this.macroTopic = macroTopic; }

//    public String getDifficulty() { return difficulty; }
//    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public List<QuestionResult> getQuestionResults() { return questionResults; }
    public void setQuestionResults(List<QuestionResult> questionResults) { this.questionResults = questionResults; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

}
