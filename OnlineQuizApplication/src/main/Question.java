package main;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class Question {
    private String category;
    private String type;
    private String difficulty;
    private String question;
    private String correctAnswer;
    private List<String> incorrectAnswers;
    private List<String> allAnswers;

    public Question() {
        this.incorrectAnswers = new ArrayList<>();
        this.allAnswers = new ArrayList<>();
    }

    // Extra constructor for QuizAPI compatibility
    public Question(String question, List<String> allAnswers, String correctAnswer) {
        this.question = question;
        this.allAnswers = new ArrayList<>(allAnswers);
        this.correctAnswer = correctAnswer;
    }

    // Getters and Setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public List<String> getIncorrectAnswers() { return incorrectAnswers; }
    public void setIncorrectAnswers(List<String> incorrectAnswers) { 
        this.incorrectAnswers = incorrectAnswers; 
        generateAllAnswers();
    }

    public List<String> getAllAnswers() { return allAnswers; }

    private void generateAllAnswers() {
        allAnswers.clear();
        allAnswers.add(correctAnswer);
        allAnswers.addAll(incorrectAnswers);
        Collections.shuffle(allAnswers);
    }

    public boolean isCorrect(String answer) {
        return correctAnswer.equals(answer);
    }
}
