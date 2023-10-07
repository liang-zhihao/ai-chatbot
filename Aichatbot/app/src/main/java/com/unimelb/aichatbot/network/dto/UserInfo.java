package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

class UserInfo {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("username")
    private String username;

    // Constructors, getters, and setters for UserInfo

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
