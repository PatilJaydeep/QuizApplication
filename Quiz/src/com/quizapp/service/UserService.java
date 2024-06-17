package com.quizapp.service;

import com.quizapp.dao.UserDao;
import com.quizapp.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

public class UserService {
    private UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    // Register user with hashed password
    public void registerUser(User user) throws SQLException, NoSuchAlgorithmException {
        String salt = getSalt();
        String hashedPassword = getSecurePassword(user.getPassword(), salt);
        user.setPassword(hashedPassword + ":" + salt);
        userDao.registerUser(user);
    }

    // Authenticate user
    public User authenticateUser(String username, String password) throws SQLException, NoSuchAlgorithmException {
        User user = userDao.authenticateUser(username, password);
        if (user != null) {
            String[] parts = user.getPassword().split(":");
            String storedHash = parts[0];
            String storedSalt = parts[1];
            String providedHash = getSecurePassword(password, storedSalt);
            if (providedHash.equals(storedHash)) {
                return user;
            }
        }
        return null;
    }

    // Generate a salt
    private String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Hash a password with a salt
    private String getSecurePassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(Base64.getDecoder().decode(salt));
        byte[] bytes = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }

    public List<User> getTopScorers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopScorers'");
    }
}

