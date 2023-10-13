package com.unimelb.aichatbot.socketio.dto;

public class PrivateMessageData {
    private String toUser;  // Used when sending
    private String fromUser;  // Used when receiving
    private String message;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
// Getters and setters
}
