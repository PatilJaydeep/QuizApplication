package com.quizapp.service;

import com.quizapp.dao.QuizDao;
import com.quizapp.model.Quiz;

import java.sql.SQLException;
import java.util.List;

public class QuizService {
    private QuizDao quizDao;

    public QuizService() {
        this.quizDao = new QuizDao();
    }

    // Create a new quiz
    public void createQuiz(Quiz quiz) throws SQLException {
        quizDao.createQuiz(quiz);
    }

    // Retrieve a quiz by ID
    public Quiz getQuiz(int id) throws SQLException {
        return quizDao.getQuiz(id);
    }

    // Retrieve all quizzes
    public List<Quiz> getAllQuizzes() throws SQLException {
        return quizDao.getAllQuizzes();
    }

    // Delete a quiz by ID
    public boolean deleteQuiz(int id) throws SQLException {
        return quizDao.deleteQuiz(id);
    }

    // Update a quiz
    public boolean updateQuiz(Quiz quiz) throws SQLException {
        return quizDao.updateQuiz(quiz);
    }
}
