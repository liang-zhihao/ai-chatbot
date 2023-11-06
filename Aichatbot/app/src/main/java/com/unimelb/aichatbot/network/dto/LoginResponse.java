package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;


public class LoginResponse {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("username")
    private String username;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

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

