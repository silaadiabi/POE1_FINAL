package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTest {
    
    @Test
    public void testMessageCreation() {
        Message message = new Message(1, "+27831234567", "Hello World");
        
        assertNotNull(message.getMessageID());
        assertEquals(1, message.getMessageNumber());
        assertEquals("+27831234567", message.getRecipientPhoneNumber());
        assertEquals("Hello World", message.getMessageText());
        assertNotNull(message.getMessageHash());
    }
    
    @Test
    public void testMessageIDGeneration() {
        Message message = new Message(5, "+27831234567", "Test Message");
        String messageID = message.getMessageID();
        
        assertNotNull(messageID);
        assertTrue(messageID.length() >= 3);
        assertTrue(messageID.startsWith("005")); // Should start with padded message number
    }
    
    @Test
    public void testMessageHashGeneration() {
        Message message = new Message(1, "+27831234567", "Hello World");
        String hash = message.getMessageHash();
        
        assertNotNull(hash);
        assertTrue(hash.contains(":"));
        assertTrue(hash.contains("1:")); // Should contain message number
    }
    
    @Test
    public void testMessageWithEmptyContent() {
        Message message = new Message(2, "+27831234567", "");
        
        assertNotNull(message.getMessageID());
        assertEquals("", message.getMessageText());
        assertNotNull(message.getMessageHash());
    }
    
    @Test
    public void testMessageToString() {
        Message message = new Message(1, "+27831234567", "Test Message");
        String stringRepresentation = message.toString();
        
        assertTrue(stringRepresentation.contains("MessageID"));
        assertTrue(stringRepresentation.contains("Message Hash"));
        assertTrue(stringRepresentation.contains("Recipient Phone"));
        assertTrue(stringRepresentation.contains("Message"));
    }
}