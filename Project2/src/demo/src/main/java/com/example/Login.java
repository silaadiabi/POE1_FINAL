package com.example;

import java.util.HashMap;
import java.util.Map;

public class Login {
    private final Map<String, User> users = new HashMap<>();
    
    private static class User {
        String username;
        String password;
        String phone;
        
        User(String username, String password, String phone) {
            this.username = username;
            this.password = password;
            this.phone = phone;
        }
    }
    
    public String registerUser(String username, String password, String phone) {
        if (username == null || username.trim().isEmpty()) {
            return "Username cannot be empty.";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Password cannot be empty.";
        }
        if (phone == null || !phone.matches("^\\+27\\d{9}$")) {
            return "Invalid phone number. Must be in +27 format with 9 following digits.";
        }
        if (users.containsKey(username)) {
            return "Username already exists.";
        }
        
        users.put(username, new User(username, password, phone));
        return "User registered successfully.";
    }
    
    public boolean loginUser(String username, String password) {
        User user = users.get(username);
        return user != null && user.password.equals(password);
    }
    
    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Login successful! Welcome, " + username + ".";
        } else {
            return "Login failed. Invalid username or password.";
        }
    }
    
    public String getPhoneForUser(String username) {
        User user = users.get(username);
        return user != null ? user.phone : null;
    }
}