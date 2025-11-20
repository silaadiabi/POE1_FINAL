package com.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MessageManager {
    private int totalMessages = 0;
    private final Login login = new Login();
    private final List<Message> recentMessages = new ArrayList<>();

    public static void main(String[] args) {
        new MessageManager().runApp();
    }

    public void runApp() {
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
                "3) Quit\n\n" +
                "Enter your choice (1-3):"
            );

            if (choice == null) {
                running = false;
                continue;
            }

            switch (choice.trim()) {
                case "1" -> sendMessages();
                case "2" -> showRecentMessages();
                case "3" -> {
                    running = false;
                    JOptionPane.showMessageDialog(null, "Exiting QuickChat. Goodbye!");
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

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
            Message msg = new Message(totalMessages, phone, text);

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
                    recentMessages.add(msg);
                    if (recentMessages.size() > 10) {
                        recentMessages.remove(0); // Keep only last 10 messages
                    }
                }
                case "2" -> {
                    JOptionPane.showMessageDialog(null, "Message Disregarded.");
                    totalMessages--; // Decrement since we're not sending
                }
                case "3" -> {
                    storeMessage(msg);
                    JOptionPane.showMessageDialog(null, "Message Stored.");
                    recentMessages.add(msg);
                    if (recentMessages.size() > 10) {
                        recentMessages.remove(0);
                    }
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
        return phone.matches("^\\+27\\d{9}$");
    }

    private void storeMessage(Message msg) {
        try {
            java.io.FileWriter writer = new java.io.FileWriter("messages.txt", true);
            writer.write(msg.toString() + "\n---\n");
            writer.close();
        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message: " + e.getMessage());
        }
    }
}