package com.unimelb.aichatbot.modules.chatHistory.requestObject;

import com.google.gson.annotations.SerializedName;

public class GetRecentChatRequest {
    @SerializedName("user_id")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GetRecentChatRequest(String userId) {
        this.userId = userId;
    }
}
