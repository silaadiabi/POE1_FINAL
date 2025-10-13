package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private Login login;

    @BeforeEach
    public void setup() {
        login = new Login();
    }

    // === 🧪 TEST 1: USERNAME VALIDATION ===
    @Test
    public void testValidUsername() {
        assertTrue(login.checkUserName("John_"), 
                "Username with underscore and <= 5 chars should be valid");
    }

    @Test
    public void testInvalidUsername_NoUnderscore() {
        assertFalse(login.checkUserName("John"), 
                "Username without underscore should be invalid");
    }

    @Test
    public void testInvalidUsername_TooLong() {
        assertFalse(login.checkUserName("Johny_"), 
                "Username longer than 5 chars should be invalid");
    }

    // === 🧪 TEST 2: PASSWORD COMPLEXITY ===
    @Test
    public void testValidPassword() {
        assertTrue(login.checkPasswordComplexity("Pass@123"), 
                "Password with upper, number, and special char should be valid");
    }

    @Test
    public void testInvalidPassword_NoUppercase() {
        assertFalse(login.checkPasswordComplexity("password@123"), 
                "Password without uppercase should be invalid");
    }

    @Test
    public void testInvalidPassword_NoNumber() {
        assertFalse(login.checkPasswordComplexity("Password@"), 
                "Password without number should be invalid");
    }

    @Test
    public void testInvalidPassword_NoSpecialCharacter() {
        assertFalse(login.checkPasswordComplexity("Password1"), 
                "Password without special char should be invalid");
    }

    @Test
    public void testInvalidPassword_ShortLength() {
        assertFalse(login.checkPasswordComplexity("Pa@1"), 
                "Password shorter than 8 chars should be invalid");
    }

    // === 🧪 TEST 3: CELL PHONE VALIDATION ===
    @Test
    public void testValidCellPhone() {
        assertTrue(login.checkCellPhoneNumber("+27821234567"), 
                "Cell number with +27 and 9–10 digits should be valid");
    }

    @Test
    public void testInvalidCellPhone_NoCountryCode() {
        assertFalse(login.checkCellPhoneNumber("0821234567"), 
                "Cell number without +27 should be invalid");
    }

    @Test
    public void testInvalidCellPhone_WrongLength() {
        assertFalse(login.checkCellPhoneNumber("+278212345"), 
                "Cell number with too few digits should be invalid");
    }

    // === 🧪 TEST 4: USER REGISTRATION ===
    @Test
    public void testSuccessfulRegistration() {
        String response = login.registerUser("John_", "Pass@123", "+27821234567");
        assertEquals("User registered successfully.", response);
    }

    @Test
    public void testRegistration_InvalidUsername() {
        String response = login.registerUser("John", "Pass@123", "+27821234567");
        assertTrue(response.contains("Username incorrectly formatted"),
                "Should show username format error");
    }

    @Test
    public void testRegistration_InvalidPassword() {
        String response = login.registerUser("John_", "pass1234", "+27821234567");
        assertTrue(response.contains("Password incorrectly formatted"),
                "Should show password complexity error");
    }

    @Test
    public void testRegistration_InvalidPhone() {
        String response = login.registerUser("John_", "Pass@123", "0821234567");
        assertTrue(response.contains("Cell phone number incorrectly formatted"),
                "Should show cell phone format error");
    }

    @Test
    public void testRegistration_MultipleErrors() {
        String response = login.registerUser("John", "pass", "082");
        assertTrue(response.contains("Username incorrectly formatted"));
        assertTrue(response.contains("Password incorrectly formatted"));
        assertTrue(response.contains("Cell phone number incorrectly formatted"));
    }

    // === 🧪 TEST 5: LOGIN VALIDATION ===
    @Test
    public void testLoginSuccess() {
        login.registerUser("Mike_", "Test@123", "+27824567890");
        assertTrue(login.loginUser("Mike_", "Test@123"));
    }

    @Test
    public void testLoginFailure_WrongPassword() {
        login.registerUser("Mike_", "Test@123", "+27824567890");
        assertFalse(login.loginUser("Mike_", "WrongPass"));
    }

    @Test
    public void testLoginFailure_WrongUsername() {
        login.registerUser("Mike_", "Test@123", "+27824567890");
        assertFalse(login.loginUser("John_", "Test@123"));
    }

    // === 🧪 TEST 6: RETURN LOGIN STATUS ===
    @Test
    public void testReturnLoginStatus_Success() {
        login.registerUser("Mike_Smith", "Test@123", "+27824567890");
        String message = login.returnLoginStatus("Mike_Smith", "Test@123");
        assertTrue(message.startsWith("Welcome Mike Smith"),
                "Expected welcome message with name");
    }

    @Test
    public void testReturnLoginStatus_Failure() {
        login.registerUser("Mike_Smith", "Test@123", "+27824567890");
        String message = login.returnLoginStatus("Mike_Smith", "WrongPass");
        assertEquals("Username or password incorrect, please try again.", message);
    }
}
