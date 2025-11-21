package com.example;

/**
 * Represents a message with unique identifier, hash, and metadata
 */
public class Message {
    private final String messageID;
    private final int messageNumber;
    private final String recipientPhoneNumber;
    private final String content;
    private final String messageHash;
    private final String flag;

    public Message(int messageNumber, String recipientPhoneNumber, String content, String flag) {
        this.messageNumber = messageNumber;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.content = content;
        this.flag = flag;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        StringBuilder idBuilder = new StringBuilder();
        
        String paddedNumber = String.format("%03d", messageNumber);
        idBuilder.append(paddedNumber);
        
        if (recipientPhoneNumber != null && !recipientPhoneNumber.isEmpty()) {
            int phoneCharsToTake = Math.min(5, recipientPhoneNumber.length());
            for (int i = 0; i < phoneCharsToTake; i++) {
                char c = recipientPhoneNumber.charAt(i);
                if (Character.isDigit(c)) {
                    idBuilder.append(c);
                }
            }
        }
        
        if (content != null && !content.isEmpty()) {
            int contentCharsToTake = Math.min(4, content.length());
            int charsAdded = 0;
            for (int i = 0; i < content.length() && charsAdded < contentCharsToTake; i++) {
                char c = content.charAt(i);
                if (Character.isLetterOrDigit(c)) {
                    idBuilder.append(Character.toUpperCase(c));
                    charsAdded++;
                }
            }
            
            while (charsAdded < contentCharsToTake) {
                idBuilder.append(charsAdded);
                charsAdded++;
            }
        }
        
        return idBuilder.toString();
    }

    private String createMessageHash() {
        String[] words = content != null ? content.trim().split("\\s+") : new String[0];
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        String prefix = messageID.length() >= 2 ? messageID.substring(0, 2) : "00";
        
        return (prefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipientPhoneNumber() { return recipientPhoneNumber; }
    public String getMessageText() { return content; }
    public String getMessageHash() { return messageHash; }
    public String getFlag() { return flag; }

    @Override
    public String toString() {
        return "MessageID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient Phone: " + recipientPhoneNumber + "\n"
                + "Message: " + content + "\n"
                + "Flag: " + flag + "\n";
    }
}