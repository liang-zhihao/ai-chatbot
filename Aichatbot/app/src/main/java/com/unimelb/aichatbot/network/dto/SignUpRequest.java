package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

public class SignUpRequest {
    @SerializedName("user_id")
    String userId;

    @SerializedName("password")
    String password;

    @SerializedName("username")
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
