package com.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginTest {

    @Test
    public void registerAndLoginSuccess() {
        Login login = new Login();
        String reg = login.registerUser("user1", "pass1", "+27123456789");
        Assertions.assertEquals("User registered successfully.", reg);

        boolean ok = login.loginUser("user1", "pass1");
        Assertions.assertTrue(ok);

        Assertions.assertEquals("+27123456789", login.getPhoneForUser("user1"));
    }

    @Test
    public void registerDuplicateAndInvalidPhone() {
        Login login = new Login();
        String reg1 = login.registerUser("user2", "pw", "+27111111111");
        Assertions.assertEquals("User registered successfully.", reg1);

        String reg2 = login.registerUser("user2", "pw2", "+27123456789");
        Assertions.assertEquals("Username already exists.", reg2);

        String bad = login.registerUser("user3", "pw", "0712345678"); // invalid format
        Assertions.assertEquals("Phone must be in +27 format with 9 digits after country code.", bad);
    }

    @Test
    public void loginFailure() {
        Login login = new Login();
        login.registerUser("u", "p", "+27123456789");
        Assertions.assertFalse(login.loginUser("u", "wrong"));
        Assertions.assertFalse(login.loginUser("nouser", "p"));
    }
}
