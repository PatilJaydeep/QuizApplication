package com.quizapp;

import com.quizapp.ui.LoginScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginScreen loginScreen = new LoginScreen(primaryStage);
        Scene scene = new Scene(loginScreen.getView(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Online Quiz Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
