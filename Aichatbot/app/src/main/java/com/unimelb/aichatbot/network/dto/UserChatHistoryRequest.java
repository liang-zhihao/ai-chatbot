package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

public class UserChatHistoryRequest {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("chatbot_id")
    private String chatbotId;

    public UserChatHistoryRequest(String userId, String chatbotId) {
        this.userId = userId;
        this.chatbotId = chatbotId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChatbotId() {
        return chatbotId;
    }

    public void setChatbotId(String chatbotId) {
        this.chatbotId = chatbotId;
    }

}

