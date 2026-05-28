/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.loginsystem;
//These are my import statements for the entire chat app
//This chatApp is made in phases.
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;





/**
 *
 * @author Student
 */
public class LoginSystem {
      // Scanner - reads what user types from keyboard
    static Scanner input = new Scanner(System.in);

      //Checks if username is valid
      //Rules: Must contain "_" and be 5 characters or less
     
    public static boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }
     // Checks if password is strong enough
     // Rules: 8+ chars, 1 uppercase, 1 number, 1 special character
     
    public static boolean checkPasswordComplexity(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.matches(regex, password);
    }
    
    //This Checks if phone number is valid South African format
    //Format: +27 followed by 9 digits (like +27123456789)
     
    public static boolean checkCellPhoneNumber(String number) {
        String regex = "^\\+27\\d{9}$";
        return Pattern.matches(regex, number);
    }
 
    
     //Validates username and password for registration
     
    public static String registerUser(String username, String password) {
        if (!checkUserName(username)) {
            return "Username is not correct. Must contain '_' and be 5 chars or less.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correct. Must have 8+ chars, 1 capital, 1 number, 1 special char.";
        }
        return "Username and password successfully captured. User registered!";
    }
 
      //Checks if login credentials match what was registered
     
    public static boolean loginUser(String username, String password, String storedUsername, String storedPassword) {
        return username.equals(storedUsername) && password.equals(storedPassword);
    }
 
      //Returns a friendly login message
     
    public static String returnLoginStatus(boolean status) {
        if (status) {
            return "Login successful! Welcome back!";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
 
      //Displays the QuickChat menu with 5 options
     
      //NEW OPTIONS ADDED:
        //3) Discard last message - deletes the most recent message
        //4) Save messages - explicitly saves all messages to file
     
      
    public static void displayQuickChatMenu(String username, String userCell) {
        System.out.println("\nWelcome to QuickChat.");
        boolean inChatMenu = true;
 
        // Keep showing menu until user quits
        while (inChatMenu) {
            System.out.println("\nQUICKCHAT MENU ");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Discard last message");
            System.out.println("4) Save messages");
            System.out.println("5) Quit");
            System.out.println("");
            System.out.print("Select an option (1-5): ");
 
            String choice = input.nextLine();
           
            switch (choice) {
                case "1":
                    // Send a new message
                    sendMessageMenu(username, userCell);
                    break;
                   
                case "2":
                    // Show all recently sent messages - COMING SOON (feature in development)
                    System.out.println("Coming Soon");
                    // DISABLED: System.out.println(ChatSystem.getMessagesForRecipient(userCell));
                    break;
                   
                case "3":
                    // NEW: Discard the last message sent
                    String discardResult = ChatSystem.discardLastMessage();
                    System.out.println(discardResult);
                    break;
                   
                case "4":
                    // NEW: Save all messages to file
                    String saveResult = ChatSystem.saveAllMessages();
                    System.out.println(saveResult);
                    break;
                   
                case "5":
                    // Quit the app
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    inChatMenu = false;
                    break;
                   
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
 
    
    // Menu for sending messages
     
      //Steps:
       // 1. Ask how many messages to send
      //  2. For each message:
        //   - Ask for recipient username
       // - Ask for recipient phone number
        //  - Validate phone number
        //   - Ask for message content (max 250 words)
        //   - Send message through ChatSystem
     
    private static void sendMessageMenu(String sender, String senderCell) {
        System.out.println("\n--- SEND MESSAGE ---");
       
        // ASK HOW MANY MESSAGES TO SEND
        System.out.print("How many messages do you want to send? ");
        int numMessages = 0;
        try {
            numMessages = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }
       
        if (numMessages <= 0) {
            System.out.println("Please enter a positive number.");
            return;
        }
       
        // Loop for sending multiple messages
        for (int i = 1; i <= numMessages; i++) {
            System.out.println("\n--- MESSAGE " + i + " ---");
           
            System.out.print("Enter recipient username: ");
            String recipientUsername = input.nextLine();
     
            System.out.print("Enter recipient cell phone (+27XXXXXXXXX): ");
            String recipientCell = input.nextLine();
     
            // Validate recipient phone before continuing
            if (!checkCellPhoneNumber(recipientCell)) {
                System.out.println("Error: Invalid cell phone format. Must be (+27) followed by 9 digits.");
                i--;  // Retry this message
                continue;
            }
     
            System.out.print("Enter your message (max 250 words): ");
            String messageContent = input.nextLine();
           
            // Count words and characters
            int wordCount = countWords(messageContent);
            int charCount = messageContent.length();
           
            // Check word count BEFORE sending (max 250 words)
            if (wordCount > 250) {
                System.out.println("Error: Message must not exceed 250 words.");
                System.out.println("Current message: " + charCount + " characters, " + wordCount + " words");
                System.out.println("Please reduce your number of words.");
                i--;  // Retry this message
                continue;
            }
     
            // Send message and display result
            String result = ChatSystem.sendMessage(sender, senderCell, recipientUsername, recipientCell, messageContent);
            System.out.println(result);
        }
    }
   
   
    private static int countWords(String message) {
        if (message == null || message.trim().isEmpty()) {
            return 0;
        }
        String[] words = message.trim().split("\\s+");
        return words.length;
    }
    // MAIN METHOD - Where app starts
   
    /**
     * Main method - Entry point of the entire application
     *
     * Process:
     *   1. Welcome popup
     *   2. Registration (username, password, phone)
     *   3. Login (verify credentials)
     *   4. QuickChat menu
     */
  
     
   public static void main(String[] args) {
   // Welcome popup which is not allowed for the POE assignment
        //JOptionPane.showMessageDialog(null, "Welcome to QuickChat");
       
        // Variables to store registration info
        String storedUsername = "";
        String storedPassword = "";
        String storedCellPhone = "";
        String cellPhone;
 
        // === REGISTRATION PHASE ===
        System.out.println(" CHAT APP REGISTRATION \n");
 
        // Get and validate username
        String username;
        while (true) {
            System.out.print("Enter Username (must contain '_' and max 5 chars): ");
            username = input.nextLine();
            if (checkUserName(username)) break;
            System.out.println("Invalid username. Example: user_");
        }
 
        // Get and validate password
        String password;
        while (true) {
            System.out.print("Enter Password (8+ chars, 1 capital, 1 number, 1 special char): ");
            password = input.nextLine();
            if (checkPasswordComplexity(password)) break;
            System.out.println("Invalid password. Example: Password1!");
        }
 
        // Register user
        String registerMessage = LoginSystem.registerUser(username, password);
        System.out.println(registerMessage);
        storedUsername = username;
        storedPassword = password;
 
        // Get and validate phone
        while (true) {
            System.out.print("Enter Cell Phone (+27 followed by 9 digits): ");
            cellPhone = input.nextLine();
            if (checkCellPhoneNumber(cellPhone)) {
                System.out.println("Cell phone number successfully added.");
                storedCellPhone = cellPhone;
                break;
            }
            System.out.println("Invalid number. Must start with +27 and have 9 digits. Example: +27123456789");
        }
 
        // LOGIN PHASE 
        System.out.println("\nCHAT APP LOGIN \n");
        String loginUser;
        String loginPass;
        boolean status;
 
        // Keep asking until login is correct
        while (true) {
            System.out.print("Enter Username: ");
            loginUser = input.nextLine();
            System.out.print("Enter Password: ");
            loginPass = input.nextLine();
            status = LoginSystem.loginUser(loginUser, loginPass, storedUsername, storedPassword);
            System.out.println(LoginSystem.returnLoginStatus(status));
            if (status) break;
        }
 
        // Show menu if login successful
        if (status) {
            displayQuickChatMenu(loginUser, storedCellPhone);
        }
    }
}
 

class Message {
   
    // Message properties
    private String messageID;              // Unique ID like "MSG_123456"
    private String sender;                 // Who sent it
    private String recipientUsername;      // Who receives it
    private String content;                // The message text
    private String recipientCell;          // Recipient's phone
    private String senderCell;             // Sender's phone
    private String timestamp;              // When it was sent
    private static int totalMessages = 0;  // Total messages counter
 
   
    public Message(String sender, String recipientUsername, String content, String recipientCell, String senderCell) {
        this.messageID = generateMessageID();
        this.sender = sender;
        this.recipientUsername = recipientUsername;
        this.content = content;
        this.recipientCell = recipientCell;
        this.senderCell = senderCell;
        this.timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }
 
    
      //Validates message ID format
      //Checks if ID is valid: not null, not empty, and max 20 characters
     
    public boolean checkMessageID() {
        return messageID != null && !messageID.isEmpty() && messageID.length() <= 20;
    }
 
     //Validates recipient cell phone format
     
    public boolean checkRecipientCell() {
        String regex = "^\\+27\\d{9}$";
        return recipientCell.matches(regex);
    }
 
    
    // Creates a security hash of the message
     //Format: First 2 digits of message ID, dot, message number, colon, first word, colon, last word
    // Example: 00.0:HI:THANKS (all uppercase)
     
    public String createMessageHash() {
        try {
            // Extract first 2 digits from message ID (e.g., "MSG_000123" -> "00")
            String firstTwoDigits = "";
            for (int i = 0; i < messageID.length() && firstTwoDigits.length() < 2; i++) {
                if (Character.isDigit(messageID.charAt(i))) {
                    firstTwoDigits += messageID.charAt(i);
                }
            }
           
            // Get message number (total messages)
            int messageNumber = Message.returnTotalMessages();
           
            // Extract first and last words from message
            String[] words = content.trim().split("\\s+");
            String firstWord = words.length > 0 ? words[0] : "UNKNOWN";
            String lastWord = words.length > 0 ? words[words.length - 1] : "UNKNOWN";
           
            // Build hash in format: XX.0:FIRSTWORD:LASTWORD (uppercase)
            String hash = firstTwoDigits + "." + messageNumber + ":" + firstWord.toUpperCase() + ":" + lastWord.toUpperCase();
           
            return hash;
        } catch (Exception e) {
            return "HASH_ERROR";
        }
    }
 
    /**
     * Formats message as readable string
     */
    public String printMessages() {
        return "\n--- Message Details ---\n" +
               "Message ID: " + messageID + "\n" +
               "From: " + sender + " (" + senderCell + ")\n" +
               "To: " + recipientUsername + " (" + recipientCell + ")\n" +
               "Message: " + content + "\n" +
               "Hash: " + createMessageHash() + "\n" +
               "Time: " + timestamp;
    }
 // Returns total messages sent
    public static int returnTotalMessages() {
        return totalMessages;
    }
 
    
     //Increases total message counter
     public static void incrementTotalMessages() {
        totalMessages++;
    }
 
    
     //Saves message to JSON file
     // Messages persist even after app closes
     public void storeMessage() {
        try (java.io.FileWriter file = new java.io.FileWriter("messages.json", true)) {
            String json = String.format(
                "{\"messageID\":\"%s\",\"sender\":\"%s\",\"recipient\":\"%s\",\"content\":\"%s\",\"recipientCell\":\"%s\",\"timestamp\":\"%s\"}\n",
                messageID, sender, recipientUsername, content.replace("\"", "\\\""), recipientCell, timestamp
            );
            file.write(json);
        } catch (Exception e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }
     //Generates unique random message ID
     private String generateMessageID() {
        return "MSG_" + String.format("%06d", new Random().nextInt(1000000));
    }
 
    // GETTER METHODS - Read message data 
    public String getMessageID() { return messageID; }
    public String getContent() { return content; }
    public String getRecipientCell() { return recipientCell; }
    public String getSenderCell() { return senderCell; }
    public String getSender() { return sender; }
    public String getRecipientUsername() { return recipientUsername; }
    public String getTimestamp() { return timestamp; }
}
 

// CHATSYSTEM CLASS - Manages all messaging operations


 //CHATSYSTEM CLASS

 //This class handles:
// - Sending messages with validation
   //- Getting messages for a user
   //- Discarding last message
  //- Saving all messages explicitly
  // - Counting messages
 
class ChatSystem {
   
    // ArrayList to store all messages in memory during this session
    private static ArrayList<Message> messages = new ArrayList<>();
 
    /**
     * Sends a message after validation
     *
     * Checks:
     *   1. Message is not empty
     *   2. Message is not too long (max 250 words)
     *   3. Message ID is valid (checkMessageID)
     *   4. Recipient phone is valid format
     *
     * If all pass: saves message and returns success
     * If any fail: returns error message
     */
    public static String sendMessage(String sender, String senderCell, String recipientUsername, String recipientCell, String content) {
       
        // Check 1: Is message empty?
        if (content == null || content.isEmpty()) {
            return "Error: Message cannot be empty.";
        }
       
        // Check 2: Is message too long? (max 250 words)
        int wordCount = countWords(content);
        if (wordCount > 250) {
            return "Error: Message must not exceed 250 words. Current: " + content.length() + " characters, " + wordCount + " words. Please reduce your number of words.";
        }
 
        // Create message object
        Message msg = new Message(sender, recipientUsername, content, recipientCell, senderCell);
 
        // Check 3: Is message ID valid? (checkMessageID)
        if (!msg.checkMessageID()) {
            return "Error: Invalid message ID.";
        }
 
        // Check 4: Is recipient phone valid?
        if (!msg.checkRecipientCell()) {
            return "Error: Invalid recipient cell phone number format. Must be (+27) followed by 9 digits";
        }
 
        // All checks passed! Send the message
        messages.add(msg);
        Message.incrementTotalMessages();
        msg.storeMessage();  // Save to file
 
        System.out.println(msg.printMessages());
        return "Message successfully sent";
    }
   
    /**
     * Counts the number of words in a message
     * Words are separated by whitespace
     */
    private static int countWords(String message) {
        if (message == null || message.trim().isEmpty()) {
            return 0;
        }
        String[] words = message.trim().split("\\s+");
        return words.length;
    }
 
    /**
     * Returns count of messages for a phone number
     */
    public static int returnPhoneMessages(String cell) {
        int count = 0;
        for (Message m : messages) {
            if (m.getRecipientCell().equals(cell) || m.getSenderCell().equals(cell)) {
                count++;
            }
        }
        return count;
    }
 
    /**
     * Gets all messages for a recipient (sender or receiver)
     * Shows message details in formatted way
     */
    public static String getMessagesForRecipient(String cell) {
        StringBuilder sb = new StringBuilder();
        boolean found = false;
       
        for (Message m : messages) {
            if (m.getRecipientCell().equals(cell) || m.getSenderCell().equals(cell)) {
                sb.append(m.printMessages()).append("\n");
                found = true;
            }
        }
       
        if (!found) {
            return "No messages yet.";
        }
       
        return "\n YOUR MESSAGES \n" + sb.toString();
    }
 
    /**
     * Discard the last message
     *
     * This removes the most recent message from the list.
     * The message is removed from memory but the file is NOT updated
     * (we still keep the file for persistence).
     *
     * @return Success or error message
     */
    public static String discardLastMessage() {
        // Check if there are any messages to discard
        if (messages.isEmpty()) {
            return "Error: No messages to discard.";
        }
       
        // Get the last message (index = size - 1)
        Message lastMessage = messages.get(messages.size() - 1);
       
        // Show which message is being discarded
        System.out.println("\n--- DISCARDING MESSAGE ---");
        System.out.println("Message ID: " + lastMessage.getMessageID());
        System.out.println("From: " + lastMessage.getSender());
        System.out.println("Content: " + lastMessage.getContent());
        System.out.println("");
       
        // Remove it from the list
        messages.remove(messages.size() - 1);
       
        return "Message successfully discarded";
    }
 
    /**
     * Explicitly save all messages to file
     *
     * This re-saves all messages currently in memory to the JSON file.
     * Useful if you want to make sure everything is saved.
     *
     * It OVERWRITES the file with only current messages in memory.
     * (So discarded messages won't be in the file)
     *
     * @return Success or error message
     */
    public static String saveAllMessages() {
        try {
            // Create a new file (overwrite existing one)
            java.io.FileWriter file = new java.io.FileWriter("messages.json", false);
           
            // Write all messages from memory
            for (Message msg : messages) {
                String json = String.format(
                    "{\"messageID\":\"%s\",\"sender\":\"%s\",\"recipient\":\"%s\",\"content\":\"%s\",\"recipientCell\":\"%s\",\"timestamp\":\"%s\"}\n",
                    msg.getMessageID(),
                    msg.getSender(),
                    msg.getRecipientUsername(),
                    msg.getContent().replace("\"", "\\\""),
                    msg.getRecipientCell(),
                    msg.getTimestamp()
                );
                file.write(json);
            }
           
            // Close file to save changes
            file.close();
           
            return "Message successfully stored";
           
        } catch (Exception e) {
            return "Error saving messages: " + e.getMessage();
        }
    }
 
    public static int returnTotalMessages() {
        return Message.returnTotalMessages();
    }
}     
