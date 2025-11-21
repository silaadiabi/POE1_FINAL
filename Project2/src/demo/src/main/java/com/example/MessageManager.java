package com.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MessageManager {
    private int totalMessages = 0;
    private final Login login = new Login();
    
    // Parallel arrays as required
    private final List<String> sentMessages = new ArrayList<>();
    private final List<String> disregardedMessages = new ArrayList<>();
    private final List<String> storedMessages = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIDs = new ArrayList<>();
    
    // Combined message objects for easier management
    private final List<Message> allMessages = new ArrayList<>();

    public static void main(String[] args) {
        new MessageManager().runApp();
    }

    public void runApp() {
        // Pre-populate with test data
        populateTestData();
        
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat!");

        // === REGISTRATION/LOGIN ===
        int authChoice = JOptionPane.showOptionDialog(null,
            "Do you want to:",
            "QuickChat Authentication",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"Register", "Login", "Exit"},
            "Login");

        if (authChoice == 2 || authChoice == -1) {
            JOptionPane.showMessageDialog(null, "Exiting QuickChat. Goodbye!");
            return;
        }

        if (authChoice == 0) { // Register
            String username = JOptionPane.showInputDialog("Enter your username:");
            String password = JOptionPane.showInputDialog("Enter your password:");
            String phone = JOptionPane.showInputDialog("Enter your phone number (+27 format):");

            String registrationResult = login.registerUser(username, password, phone);
            JOptionPane.showMessageDialog(null, registrationResult);

            if (!registrationResult.equals("User registered successfully.")) {
                return; // Registration failed
            }
        }

        // === LOGIN ===
        boolean loggedIn = false;
        int loginAttempts = 0;
        
        while (!loggedIn && loginAttempts < 3) {
            String username = JOptionPane.showInputDialog("Enter your username:");
            if (username == null) return;
            
            String password = JOptionPane.showInputDialog("Enter your password:");
            if (password == null) return;

            if (login.loginUser(username, password)) {
                JOptionPane.showMessageDialog(null, login.returnLoginStatus(username, password));
                loggedIn = true;
            } else {
                loginAttempts++;
                if (loginAttempts < 3) {
                    JOptionPane.showMessageDialog(null, 
                        "Login failed. Please try again. Attempts remaining: " + (3 - loginAttempts));
                } else {
                    JOptionPane.showMessageDialog(null, "Too many failed attempts. Exiting.");
                    return;
                }
            }
        }

        boolean running = true;

        while (running) {
            String choice = JOptionPane.showInputDialog(
                "Please choose an option:\n" +
                "1) Send Messages\n" +
                "2) Show Recently Sent Messages\n" +
                "3) Display Arrays Menu\n" +
                "4) Quit\n\n" +
                "Enter your choice (1-4):"
            );

            if (choice == null) {
                running = false;
                continue;
            }

            switch (choice.trim()) {
                case "1" -> sendMessages();
                case "2" -> showRecentMessages();
                case "3" -> showArraysMenu();
                case "4" -> {
                    running = false;
                    JOptionPane.showMessageDialog(null, "Exiting QuickChat. Goodbye!");
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid choice. Please enter 1, 2, 3, or 4.");
            }
        }
    }

    private void populateTestData() {
        // Test Data Message 1
        Message msg1 = new Message(1, "+27834557896", "Did you get the cake?", "Sent");
        allMessages.add(msg1);
        sentMessages.add(msg1.getMessageText());
        messageHashes.add(msg1.getMessageHash());
        messageIDs.add(msg1.getMessageID());

        // Test Data Message 2
        Message msg2 = new Message(2, "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
        allMessages.add(msg2);
        storedMessages.add(msg2.getMessageText());
        messageHashes.add(msg2.getMessageHash());
        messageIDs.add(msg2.getMessageID());

        // Test Data Message 3
        Message msg3 = new Message(3, "+27834484567", "Yohoooo, I am at your gate.", "Disregard");
        allMessages.add(msg3);
        disregardedMessages.add(msg3.getMessageText());
        messageHashes.add(msg3.getMessageHash());
        messageIDs.add(msg3.getMessageID());

        // Test Data Message 4
        Message msg4 = new Message(4, "0838884567", "It is dinner time!", "Sent");
        allMessages.add(msg4);
        sentMessages.add(msg4.getMessageText());
        messageHashes.add(msg4.getMessageHash());
        messageIDs.add(msg4.getMessageID());

        // Test Data Message 5
        Message msg5 = new Message(5, "+27838884567", "Ok, I am leaving without you.", "Stored");
        allMessages.add(msg5);
        storedMessages.add(msg5.getMessageText());
        messageHashes.add(msg5.getMessageHash());
        messageIDs.add(msg5.getMessageID());

        totalMessages = 5;
        
        // Store messages in JSON file for reading later
        storeMessagesToJSON();
    }

    private void showArraysMenu() {
        boolean inMenu = true;
        
        while (inMenu) {
            String choice = JOptionPane.showInputDialog(
                "Arrays Menu:\n" +
                "1) Display sender and recipient of all sent messages\n" +
                "2) Display the longest sent message\n" +
                "3) Search for message by ID\n" +
                "4) Search messages by recipient\n" +
                "5) Delete message by hash\n" +
                "6) Display message report\n" +
                "7) Read JSON file into stored messages array\n" +
                "8) Back to main menu\n\n" +
                "Enter your choice (1-8):"
            );

            if (choice == null) {
                inMenu = false;
                continue;
            }

            switch (choice.trim()) {
                case "1" -> displaySentMessagesSendersRecipients();
                case "2" -> displayLongestMessage();
                case "3" -> searchMessageByID();
                case "4" -> searchMessagesByRecipient();
                case "5" -> deleteMessageByHash();
                case "6" -> displayMessageReport();
                case "7" -> readJSONFileToArray();
                case "8" -> inMenu = false;
                default -> JOptionPane.showMessageDialog(null, "Invalid choice. Please enter 1-8.");
            }
        }
    }

    private void displaySentMessagesSendersRecipients() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Sent Messages - Senders and Recipients ===\n\n");
        
        for (Message msg : allMessages) {
            if ("Sent".equals(msg.getFlag())) {
                sb.append("Recipient: ").append(msg.getRecipientPhoneNumber())
                  .append(" | Message: ").append(msg.getMessageText())
                  .append("\n");
            }
        }
        
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void displayLongestMessage() {
        Message longestMessage = null;
        
        for (Message msg : allMessages) {
            if ("Sent".equals(msg.getFlag())) {
                if (longestMessage == null || msg.getMessageText().length() > longestMessage.getMessageText().length()) {
                    longestMessage = msg;
                }
            }
        }
        
        if (longestMessage != null) {
            JOptionPane.showMessageDialog(null, 
                "=== Longest Sent Message ===\n\n" +
                "Message: " + longestMessage.getMessageText() + "\n" +
                "Length: " + longestMessage.getMessageText().length() + " characters\n" +
                "Recipient: " + longestMessage.getRecipientPhoneNumber() + "\n" +
                "Message ID: " + longestMessage.getMessageID());
        } else {
            JOptionPane.showMessageDialog(null, "No sent messages found.");
        }
    }

    private void searchMessageByID() {
        String searchID = JOptionPane.showInputDialog("Enter Message ID to search:");
        if (searchID == null) return;
        
        Message foundMessage = null;
        for (Message msg : allMessages) {
            if (msg.getMessageID().equals(searchID)) {
                foundMessage = msg;
                break;
            }
        }
        
        if (foundMessage != null) {
            JOptionPane.showMessageDialog(null, 
                "=== Message Found ===\n\n" +
                "Message ID: " + foundMessage.getMessageID() + "\n" +
                "Recipient: " + foundMessage.getRecipientPhoneNumber() + "\n" +
                "Message: " + foundMessage.getMessageText() + "\n" +
                "Hash: " + foundMessage.getMessageHash());
        } else {
            JOptionPane.showMessageDialog(null, "No message found with ID: " + searchID);
        }
    }

    private void searchMessagesByRecipient() {
        String recipient = JOptionPane.showInputDialog("Enter recipient phone number to search:");
        if (recipient == null) return;
        
        List<Message> foundMessages = new ArrayList<>();
        for (Message msg : allMessages) {
            if (msg.getRecipientPhoneNumber().equals(recipient)) {
                foundMessages.add(msg);
            }
        }
        
        if (!foundMessages.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Messages for Recipient: ").append(recipient).append(" ===\n\n");
            
            for (Message msg : foundMessages) {
                sb.append("Message: ").append(msg.getMessageText())
                  .append(" | Status: ").append(msg.getFlag())
                  .append("\n");
            }
            
            JOptionPane.showMessageDialog(null, sb.toString());
        } else {
            JOptionPane.showMessageDialog(null, "No messages found for recipient: " + recipient);
        }
    }

    private void deleteMessageByHash() {
        String hashToDelete = JOptionPane.showInputDialog("Enter message hash to delete:");
        if (hashToDelete == null) return;
        
        Message messageToDelete = null;
        for (Message msg : allMessages) {
            if (msg.getMessageHash().equals(hashToDelete)) {
                messageToDelete = msg;
                break;
            }
        }
        
        if (messageToDelete != null) {
            allMessages.remove(messageToDelete);
            sentMessages.remove(messageToDelete.getMessageText());
            storedMessages.remove(messageToDelete.getMessageText());
            disregardedMessages.remove(messageToDelete.getMessageText());
            messageHashes.remove(messageToDelete.getMessageHash());
            messageIDs.remove(messageToDelete.getMessageID());
            
            JOptionPane.showMessageDialog(null, 
                "Message successfully deleted:\n" +
                "Message: " + messageToDelete.getMessageText() + "\n" +
                "Hash: " + messageToDelete.getMessageHash());
            
            // Update the JSON file after deletion
            storeMessagesToJSON();
        } else {
            JOptionPane.showMessageDialog(null, "No message found with hash: " + hashToDelete);
        }
    }

    private void displayMessageReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Message Report ===\n\n");
        
        for (Message msg : allMessages) {
            if ("Sent".equals(msg.getFlag())) {
                sb.append("Message Hash: ").append(msg.getMessageHash()).append("\n")
                  .append("Recipient: ").append(msg.getRecipientPhoneNumber()).append("\n")
                  .append("Message: ").append(msg.getMessageText()).append("\n")
                  .append("---\n");
            }
        }
        
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void readJSONFileToArray() {
        try {
            // ChatGPT assisted code for reading JSON file - using manual JSON parsing
            String content = new String(Files.readAllBytes(Paths.get("messages.json")));
            
            storedMessages.clear(); // Clear existing stored messages
            
            // Manual JSON parsing
            String[] messages = content.split("\\},\\s*\\{");
            for (String messageStr : messages) {
                // Extract message content
                if (messageStr.contains("\"message\":")) {
                    int start = messageStr.indexOf("\"message\":\"") + 10;
                    int end = messageStr.indexOf("\"", start);
                    if (start > 9 && end > start) {
                        String message = messageStr.substring(start, end);
                        storedMessages.add(message);
                    }
                }
            }
            
            JOptionPane.showMessageDialog(null, 
                "JSON file successfully read into stored messages array.\n" +
                "Total stored messages: " + storedMessages.size() + "\n\n" +
                "Note: This implementation uses manual JSON parsing as requested in the requirements.");
                
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading JSON file: " + e.getMessage());
        }
    }

    private void storeMessagesToJSON() {
        try {
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[\n");
            
            for (int i = 0; i < allMessages.size(); i++) {
                Message msg = allMessages.get(i);
                jsonBuilder.append("  {\n");
                jsonBuilder.append("    \"message\": \"").append(msg.getMessageText().replace("\"", "\\\"")).append("\",\n");
                jsonBuilder.append("    \"recipient\": \"").append(msg.getRecipientPhoneNumber()).append("\",\n");
                jsonBuilder.append("    \"hash\": \"").append(msg.getMessageHash()).append("\",\n");
                jsonBuilder.append("    \"id\": \"").append(msg.getMessageID()).append("\",\n");
                jsonBuilder.append("    \"flag\": \"").append(msg.getFlag()).append("\"\n");
                jsonBuilder.append("  }");
                
                if (i < allMessages.size() - 1) {
                    jsonBuilder.append(",");
                }
                jsonBuilder.append("\n");
            }
            
            jsonBuilder.append("]");
            
            Files.write(Paths.get("messages.json"), jsonBuilder.toString().getBytes());
        } catch (IOException e) {
            System.err.println("Error storing messages to JSON: " + e.getMessage());
        }
    }

    // Getters for unit testing
    public List<String> getSentMessages() { return sentMessages; }
    public List<String> getDisregardedMessages() { return disregardedMessages; }
    public List<String> getStoredMessages() { return storedMessages; }
    public List<String> getMessageHashes() { return messageHashes; }
    public List<String> getMessageIDs() { return messageIDs; }
    public List<Message> getAllMessages() { return allMessages; }
    public Login getLogin() { return login; }

    private void sendMessages() {
        String input = JOptionPane.showInputDialog("How many messages would you like to send?");
        if (input == null) return;

        int count;
        try {
            count = Integer.parseInt(input);
            if (count <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number entered.");
            return;
        }

        for (int i = 0; i < count; i++) {
            String phone = JOptionPane.showInputDialog("Enter recipient phone number (+27 format):");
            if (phone == null) return;

            String text = JOptionPane.showInputDialog("Enter your message:");
            if (text == null) return;

            // Validation
            if (text.length() > 250) {
                JOptionPane.showMessageDialog(null, "Message too long. Max 250 characters.");
                i--; // Repeat this iteration
                continue;
            }

            if (!checkCellPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(null, "Invalid phone number. Must be in +27 format with 9 following digits.");
                i--; // Repeat this iteration
                continue;
            }

            totalMessages++;
            Message msg = new Message(totalMessages, phone, text, text);

            String optionInput = JOptionPane.showInputDialog(
                msg.toString() + 
                "\n\nChoose an option:\n" +
                "1) Send\n" +
                "2) Disregard\n" +
                "3) Store\n\n" +
                "Enter your choice (1-3):"
            );

            if (optionInput == null) {
                totalMessages--; // User cancelled
                i--;
                continue;
            }

            switch (optionInput.trim()) {
                case "1" -> {
                    JOptionPane.showMessageDialog(null, "Message Sent:\n\n" + msg.toString());
                    allMessages.add(msg);
                    sentMessages.add(msg.getMessageText());
                    messageHashes.add(msg.getMessageHash());
                    messageIDs.add(msg.getMessageID());
                    storeMessagesToJSON(); // Update JSON file
                }
                case "2" -> {
                    JOptionPane.showMessageDialog(null, "Message Disregarded.");
                    allMessages.add(msg);
                    disregardedMessages.add(msg.getMessageText());
                    messageHashes.add(msg.getMessageHash());
                    messageIDs.add(msg.getMessageID());
                    storeMessagesToJSON(); // Update JSON file
                }
                case "3" -> {
                    storeMessage(msg);
                    JOptionPane.showMessageDialog(null, "Message Stored.");
                    allMessages.add(msg);
                    storedMessages.add(msg.getMessageText());
                    messageHashes.add(msg.getMessageHash());
                    messageIDs.add(msg.getMessageID());
                    storeMessagesToJSON(); // Update JSON file
                }
                default -> {
                    JOptionPane.showMessageDialog(null, "Invalid choice. Message disregarded.");
                    totalMessages--; // Decrement since we're not sending
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Total Messages Processed: " + totalMessages);
    }

    private void showRecentMessages() {
        List<Message> recentMessages = allMessages.stream()
            .filter(msg -> "Sent".equals(msg.getFlag()))
            .limit(10)
            .toList();

        if (recentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No recent messages to display.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Recent Messages ===\n\n");
        for (int i = 0; i < recentMessages.size(); i++) {
            sb.append("Message ").append(i + 1).append(":\n");
            sb.append(recentMessages.get(i).toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private boolean checkCellPhoneNumber(String phone) {
        if (phone == null) return false;
        return phone.matches("^\\+27\\d{9}$") || phone.matches("^0\\d{9}$");
    }

    private void storeMessage(Message msg) {
        try {
            java.io.FileWriter writer = new java.io.FileWriter("stored_messages.txt", true);
            writer.write(msg.toString() + "\n---\n");
            writer.close();
        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message: " + e.getMessage());
        }
    }
}