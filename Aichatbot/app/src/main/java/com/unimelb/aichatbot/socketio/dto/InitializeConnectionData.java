package com.unimelb.aichatbot.socketio.dto;

import com.google.gson.annotations.SerializedName;

public class InitializeConnectionData {
    @SerializedName("user_id")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}