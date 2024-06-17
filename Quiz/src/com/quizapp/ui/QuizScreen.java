package com.quizapp.ui;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.service.QuizService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class QuizScreen {
    private Stage primaryStage;
    private QuizService quizService;
    private VBox view;
    private Quiz quiz;
    private int currentQuestionIndex;
    private List<RadioButton> optionButtons;
    private ToggleGroup optionsGroup;
    private Label questionLabel;
    private Label feedbackLabel;
    private Button nextButton;
    private int score;

    public QuizScreen(){}

    public QuizScreen(Stage primaryStage, Quiz quiz) {
        this.primaryStage = primaryStage;
        this.quizService = new QuizService();
        this.quiz = quiz;
        this.currentQuestionIndex = 0;
        this.optionButtons = new ArrayList<>();
        this.optionsGroup = new ToggleGroup();
        this.view = new VBox(10);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.CENTER);

        createQuizScreen();
        showQuestion(currentQuestionIndex);
    }

    private void createQuizScreen() {
        questionLabel = new Label();
        questionLabel.setWrapText(true);
        questionLabel.setStyle("-fx-font-size: 18px;");

        GridPane optionsPane = new GridPane();
        optionsPane.setHgap(10);
        optionsPane.setVgap(10);
        optionsPane.setAlignment(Pos.CENTER);

        for (int i = 0; i < 4; i++) { 
            RadioButton optionButton = new RadioButton();
            optionButton.setToggleGroup(optionsGroup);
            optionButtons.add(optionButton);
            optionsPane.add(optionButton, 0, i);
        }

        nextButton = new Button("Next");
        nextButton.setOnAction(event -> handleNext());

        feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-text-fill: red;");

        view.getChildren().addAll(questionLabel, optionsPane, feedbackLabel, nextButton);
    }

    private void showQuestion(int index) {
        if (index < quiz.getQuestions().size()) {
            Question question = quiz.getQuestions().get(index);
            questionLabel.setText(question.getTitle());

            List<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                optionButtons.get(i).setText(options.get(i));
                optionButtons.get(i).setVisible(true);
            }
            for (int i = options.size(); i < 4; i++) {
                optionButtons.get(i).setVisible(false);
            }
            feedbackLabel.setText("");
        } else {
            endQuiz();
        }
    }

    private void handleNext() {
        if (currentQuestionIndex < quiz.getQuestions().size()) {
            Question question = quiz.getQuestions().get(currentQuestionIndex);
            int selectedIndex = getSelectedOptionIndex();

            if (selectedIndex == -1) {
                feedbackLabel.setText("Please select an option.");
                return;
            }

            if (selectedIndex == question.getCorrectAnswerIndex()) {
                feedbackLabel.setText("Correct!");
                score++;
            } else {
                feedbackLabel.setText("Incorrect! The correct answer is: " + question.getOptions().get(question.getCorrectAnswerIndex()));
            }

            currentQuestionIndex++;
            if (currentQuestionIndex < quiz.getQuestions().size()) {
                showQuestion(currentQuestionIndex);
            } else {
                endQuiz();
            }
        }
    }

    private int getSelectedOptionIndex() {
        for (int i = 0; i < optionButtons.size(); i++) {
            if (optionButtons.get(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    private void endQuiz() {
        showAlert(Alert.AlertType.INFORMATION, "Quiz Completed", "You scored " + score + " out of " + quiz.getQuestions().size());
        // Optionally, you can navigate back to the main screen or any other action.
        LoginScreen mainScreen = new LoginScreen(primaryStage); // Assuming a MainScreen exists
        primaryStage.setScene(new Scene(mainScreen.getView(), 800, 600));
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}
