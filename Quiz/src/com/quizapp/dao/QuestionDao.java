package com.quizapp.dao;

import com.quizapp.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/quizapp";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "";

    private static final String INSERT_QUESTION_SQL = "INSERT INTO questions (quiz_id, title, options, correct_answer_index) VALUES (?, ?, ?, ?)";
    private static final String SELECT_QUESTION_BY_ID = "SELECT * FROM questions WHERE id = ?";
    private static final String SELECT_QUESTIONS_BY_QUIZ_ID = "SELECT * FROM questions WHERE quiz_id = ?";
    private static final String DELETE_QUESTION_SQL = "DELETE FROM questions WHERE id = ?";
    private static final String UPDATE_QUESTION_SQL = "UPDATE questions SET title = ?, options = ?, correct_answer_index = ? WHERE id = ?";

    // Create question
    public void createQuestion(Question question, int quizId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUESTION_SQL)) {
            preparedStatement.setInt(1, quizId);
            preparedStatement.setString(2, question.getTitle());
            preparedStatement.setString(3, String.join(",", question.getOptions()));
            preparedStatement.setInt(4, question.getCorrectAnswerIndex());
            preparedStatement.executeUpdate();
        }
    }

    // Retrieve question by ID
    public Question getQuestion(int id) throws SQLException {
        Question question = null;
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUESTION_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                question = new Question();
                question.setId(rs.getInt("id"));
                question.setTitle(rs.getString("title"));
                question.setOptions(List.of(rs.getString("options").split(",")));
                question.setCorrectAnswerIndex(rs.getInt("correct_answer_index"));
            }
        }
        return question;
    }

    // Retrieve questions by quiz ID
    public List<Question> getQuestionsByQuizId(int quizId) throws SQLException {
        List<Question> questions = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUESTIONS_BY_QUIZ_ID)) {
            preparedStatement.setInt(1, quizId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getInt("id"));
                question.setTitle(rs.getString("title"));
                question.setOptions(List.of(rs.getString("options").split(",")));
                question.setCorrectAnswerIndex(rs.getInt("correct_answer_index"));
                questions.add(question);
            }
        }
        return questions;
    }

    // Delete question
    public boolean deleteQuestion(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(DELETE_QUESTION_SQL)) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    // Update question
    public boolean updateQuestion(Question question) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUESTION_SQL)) {
            statement.setString(1, question.getTitle());
            statement.setString(2, String.join(",", question.getOptions()));
            statement.setInt(3, question.getCorrectAnswerIndex());
            statement.setInt(4, question.getId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
}

