package com.unimelb.aichatbot.modules.chatHistory;

public class RecommendUserResponse {
    private RecommendUserData data;
    private String message;
    private int status;
    private boolean success;
    private String timestamp;

    // Getters and setters
    public RecommendUserData getData() {
        return data;
    }

    public void setData(RecommendUserData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
