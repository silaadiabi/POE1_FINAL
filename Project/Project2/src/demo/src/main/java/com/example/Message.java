package com.example;

import java.util.Random;

public class Message {
    private final String messageID;
    private final int messageNumber;
    private final String recipientPhoneNumber;
    private final String content;
    private final String messageHash;
    private boolean sent = false;

    public Message(int messageNumber, String content, String recipientPhoneNumber) {
        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.content = content;
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        long num = 1000000000L + (long) (rand.nextDouble() * 8999999999L);
        return String.valueOf(num);
    }

    private String createMessageHash() {
        String[] words = content.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        String prefix = messageID.substring(0, 2);
        return (prefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public boolean checkRecipientCell() {
        return recipientPhoneNumber.matches("^\\+\\d{10,13}$");
    }

    public String sendOrStoreMessage(String action) {
        if ("send".equalsIgnoreCase(action)) {
            sent = true;
            return "Message sent successfully.";
        } else if ("store".equalsIgnoreCase(action)) {
            sent = false;
            return "Message stored (not sent).";
        } else {
            return "Invalid action.";
        }
    }

    public boolean isSent() {
        return sent;
    }

    public String getRecipientCell() {
        return recipientPhoneNumber;
    }

    public String printMessage() {
        return toString();
    }

    public String toJson() {
        return "  {\n" +
                "    \"MessageID\": \"" + messageID + "\",\n" +
                "    \"MessageHash\": \"" + messageHash + "\",\n" +
                "    \"RecipientPhoneNumber\": \"" + recipientPhoneNumber + "\",\n" +
                "    \"Message\": \"" + content.replace("\"", "\\\"") + "\",\n" +
                "    \"Sent\": " + sent + "\n" +
                "  }";
    }

    @Override
    public String toString() {
        return "MessageID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient Phone: " + recipientPhoneNumber + "\n"
                + "Message: " + content + "\n"
                + "Sent: " + (sent ? "Yes" : "No");
    }
}
