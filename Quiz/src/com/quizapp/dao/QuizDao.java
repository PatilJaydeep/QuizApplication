package com.quizapp.dao;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDao {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/quizapp";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "";

    private static final String INSERT_QUIZ_SQL = "INSERT INTO quizzes (title) VALUES (?)";
    private static final String SELECT_QUIZ_BY_ID = "SELECT * FROM quizzes WHERE id = ?";
    private static final String SELECT_ALL_QUIZZES = "SELECT * FROM quizzes";
    private static final String DELETE_QUIZ_SQL = "DELETE FROM quizzes WHERE id = ?";
    private static final String UPDATE_QUIZ_SQL = "UPDATE quizzes SET title = ? WHERE id = ?";
    private static final String INSERT_QUESTION_SQL = "INSERT INTO questions (quiz_id, title, options, correct_answer_index) VALUES (?, ?, ?, ?)";
    private static final String SELECT_QUESTIONS_BY_QUIZ_ID = "SELECT * FROM questions WHERE quiz_id = ?";

    // Create quiz
    public void createQuiz(Quiz quiz) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUIZ_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, quiz.getTitle());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating quiz failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    quiz.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating quiz failed, no ID obtained.");
                }
            }

            // Insert questions
            for (Question question : quiz.getQuestions()) {
                addQuestion(quiz.getId(), question);
            }
        }
    }

    // Retrieve quiz by ID
    public Quiz getQuiz(int id) throws SQLException {
        Quiz quiz = null;
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUIZ_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                quiz = new Quiz();
                quiz.setId(rs.getInt("id"));
                quiz.setTitle(rs.getString("title"));
                quiz.setQuestions(getQuestionsByQuizId(id));
            }
        }
        return quiz;
    }

    // Retrieve all quizzes
    public List<Quiz> getAllQuizzes() throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUIZZES)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setId(rs.getInt("id"));
                quiz.setTitle(rs.getString("title"));
                quiz.setQuestions(getQuestionsByQuizId(quiz.getId()));
                quizzes.add(quiz);
            }
        }
        return quizzes;
    }

    // Delete quiz
    public boolean deleteQuiz(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(DELETE_QUIZ_SQL)) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    // Update quiz
    public boolean updateQuiz(Quiz quiz) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUIZ_SQL)) {
            statement.setString(1, quiz.getTitle());
            statement.setInt(2, quiz.getId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // Add question to a quiz
    private void addQuestion(int quizId, Question question) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUESTION_SQL)) {
            preparedStatement.setInt(1, quizId);
            preparedStatement.setString(2, question.getTitle());
            preparedStatement.setString(3, String.join(",", question.getOptions()));
            preparedStatement.setInt(4, question.getCorrectAnswerIndex());
            preparedStatement.executeUpdate();
        }
    }

    // Retrieve questions by quiz ID
    private List<Question> getQuestionsByQuizId(int quizId) throws SQLException {
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
}
