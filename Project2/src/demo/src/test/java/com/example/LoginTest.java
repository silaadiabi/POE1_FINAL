package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginTest {
    private Login login;

    @BeforeEach
    public void setUp() {
        login = new Login();
    }

    // === Username Validation Tests ===
    @Test
    public void testValidUsernameWithUnderscoreAndFiveCharacters() {
        assertTrue(login.checkUserName("Ky_1"), "Username with underscore and 4 chars should be valid");
        assertTrue(login.checkUserName("User_"), "Username with underscore and 5 chars should be valid");
    }

    @Test
    public void testInvalidUsernameWithoutUnderscore() {
        assertFalse(login.checkUserName("Kyle1"), "Username without underscore should be invalid");
        assertFalse(login.checkUserName("User1"), "Username without underscore should be invalid");
    }

    @Test
    public void testInvalidUsernameTooLong() {
        assertFalse(login.checkUserName("Kyle_123"), "Username longer than 5 chars should be invalid");
        assertFalse(login.checkUserName("VeryLongUsername_"), "Username longer than 5 chars should be invalid");
    }

    @Test
    public void testUsernameEdgeCases() {
        assertTrue(login.checkUserName("A_B"), "Short username with underscore should be valid");
        assertFalse(login.checkUserName(""), "Empty username should be invalid");
        assertFalse(login.checkUserName("_"), "Only underscore should be invalid");
        assertFalse(login.checkUserName("ABCDE"), "5 chars without underscore should be invalid");
    }

    // === Password Complexity Tests ===
    @Test
    public void testValidPasswordWithAllRequirements() {
        assertTrue(login.checkPasswordComplexity("Pass123!"), 
            "Password with 8+ chars, uppercase, number, and special char should be valid");
        assertTrue(login.checkPasswordComplexity("Complex@123"), 
            "Password with all requirements should be valid");
    }

    @Test
    public void testInvalidPasswordTooShort() {
        assertFalse(login.checkPasswordComplexity("Short1!"), 
            "Password shorter than 8 chars should be invalid");
    }

    @Test
    public void testInvalidPasswordMissingUppercase() {
        assertFalse(login.checkPasswordComplexity("password123!"), 
            "Password without uppercase should be invalid");
    }

    @Test
    public void testInvalidPasswordMissingNumber() {
        assertFalse(login.checkPasswordComplexity("Password!"), 
            "Password without number should be invalid");
    }

    @Test
    public void testInvalidPasswordMissingSpecialCharacter() {
        assertFalse(login.checkPasswordComplexity("Password123"), 
            "Password without special character should be invalid");
    }

    @Test
    public void testPasswordEdgeCases() {
        assertFalse(login.checkPasswordComplexity(""), "Empty password should be invalid");
        assertFalse(login.checkPasswordComplexity("A1!"), "Too short password should be invalid");
        assertTrue(login.checkPasswordComplexity("A1!bcdef"), "Exactly 8 chars with all requirements should be valid");
    }

    // === Phone Number Validation Tests ===
    @Test
    public void testValidSouthAfricanPhoneNumbers() {
        assertTrue(login.checkCellPhoneNumber("+27831234567"), 
            "Valid SA number with +27 and 9 digits should be valid");
        assertTrue(login.checkCellPhoneNumber("+27761234567"), 
            "Valid SA number with +27 and 9 digits should be valid");
    }

    @Test
    public void testInvalidPhoneNumberWrongCountryCode() {
        assertFalse(login.checkCellPhoneNumber("+25831234567"), 
            "Number with wrong country code should be invalid");
        assertFalse(login.checkCellPhoneNumber("0731234567"), 
            "Number without +27 should be invalid");
    }

    @Test
    public void testInvalidPhoneNumberWrongLength() {
        assertFalse(login.checkCellPhoneNumber("+2783123456"), 
            "Number with only 8 digits should be invalid");
        assertFalse(login.checkCellPhoneNumber("+278312345678"), 
            "Number with 11 digits should be invalid");
    }

    @Test
    public void testInvalidPhoneNumberFormat() {
        assertFalse(login.checkCellPhoneNumber("27831234567"), 
            "Number without + should be invalid");
        assertFalse(login.checkCellPhoneNumber("+27 831234567"), 
            "Number with space should be invalid");
        assertFalse(login.checkCellPhoneNumber(""), 
            "Empty phone number should be invalid");
    }

    // === User Registration Tests ===
    @Test
    public void testSuccessfulUserRegistration() {
        String result = login.registerUser("Ky_1", "Pass123!", "+27831234567");
        assertEquals("User registered successfully.", result, 
            "Valid registration should return success message");
    }

    @Test
    public void testRegistrationWithInvalidUsername() {
        String result = login.registerUser("Kyle", "Pass123!", "+27831234567");
        assertTrue(result.contains("Username incorrectly formatted"), 
            "Invalid username should return error message");
    }

    @Test
    public void testRegistrationWithInvalidPassword() {
        String result = login.registerUser("Ky_1", "password", "+27831234567");
        assertTrue(result.contains("Password incorrectly formatted"), 
            "Invalid password should return error message");
    }

    @Test
    public void testRegistrationWithInvalidPhone() {
        String result = login.registerUser("Ky_1", "Pass123!", "0831234567");
        assertTrue(result.contains("Cell phone number incorrectly formatted"), 
            "Invalid phone should return error message");
    }

    @Test
    public void testRegistrationWithMultipleErrors() {
        String result = login.registerUser("Kyle", "pass", "083123");
        assertTrue(result.contains("Username incorrectly formatted"), 
            "Should contain username error");
        assertTrue(result.contains("Password incorrectly formatted"), 
            "Should contain password error");
        assertTrue(result.contains("Cell phone number incorrectly formatted"), 
            "Should contain phone error");
    }

    // === Login Tests ===
    @Test
    public void testSuccessfulLogin() {
        login.registerUser("Ky_1", "Pass123!", "+27831234567");
        assertTrue(login.loginUser("Ky_1", "Pass123!"), 
            "Login with correct credentials should succeed");
    }

    @Test
    public void testLoginWithWrongPassword() {
        login.registerUser("Ky_1", "Pass123!", "+27831234567");
        assertFalse(login.loginUser("Ky_1", "WrongPass!"), 
            "Login with wrong password should fail");
    }

    @Test
    public void testLoginWithWrongUsername() {
        login.registerUser("Ky_1", "Pass123!", "+27831234567");
        assertFalse(login.loginUser("WrongUser", "Pass123!"), 
            "Login with wrong username should fail");
    }

    @Test
    public void testLoginWithoutRegistration() {
        assertFalse(login.loginUser("NonExistent", "Password123!"), 
            "Login without registration should fail");
    }

    // === Login Status Tests ===
    @Test
    public void testSuccessfulLoginStatus() {
        login.registerUser("Ky_1", "Pass123!", "+27831234567");
        String status = login.returnLoginStatus("Ky_1", "Pass123!");
        assertTrue(status.contains("Welcome") && status.contains("it is great to see you again"), 
            "Successful login should return welcome message");
    }

    @Test
    public void testFailedLoginStatus() {
        login.registerUser("Ky_1", "Pass123!", "+27831234567");
        String status = login.returnLoginStatus("Ky_1", "WrongPass");
        assertEquals("Username or password incorrect, please try again.", status, 
            "Failed login should return error message");
    }

    @Test
    public void testLoginStatusWithUnregisteredUser() {
        String status = login.returnLoginStatus("NonExistent", "Password");
        assertEquals("Username or password incorrect, please try again.", status, 
            "Unregistered user login should return error message");
    }
}