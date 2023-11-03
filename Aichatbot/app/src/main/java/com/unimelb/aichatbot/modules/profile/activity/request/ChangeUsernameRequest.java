package com.unimelb.aichatbot.modules.profile.activity.request;

public class ChangeUsernameRequest {
    private String user_id;
    private String new_username;

    public ChangeUsernameRequest(String userId, String newUsername) {
        this.user_id = userId;
        this.new_username = newUsername;
    }

    // Getters and Setters
    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

    public String getNewUsername() {
        return new_username;
    }

    public void setNewUsername(String newUsername) {
        this.new_username = newUsername;
    }
}
