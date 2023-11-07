package com.unimelb.aichatbot.modules.newChat.model;

import com.google.gson.annotations.SerializedName;

public class FriendListRequest {
    @SerializedName("user_id")
    private String userId;

    public FriendListRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
