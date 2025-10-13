package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Login {
    private String registeredUsername;
    private String registeredPassword;
    private String registeredPhone;

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) return false;

        boolean hasUpper = false;
        boolean hasNum = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isDigit(c)) hasNum = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasUpper && hasNum && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String phone) {
        String regex = "^\\+27\\d{9,10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public String registerUser(String username, String password, String phoneNumber) {
        String errorMessage = "";

        if (!checkUserName(username)) {
            errorMessage += "Username incorrectly formatted. It must contain an underscore and be no more than 5 characters long.\n";
        }

        if (!checkPasswordComplexity(password)) {
            errorMessage += "Password incorrectly formatted. It must have at least 8 characters, a capital letter, a number, and a special character.\n";
        }

        if (!checkCellPhoneNumber(phoneNumber)) {
            errorMessage += "Cell phone number incorrectly formatted. Must include +27 and 9–10 digits.\n";
        }

        if (!errorMessage.isEmpty()) {
            return errorMessage.trim();
        }

        registeredUsername = username;
        registeredPassword = password;
        registeredPhone = phoneNumber;

        return "User registered successfully.";
    }

    public boolean loginUser(String username, String password) {
        return registeredUsername != null &&
                registeredUsername.equals(username) &&
                registeredPassword.equals(password);
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            String[] parts = username.split("_", 2);
            String firstName = parts.length > 0 ? parts[0] : "User";
            String lastName = parts.length > 1 ? parts[1] : "";
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }
}
