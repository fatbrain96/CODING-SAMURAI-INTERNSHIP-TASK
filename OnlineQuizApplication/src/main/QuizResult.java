package main;

public class QuizResult {
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private double percentage;
    private long timeTaken; // in seconds
    
    public QuizResult(int totalQuestions, int correctAnswers, long timeTaken) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = totalQuestions - correctAnswers;
        this.percentage = (double) correctAnswers / totalQuestions * 100;
        this.timeTaken = timeTaken;
    }
    
    // Getters
    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectAnswers() { return correctAnswers; }
    public int getWrongAnswers() { return wrongAnswers; }
    public double getPercentage() { return percentage; }
    public long getTimeTaken() { return timeTaken; }
    
    public String getGrade() {
        if (percentage >= 90) return "A+";
        else if (percentage >= 80) return "A";
        else if (percentage >= 70) return "B";
        else if (percentage >= 60) return "C";
        else if (percentage >= 50) return "D";
        else return "F";
    }
}
