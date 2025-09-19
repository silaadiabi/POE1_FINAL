package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginTest {

    private Login login;
    private Login.Validator validator;

    @BeforeEach
    public void setUp() {
        login = new Login();
        validator = new Login.Validator();
    }

    // --- Validator tests ---
    @Test
    public void testValidUsername() {
        assertTrue(validator.checkUserName("Kyl_1"));
        assertFalse(validator.checkUserName("Kyle!!!!!!"));
      
    }

    @Test
    public void testPasswordComplexity() {
        assertTrue(validator.checkPasswordComplexity("Ch&&sec@ke99!"));
        assertFalse(validator.checkPasswordComplexity("Password")); // no uppercase
       
    }

    @Test
    public void testCellPhoneNumber() {
        assertTrue(validator.checkCellPhoneNumber("+27838968976"));
        assertFalse(validator.checkCellPhoneNumber("08966553")); // missing +27
       
    }

    // --- Registration tests ---
    @Test
    public void testSuccessfulRegistration() {
        String result = login.registerUser("John", "Doe", "Kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("User registered successfully!", result);
    }

    @Test
    public void testFailedRegistration() {
        String result = login.registerUser("John", "Doe", "Kyle!!!!!!", "Password", "08966553");
        assertEquals("Registration failed. Please try again.", result);
    }

    // --- Login tests ---
    @Test
    public void testLoginUser() {
        login.registerUser("John", "Doe", "Kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("Kyl_1", "Ch&&sec@ke99!"));
        assertFalse(login.loginUser("Kyle!!!!!!", "Password"));
       
    }

    @Test
    public void testReturnLoginStatus() {
        login.registerUser("John", "Doe", "Kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome John Doe, it is great to see you again.", 
                     login.returnLoginStatus("Kyl_1", "Ch&&sec@ke99!"));
        assertEquals("Username or password incorrect, please try again.", 
                     login.returnLoginStatus("Kyle!!!!!!", "Password"));
    }
}
