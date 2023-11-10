package com.unimelb.aichatbot.modules.chatHistory;

public class RecommendUserRequest {
    private String user_id;

    public RecommendUserRequest(String userId) {
        this.user_id = userId;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }
}
