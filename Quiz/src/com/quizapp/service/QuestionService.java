package com.quizapp.service;

import com.quizapp.dao.QuestionDao;
import com.quizapp.model.Question;

import java.sql.SQLException;
import java.util.List;

public class QuestionService {
    private QuestionDao questionDao;

    public QuestionService() {
        this.questionDao = new QuestionDao();
    }

    // Create a new question for a specific quiz
    public void createQuestion(Question question, int quizId) throws SQLException {
        questionDao.createQuestion(question, quizId);
    }

    // Retrieve a question by ID
    public Question getQuestion(int id) throws SQLException {
        return questionDao.getQuestion(id);
    }

    // Retrieve all questions for a specific quiz by quiz ID
    public List<Question> getQuestionsByQuizId(int quizId) throws SQLException {
        return questionDao.getQuestionsByQuizId(quizId);
    }

    // Delete a question by ID
    public boolean deleteQuestion(int id) throws SQLException {
        return questionDao.deleteQuestion(id);
    }

    // Update an existing question
    public boolean updateQuestion(Question question) throws SQLException {
        return questionDao.updateQuestion(question);
    }
}
