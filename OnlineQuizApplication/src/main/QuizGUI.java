package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class QuizGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Start Screen Components
    private JComboBox<String> categoryCombo;
    private JComboBox<String> difficultyCombo;
    private JSpinner questionCountSpinner;
    
    // Quiz Screen Components
    private JLabel questionLabel;
    private JLabel questionCountLabel;
    private JLabel categoryLabel;
    private JLabel difficultyLabel;
    private ButtonGroup answerGroup;
    private JRadioButton[] answerButtons;
    private JButton nextButton;
    private JButton previousButton;
    private JProgressBar progressBar;
    private JLabel timerLabel;
    
    // Quiz Data
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private String[] userAnswers;
    private long startTime;
    private Timer timer;
    private int timeElapsed = 0;
    
    public QuizGUI() {
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Online Quiz Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        createStartScreen();
        createQuizScreen();
        createResultScreen();
        
        add(mainPanel);
        cardLayout.show(mainPanel, "START");
    }
    
    private void createStartScreen() {
        JPanel startPanel = new JPanel(new BorderLayout());
        startPanel.setBackground(new Color(240, 248, 255));
        
        // Title
        JLabel titleLabel = new JLabel("Online Quiz Application", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        // Settings Panel
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBackground(Color.WHITE);
        settingsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Quiz Settings", 0, 0, new Font("Arial", Font.BOLD, 16),
            new Color(70, 130, 180)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Category selection
        gbc.gridx = 0; gbc.gridy = 0;
        settingsPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        categoryCombo = new JComboBox<>(new String[]{
            "Any", "General Knowledge", "Science", "History", "Geography", "Sports", "Entertainment"
        });
        categoryCombo.setPreferredSize(new Dimension(200, 30));
        settingsPanel.add(categoryCombo, gbc);
        
        // Difficulty selection
        gbc.gridx = 0; gbc.gridy = 1;
        settingsPanel.add(new JLabel("Difficulty:"), gbc);
        gbc.gridx = 1;
        difficultyCombo = new JComboBox<>(new String[]{"Any", "Easy", "Medium", "Hard"});
        difficultyCombo.setPreferredSize(new Dimension(200, 30));
        settingsPanel.add(difficultyCombo, gbc);
        
        // Question count
        gbc.gridx = 0; gbc.gridy = 2;
        settingsPanel.add(new JLabel("Number of Questions:"), gbc);
        gbc.gridx = 1;
        questionCountSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 20, 1));
        questionCountSpinner.setPreferredSize(new Dimension(200, 30));
        settingsPanel.add(questionCountSpinner, gbc);
        
        // Start button
        JButton startButton = new JButton("Start Quiz");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(34, 139, 34));
        startButton.setForeground(Color.WHITE);
        startButton.setPreferredSize(new Dimension(150, 40));
        startButton.addActionListener(e -> startQuiz());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(startButton);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        centerPanel.add(settingsPanel, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        
        startPanel.add(titleLabel, BorderLayout.NORTH);
        startPanel.add(centerPanel, BorderLayout.CENTER);
        startPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(startPanel, "START");
    }
    
    private void createQuizScreen() {
        JPanel quizPanel = new JPanel(new BorderLayout());
        quizPanel.setBackground(new Color(248, 248, 255));
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        infoPanel.setBackground(new Color(70, 130, 180));
        
        questionCountLabel = new JLabel();
        questionCountLabel.setForeground(Color.WHITE);
        questionCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        categoryLabel = new JLabel();
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        difficultyLabel = new JLabel();
        difficultyLabel.setForeground(Color.WHITE);
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        infoPanel.add(questionCountLabel);
        infoPanel.add(timerLabel);
        infoPanel.add(categoryLabel);
        infoPanel.add(difficultyLabel);
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("Progress");
        
        headerPanel.add(infoPanel, BorderLayout.NORTH);
        headerPanel.add(progressBar, BorderLayout.SOUTH);
        
        // Question Panel
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setVerticalAlignment(JLabel.TOP);
        questionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel answersPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        answersPanel.setBackground(Color.WHITE);
        
        answerGroup = new ButtonGroup();
        answerButtons = new JRadioButton[4];
        
        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JRadioButton();
            answerButtons[i].setFont(new Font("Arial", Font.PLAIN, 16));
            answerButtons[i].setBackground(Color.WHITE);
            answerButtons[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            answerGroup.add(answerButtons[i]);
            answersPanel.add(answerButtons[i]);
        }
        
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        questionPanel.add(answersPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(248, 248, 255));
        
        previousButton = new JButton("Previous");
        previousButton.setFont(new Font("Arial", Font.BOLD, 14));
        previousButton.addActionListener(e -> previousQuestion());
        
        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextButton.addActionListener(e -> nextQuestion());
        
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        
        quizPanel.add(headerPanel, BorderLayout.NORTH);
        quizPanel.add(questionPanel, BorderLayout.CENTER);
        quizPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(quizPanel, "QUIZ");
    }
    
    private void createResultScreen() {
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(new Color(240, 248, 255));
        
        mainPanel.add(resultPanel, "RESULT");
    }
    
    private void startQuiz() {
        // Show loading dialog
        JDialog loadingDialog = new JDialog(this, "Loading...", true);
        JLabel loadingLabel = new JLabel("Fetching questions from server...", JLabel.CENTER);
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        loadingDialog.add(loadingLabel);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(this);
        
        SwingWorker<List<Question>, Void> worker = new SwingWorker<List<Question>, Void>() {
            @Override
            protected List<Question> doInBackground() throws Exception {
                String category = categoryCombo.getSelectedItem().toString().toLowerCase();
                String difficulty = difficultyCombo.getSelectedItem().toString().toLowerCase();
                int count = (Integer) questionCountSpinner.getValue();
                
                return QuizAPI.fetchQuestions(count, category, difficulty);
            }
            
            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    questions = get();
                    if (questions.isEmpty()) {
                        JOptionPane.showMessageDialog(QuizGUI.this,
                            "Failed to fetch questions. Please check your internet connection.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    userAnswers = new String[questions.size()];
                    currentQuestionIndex = 0;
                    startTime = System.currentTimeMillis();
                    timeElapsed = 0;
                    
                    startTimer();
                    displayQuestion();
                    cardLayout.show(mainPanel, "QUIZ");
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(QuizGUI.this,
                        "Error loading questions: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
        loadingDialog.setVisible(true);
    }
    
    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeElapsed++;
            int minutes = timeElapsed / 60;
            int seconds = timeElapsed % 60;
            timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
        });
        timer.start();
    }
    
    private void displayQuestion() {
        if (questions == null || questions.isEmpty()) return;
        
        Question currentQuestion = questions.get(currentQuestionIndex);
        
        // Update header info
        questionCountLabel.setText(String.format("Question %d of %d", 
            currentQuestionIndex + 1, questions.size()));
        categoryLabel.setText("Category: " + currentQuestion.getCategory());
        difficultyLabel.setText("Difficulty: " + 
            currentQuestion.getDifficulty().substring(0, 1).toUpperCase() + 
            currentQuestion.getDifficulty().substring(1));
        
        // Update progress bar
        progressBar.setValue((currentQuestionIndex + 1) * 100 / questions.size());
        progressBar.setString(String.format("Progress: %d%%", 
            (currentQuestionIndex + 1) * 100 / questions.size()));
        
        // Display question and answers
        questionLabel.setText("<html><body style='width: 600px'>" + 
            currentQuestion.getQuestion() + "</body></html>");
        
        List<String> answers = currentQuestion.getAllAnswers();
        for (int i = 0; i < answerButtons.length && i < answers.size(); i++) {
            answerButtons[i].setText(answers.get(i));
            answerButtons[i].setVisible(true);
            
            // Restore previous selection
            if (userAnswers[currentQuestionIndex] != null && 
                userAnswers[currentQuestionIndex].equals(answers.get(i))) {
                answerButtons[i].setSelected(true);
            }
        }
        
        // Hide unused buttons
        for (int i = answers.size(); i < answerButtons.length; i++) {
            answerButtons[i].setVisible(false);
        }
        
        // Update button states
        previousButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setText(currentQuestionIndex == questions.size() - 1 ? "Finish" : "Next");
    }
    
    private void saveCurrentAnswer() {
        for (JRadioButton button : answerButtons) {
            if (button.isSelected()) {
                userAnswers[currentQuestionIndex] = button.getText();
                break;
            }
        }
    }
    
    private void nextQuestion() {
        saveCurrentAnswer();
        
        if (currentQuestionIndex == questions.size() - 1) {
            finishQuiz();
        } else {
            currentQuestionIndex++;
            answerGroup.clearSelection();
            displayQuestion();
        }
    }
    
    private void previousQuestion() {
        saveCurrentAnswer();
        currentQuestionIndex--;
        answerGroup.clearSelection();
        displayQuestion();
    }
    
    private void finishQuiz() {
        timer.stop();
        
        int correctAnswers = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] != null && questions.get(i).isCorrect(userAnswers[i])) {
                correctAnswers++;
            }
        }
        
        QuizResult result = new QuizResult(questions.size(), correctAnswers, timeElapsed);
        showResults(result);
    }
    
    private void showResults(QuizResult result) {
        // Remove existing result panel and create new one
        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            if (mainPanel.getComponentZOrder(comp) == 2) { // Result panel is 3rd component
                mainPanel.remove(comp);
                break;
            }
        }
        
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(new Color(240, 248, 255));
        
        // Title
        JLabel titleLabel = new JLabel("Quiz Results", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        // Results Panel
        JPanel resultsPanel = new JPanel(new GridBagLayout());
        resultsPanel.setBackground(Color.WHITE);
        resultsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Your Score", 0, 0, new Font("Arial", Font.BOLD, 16),
            new Color(70, 130, 180)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Score display
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel scoreLabel = new JLabel(String.format("%.1f%%", result.getPercentage()));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 48));
        scoreLabel.setForeground(result.getPercentage() >= 70 ? new Color(34, 139, 34) : Color.RED);
        resultsPanel.add(scoreLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Grade
        gbc.gridx = 0; gbc.gridy = 1;
        resultsPanel.add(new JLabel("Grade:"), gbc);
        gbc.gridx = 1;
        JLabel gradeLabel = new JLabel(result.getGrade());
        gradeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultsPanel.add(gradeLabel, gbc);
        
        // Correct answers
        gbc.gridx = 0; gbc.gridy = 2;
        resultsPanel.add(new JLabel("Correct Answers:"), gbc);
        gbc.gridx = 1;
        resultsPanel.add(new JLabel(result.getCorrectAnswers() + " / " + result.getTotalQuestions()), gbc);
        
        // Wrong answers
        gbc.gridx = 0; gbc.gridy = 3;
        resultsPanel.add(new JLabel("Wrong Answers:"), gbc);
        gbc.gridx = 1;
        resultsPanel.add(new JLabel(String.valueOf(result.getWrongAnswers())), gbc);
        
        // Time taken
        gbc.gridx = 0; gbc.gridy = 4;
        resultsPanel.add(new JLabel("Time Taken:"), gbc);
        gbc.gridx = 1;
        int minutes = (int) result.getTimeTaken() / 60;
        int seconds = (int) result.getTimeTaken() % 60;
        resultsPanel.add(new JLabel(String.format("%02d:%02d", minutes, seconds)), gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(240, 248, 255));
        
        JButton newQuizButton = new JButton("New Quiz");
        newQuizButton.setFont(new Font("Arial", Font.BOLD, 14));
        newQuizButton.setBackground(new Color(34, 139, 34));
        newQuizButton.setForeground(Color.WHITE);
        newQuizButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "START");
            if (timer != null) timer.stop();
        });
        
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(newQuizButton);
        buttonPanel.add(exitButton);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        centerPanel.add(resultsPanel, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        
        resultPanel.add(titleLabel, BorderLayout.NORTH);
        resultPanel.add(centerPanel, BorderLayout.CENTER);
        resultPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(resultPanel, "RESULT");
        cardLayout.show(mainPanel, "RESULT");
    }
}
