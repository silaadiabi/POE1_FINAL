package com.example;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

public class MessageManager {
    private static Login login = new Login();
    private static boolean loggedIn = false;

    private static Message[] messages;
    private static int messageCount = 0;
    private static int maxMessages = 0;

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "=== QUICKCHAT REGISTRATION ===");

        String username = JOptionPane.showInputDialog("Enter username:");
        String password = JOptionPane.showInputDialog("Enter password:");
        String phone = JOptionPane.showInputDialog("Enter phone number:");

        if (username == null || password == null || phone == null) {
            JOptionPane.showMessageDialog(null, "Registration cancelled. Exiting.");
            return;
        }

        String registrationResult = login.registerUser(username, password, phone);
        JOptionPane.showMessageDialog(null, registrationResult);

        if (!registrationResult.equals("User registered successfully.")) {
            JOptionPane.showMessageDialog(null, "Registration failed. Restart and try again.");
            return;
        }

        JOptionPane.showMessageDialog(null, "=== LOGIN ===");
        String loginUser = JOptionPane.showInputDialog("Enter username:");
        String loginPass = JOptionPane.showInputDialog("Enter password:");

        if (loginUser == null || loginPass == null) {
            JOptionPane.showMessageDialog(null, "Login cancelled. Exiting.");
            return;
        }

        loggedIn = login.loginUser(loginUser, loginPass);
        if (!loggedIn) {
            JOptionPane.showMessageDialog(null, "Login failed. Exiting program.");
            return;
        }

        JOptionPane.showMessageDialog(null, login.returnLoginStatus(loginUser, loginPass));

        String messageLimitInput = JOptionPane.showInputDialog("How many messages would you like to send?");
        if (messageLimitInput == null) {
            JOptionPane.showMessageDialog(null, "Operation cancelled. Exiting.");
            return;
        }

        try {
            maxMessages = Integer.parseInt(messageLimitInput);
            if (maxMessages <= 0) {
                JOptionPane.showMessageDialog(null, "Number must be greater than 0. Exiting.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Exiting.");
            return;
        }

        messages = new Message[maxMessages];

        boolean running = true;
        while (running) {
            String[] options = {"Send Messages", "Show Recently Sent Messages", "Quit"};
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "=== QUICKCHAT MENU ===",
                    "QuickChat Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            switch (choice) {
                case 0 -> handleSendMessages();
                case 1 -> showRecentMessages();
                case 2 -> {
                    running = false;
                    JOptionPane.showMessageDialog(null, "Exiting QuickChat. Goodbye!");
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid option. Try again.");
            }
        }
    }

    private static void handleSendMessages() {
        int entered = 0;
        int messageIndex = messageCount + 1;

        while (entered < maxMessages - messageCount) {
            JOptionPane.showMessageDialog(null, "=== Create Message " + (messageCount + 1) + " of " + maxMessages + " ===");

            String recipient = JOptionPane.showInputDialog("Enter recipient phone (+CountryCode):");
            String content = JOptionPane.showInputDialog("Enter message (max 250 chars):");

            if (recipient == null || content == null) return;

            Message msg = new Message(messageIndex, content, recipient);

            if (!msg.checkRecipientCell()) {
                JOptionPane.showMessageDialog(null, "Invalid phone number format. Use +CountryCode followed by number.");
                continue;
            }

            if (content.length() > 250) {
                JOptionPane.showMessageDialog(null, "Message exceeds 250 characters. Please shorten it.");
                continue;
            }

            JOptionPane.showMessageDialog(null, "=== Message Preview ===\n" + msg.printMessage());

            String[] actions = {"Send", "Store", "Disregard", "StoreJSON"};
            int actionChoice = JOptionPane.showOptionDialog(
                    null,
                    "Choose an action for this message:",
                    "Message Action",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    actions,
                    actions[0]
            );

            boolean accepted = false;

            switch (actionChoice) {
                case 0 -> {
                    JOptionPane.showMessageDialog(null, msg.sendOrStoreMessage("send"));
                    storeMessage(msg);
                    accepted = true;
                }
                case 1 -> {
                    JOptionPane.showMessageDialog(null, msg.sendOrStoreMessage("store"));
                    storeMessage(msg);
                    accepted = true;
                }
                case 2 -> JOptionPane.showMessageDialog(null, "Message disregarded.");
                case 3 -> {
                    JOptionPane.showMessageDialog(null, msg.sendOrStoreMessage("store"));
                    storeMessage(msg);
                    writeMessagesToJsonFile();
                    accepted = true;
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid action. Try again.");
            }

            if (accepted) {
                entered++;
                messageIndex++;
            }
        }

        long sentCount = countSentMessages();
        JOptionPane.showMessageDialog(null, "Total messages sent: " + sentCount);
    }

    private static void storeMessage(Message msg) {
        if (messageCount < maxMessages) {
            messages[messageCount] = msg;
            messageCount++;
        } else {
            JOptionPane.showMessageDialog(null, "Message limit reached.");
        }
    }

    private static void showRecentMessages() {
        if (messageCount == 0) {
            JOptionPane.showMessageDialog(null, "No messages available.");
            return;
        }

        StringBuilder sb = new StringBuilder("=== Recently Sent Messages ===\n");
        for (int i = 0; i < messageCount; i++) {
            Message msg = messages[i];
            if (msg.isSent()) {
                sb.append("To: ").append(msg.getRecipientCell()).append("\n")
                        .append(msg.printMessage()).append("\n---------------------------\n");
            }
        }

        long sent = countSentMessages();
        long stored = messageCount - sent;
        sb.append("\nSummary: ").append(sent).append(" sent, ").append(stored).append(" stored");

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static long countSentMessages() {
        long count = 0;
        for (int i = 0; i < messageCount; i++) {
            if (messages[i].isSent()) count++;
        }
        return count;
    }

    private static void writeMessagesToJsonFile() {
        try (FileWriter fw = new FileWriter("messages.json")) {
            fw.write("[\n");
            for (int i = 0; i < messageCount; i++) {
                fw.write(messages[i].toJson());
                if (i < messageCount - 1) fw.write(",\n");
            }
            fw.write("\n]");
            JOptionPane.showMessageDialog(null, "Messages written to messages.json");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to messages.json: " + e.getMessage());
        }
    }
}
