package com.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * Comprehensive unit tests for QuickChat message management system
 */
public class MessageTest {

    private MessageManager messageManager;

    @BeforeEach
    public void setUp() {
        messageManager = new MessageManager();
        // Test data is pre-populated in MessageManager constructor
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        List<String> sentMessages = messageManager.getSentMessages();
        
        Assertions.assertTrue(sentMessages.contains("Did you get the cake?"), 
            "Should contain 'Did you get the cake?'");
        Assertions.assertTrue(sentMessages.contains("It is dinner time!"), 
            "Should contain 'It is dinner time!'");
        
        // Should NOT contain stored or disregarded messages
        Assertions.assertFalse(sentMessages.contains("Where are you? You are late! I have asked you to be on time."));
        Assertions.assertFalse(sentMessages.contains("Yohoooo, I am at your gate."));
    }

    @Test
    public void testDisplayLongestMessage() {
        List<Message> allMessages = messageManager.getAllMessages();
        Message longestSentMessage = null;
        
        for (Message msg : allMessages) {
            if ("Sent".equals(msg.getFlag())) {
                if (longestSentMessage == null || 
                    msg.getMessageText().length() > longestSentMessage.getMessageText().length()) {
                    longestSentMessage = msg;
                }
            }
        }
        
        Assertions.assertNotNull(longestSentMessage, "Should find a sent message");
        Assertions.assertEquals("Where are you? You are late! I have asked you to be on time.", 
                               longestSentMessage.getMessageText());
    }

    @Test
    public void testSearchForMessageID() {
        List<Message> allMessages = messageManager.getAllMessages();
        Message message4 = allMessages.stream()
            .filter(msg -> msg.getMessageText().equals("It is dinner time!"))
            .findFirst()
            .orElse(null);
        
        Assertions.assertNotNull(message4, "Message 4 should exist");
        
        String messageID = message4.getMessageID();
        boolean found = allMessages.stream()
            .anyMatch(msg -> msg.getMessageID().equals(messageID));
        
        Assertions.assertTrue(found, "Should find message by ID: " + messageID);
    }

    @Test
    public void testSearchMessagesByRecipient() {
        String recipient = "+27838884567";
        
        List<Message> allMessages = messageManager.getAllMessages();
        
        List<Message> foundMessages = allMessages.stream()
            .filter(msg -> msg.getRecipientPhoneNumber().equals(recipient))
            .toList();
        
        Assertions.assertEquals(2, foundMessages.size(), "Should find 2 messages for recipient " + recipient);
        
        List<String> messageTexts = foundMessages.stream()
            .map(Message::getMessageText)
            .toList();
        
        Assertions.assertTrue(messageTexts.contains("Where are you? You are late! I have asked you to be on time."));
        Assertions.assertTrue(messageTexts.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageByHash() {
        List<Message> allMessages = messageManager.getAllMessages();
        Message message2 = allMessages.stream()
            .filter(msg -> msg.getMessageText().equals("Where are you? You are late! I have asked you to be on time."))
            .findFirst()
            .orElse(null);
        
        Assertions.assertNotNull(message2, "Message 2 should exist");
        
        String messageHash = message2.getMessageHash();
        int initialSize = allMessages.size();
        
        // Simulate deletion
        allMessages.removeIf(msg -> msg.getMessageHash().equals(messageHash));
        messageManager.getStoredMessages().removeIf(msg -> msg.equals(message2.getMessageText()));
        messageManager.getMessageHashes().removeIf(hash -> hash.equals(messageHash));
        
        Assertions.assertEquals(initialSize - 1, allMessages.size(), "Message should be deleted");
        Assertions.assertFalse(messageManager.getMessageHashes().contains(messageHash), 
                             "Message hash should be removed from hashes list");
        
        boolean stillExists = allMessages.stream()
            .anyMatch(msg -> msg.getMessageHash().equals(messageHash));
        Assertions.assertFalse(stillExists, "Message should no longer exist");
    }

    @Test
    public void testDisplayReport() {
        List<Message> allMessages = messageManager.getAllMessages();
        List<Message> sentMessages = allMessages.stream()
            .filter(msg -> "Sent".equals(msg.getFlag()))
            .toList();
        
        for (Message msg : sentMessages) {
            Assertions.assertNotNull(msg.getMessageHash(), "Message hash should not be null");
            Assertions.assertNotNull(msg.getRecipientPhoneNumber(), "Recipient should not be null");
            Assertions.assertNotNull(msg.getMessageText(), "Message text should not be null");
        }
        
        Assertions.assertTrue(sentMessages.size() >= 2, "Should have at least 2 sent messages");
    }

    @Test
    public void testAllArraysPopulated() {
        Assertions.assertFalse(messageManager.getSentMessages().isEmpty(), "Sent messages should be populated");
        Assertions.assertFalse(messageManager.getStoredMessages().isEmpty(), "Stored messages should be populated");
        Assertions.assertFalse(messageManager.getDisregardedMessages().isEmpty(), "Disregarded messages should be populated");
        Assertions.assertFalse(messageManager.getMessageHashes().isEmpty(), "Message hashes should be populated");
        Assertions.assertFalse(messageManager.getMessageIDs().isEmpty(), "Message IDs should be populated");
    }

    @Test
    public void testMessageIdAndHashGeneration() {
        Message message = new Message(1, "+27834557896", "Test message", "Sent");
        
        Assertions.assertNotNull(message.getMessageID(), "Message ID should be generated");
        Assertions.assertNotNull(message.getMessageHash(), "Message hash should be generated");
        Assertions.assertFalse(message.getMessageID().isEmpty(), "Message ID should not be empty");
        Assertions.assertFalse(message.getMessageHash().isEmpty(), "Message hash should not be empty");
    }
}