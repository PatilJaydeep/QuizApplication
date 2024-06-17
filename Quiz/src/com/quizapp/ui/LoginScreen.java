package com.quizapp.ui;

import com.quizapp.model.User;
import com.quizapp.service.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
    private UserService userService;
    private Stage primaryStage;
    private VBox view;

    public LoginScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userService = new UserService();
        this.view = new VBox(10);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.CENTER);

        createLoginScreen();
    }

    private void createLoginScreen() {
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText()));
        registerButton.setOnAction(event -> handleRegister(usernameField.getText(), passwordField.getText()));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 0, 2);
        gridPane.add(registerButton, 1, 2);

        view.getChildren().add(gridPane);
    }

    private void handleLogin(String username, String password) {
        try {
            User user = userService.authenticateUser(username, password);
            if (user != null) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
                QuizScreen mainScreen = new QuizScreen();
                primaryStage.setScene(new Scene(mainScreen.getView(), 800, 600));
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while logging in.");
        }
    }

    private void handleRegister(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username and password cannot be empty.");
            return;
        }
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            userService.registerUser(user);
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Account created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while registering.");
        }
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
