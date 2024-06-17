package com.quizapp.ui;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.service.QuestionService;
import com.quizapp.service.QuizService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class AdminScreen {
    private Stage primaryStage;
    private QuizService quizService;
    private QuestionService questionService;
    private VBox view;

    private ListView<Quiz> quizListView;
    private ListView<Question> questionListView;

    private TextField quizTitleField;
    private TextField questionTitleField;
    private TextField option1Field;
    private TextField option2Field;
    private TextField option3Field;
    private TextField option4Field;
    private ComboBox<Integer> correctAnswerComboBox;

    public AdminScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.quizService = new QuizService();
        this.questionService = new QuestionService();
        this.view = new VBox(10);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.CENTER);

        createAdminScreen();
        loadQuizzes();
    }

    private void createAdminScreen() {
        Label quizTitleLabel = new Label("Quiz Title:");
        quizTitleField = new TextField();

        Button addQuizButton = new Button("Add Quiz");
        addQuizButton.setOnAction(event -> handleAddQuiz());

        Button deleteQuizButton = new Button("Delete Quiz");
        deleteQuizButton.setOnAction(event -> handleDeleteQuiz());

        quizListView = new ListView<>();
        quizListView.setPrefHeight(150);
        quizListView.setOnMouseClicked(event -> loadQuestions());

        HBox quizButtonsBox = new HBox(10, addQuizButton, deleteQuizButton);
        quizButtonsBox.setAlignment(Pos.CENTER);

        Label questionTitleLabel = new Label("Question Title:");
        questionTitleField = new TextField();

        option1Field = new TextField();
        option2Field = new TextField();
        option3Field = new TextField();
        option4Field = new TextField();

        correctAnswerComboBox = new ComboBox<>();
        correctAnswerComboBox.setItems(FXCollections.observableArrayList(0, 1, 2, 3));

        Button addQuestionButton = new Button("Add Question");
        addQuestionButton.setOnAction(event -> handleAddQuestion());

        Button deleteQuestionButton = new Button("Delete Question");
        deleteQuestionButton.setOnAction(event -> handleDeleteQuestion());

        questionListView = new ListView<>();
        questionListView.setPrefHeight(150);

        HBox questionButtonsBox = new HBox(10, addQuestionButton, deleteQuestionButton);
        questionButtonsBox.setAlignment(Pos.CENTER);

        GridPane questionPane = new GridPane();
        questionPane.setHgap(10);
        questionPane.setVgap(10);
        questionPane.add(new Label("Option 1:"), 0, 0);
        questionPane.add(option1Field, 1, 0);
        questionPane.add(new Label("Option 2:"), 0, 1);
        questionPane.add(option2Field, 1, 1);
        questionPane.add(new Label("Option 3:"), 0, 2);
        questionPane.add(option3Field, 1, 2);
        questionPane.add(new Label("Option 4:"), 0, 3);
        questionPane.add(option4Field, 1, 3);
        questionPane.add(new Label("Correct Answer:"), 0, 4);
        questionPane.add(correctAnswerComboBox, 1, 4);

        view.getChildren().addAll(
            new Label("Manage Quizzes"), quizTitleLabel, quizTitleField, quizListView, quizButtonsBox,
            new Label("Manage Questions"), questionTitleLabel, questionTitleField, questionListView, questionButtonsBox, questionPane
        );
    }

    private void handleAddQuiz() {
        String quizTitle = quizTitleField.getText();
        if (!quizTitle.isEmpty()) {
            Quiz quiz = new Quiz();
            quiz.setTitle(quizTitle);
            try {
                quizService.createQuiz(quiz);
                loadQuizzes();
                quizTitleField.clear();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the quiz.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Quiz title cannot be empty.");
        }
    }

    private void handleDeleteQuiz() {
        Quiz selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuiz != null) {
            try {
                quizService.deleteQuiz(selectedQuiz.getId());
                loadQuizzes();
                questionListView.getItems().clear();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the quiz.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a quiz to delete.");
        }
    }

    private void handleAddQuestion() {
        Quiz selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a quiz to add questions.");
            return;
        }

        String questionTitle = questionTitleField.getText();
        List<String> options = List.of(
            option1Field.getText(), 
            option2Field.getText(), 
            option3Field.getText(), 
            option4Field.getText()
        );

        Integer correctAnswerIndex = correctAnswerComboBox.getValue();

        if (questionTitle.isEmpty() || options.stream().anyMatch(String::isEmpty) || correctAnswerIndex == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill all the fields and select a correct answer.");
            return;
        }

        Question question = new Question();
        question.setTitle(questionTitle);
        question.setOptions(options);
        question.setCorrectAnswerIndex(correctAnswerIndex);

        try {
            questionService.createQuestion(question, selectedQuiz.getId());
            loadQuestions();
            clearQuestionFields();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the question.");
        }
    }

    private void handleDeleteQuestion() {
        Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            try {
                questionService.deleteQuestion(selectedQuestion.getId());
                loadQuestions();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the question.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a question to delete.");
        }
    }

    private void loadQuizzes() {
        try {
            List<Quiz> quizzes = quizService.getAllQuizzes();
            ObservableList<Quiz> quizList = FXCollections.observableArrayList(quizzes);
            quizListView.setItems(quizList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading quizzes.");
        }
    }

    private void loadQuestions() {
        Quiz selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuiz != null) {
            try {
                List<Question> questions = questionService.getQuestionsByQuizId(selectedQuiz.getId());
                ObservableList<Question> questionList = FXCollections.observableArrayList(questions);
                questionListView.setItems(questionList);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading questions.");
            }
        }
    }

    private void clearQuestionFields() {
        questionTitleField.clear();
        option1Field.clear();
        option2Field.clear();
        option3Field.clear();
        option4Field.clear();
        correctAnswerComboBox.setValue(null);
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
