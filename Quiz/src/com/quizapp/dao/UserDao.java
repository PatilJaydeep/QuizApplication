package com.quizapp.dao;

import com.quizapp.model.User;

import java.sql.*;

public class UserDao {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/quizapp";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "";

    private static final String INSERT_USER_SQL = "INSERT INTO users (username, password) VALUES (?, ?)";
    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";

    // Register user
    public void registerUser(User user) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
        }
    }

    // Authenticate user
    public User authenticateUser(String username, String password) throws SQLException {
        User user = null;
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERNAME)) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next() && password.equals(rs.getString("password"))) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }
        }
        return user;
    }
}
