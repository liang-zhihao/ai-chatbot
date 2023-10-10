package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

public class ChatWithBotRequest {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("chatbot_id")
    private String chatbotId;

    @SerializedName("message")
    private String message;

    // Constructors, getters, and setters

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}