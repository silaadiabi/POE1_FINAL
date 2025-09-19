package com.example;

public class Login {

    // --- Stored user details ---
    private String Username;
    private String Password;
    private String Phone;
    private String FirstName;
    private String LastName;

    // Inner class Validator (kept inside Login.java)
    public static class Validator {

        //  Check username
        public boolean checkUserName(String username) {
            if (username.contains("_") && username.length() <= 5) {
                System.out.println("Username successfully captured");
                return true;
            } else {
                System.out.println("Username is not correctly formatted, " +
                                   "please ensure that your username contains an underscore and " +
                                   "is no more than five characters in length.");
                return false;
            }
        }

        //  Check password complexity
        public boolean checkPasswordComplexity(String password) {
            boolean hasUpper = false;
            boolean hasDigit = false;
            boolean hasSpecial = false;

            for (char c : password.toCharArray()) {
                if (Character.isUpperCase(c)) hasUpper = true;
                if (Character.isDigit(c)) hasDigit = true;
                if (!Character.isLetterOrDigit(c)) hasSpecial = true;
            }

            if (password.length() >= 8 && hasUpper && hasDigit && hasSpecial) {
                System.out.println("Password successfully captured");
                return true;
            } else {
                System.out.println("Password is not correctly formatted; " +
                                   "please ensure that the password contains at least eight characters, " +
                                   "a capital letter, a number, and a special character.");
                return false;
            }
        }

        //  Check South African cell phone number
        public boolean checkCellPhoneNumber(String phone) {
            if (phone.startsWith("+27") && phone.length() == 12) {
                System.out.println("Phone number successfully captured");
                return true;
            } else {
                System.out.println("Phone number is not correctly formatted; " +
                                   "please ensure it starts with +27 and is followed by 9 digits.");
                return false;
            }
        }
    }

    // Object of Validator
    private Validator validator = new Validator();

    //  Register user
    public String registerUser(String firstName, String lastName, String username, String password, String phone) {
        boolean validUser = validator.checkUserName(username);
        boolean validPass = validator.checkPasswordComplexity(password);
        boolean validPhone = validator.checkCellPhoneNumber(phone);

        if (validUser && validPass && validPhone) {
            FirstName = firstName;
            LastName = lastName;
            Username = username;
            Password = password;
            Phone = phone;
            return "User registered successfully!";
        } else {
            return "Registration failed. Please try again.";
        }
    }

    //  Login user
    public boolean loginUser(String username, String password) {
        return username.equals(Username) && password.equals(Password);
    }

    //  Return login status
    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + FirstName + " " + LastName +
                   ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}

