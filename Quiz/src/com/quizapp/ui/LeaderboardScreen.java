package com.quizapp.ui;

import com.quizapp.model.User;
import com.quizapp.service.QuizService;
import com.quizapp.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import java.sql.SQLException;
import java.util.List;

public class LeaderboardScreen {
    private Stage primaryStage;
    private UserService userService;
    private QuizService quizService;
    private VBox view;
    private TableView<User> leaderboardTable;

    public LeaderboardScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userService = new UserService();
        this.quizService = new QuizService();
        this.view = new VBox(10);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.CENTER);

        createLeaderboardScreen();
    }

    private void createLeaderboardScreen() {
        leaderboardTable = new TableView<>();

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("totalScore"));

        TableColumn<User, Integer> quizCountColumn = new TableColumn<>("Quizzes Taken");
        quizCountColumn.setCellValueFactory(new PropertyValueFactory<>("quizCount"));

        leaderboardTable.getColumns().addAll(usernameColumn, scoreColumn, quizCountColumn);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> goBack());

        view.getChildren().addAll(leaderboardTable, backButton);
    }

    private void goBack() {
        QuizScreen mainScreen = new QuizScreen(primaryStage, null); // Assuming MainScreen class exists
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
