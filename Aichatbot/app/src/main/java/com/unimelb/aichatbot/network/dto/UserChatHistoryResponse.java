package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserChatHistoryResponse {
    @SerializedName("chat_history")
    private List<ChatHistoryItem> chatHistoryItem;

    public List<ChatHistoryItem> getChatHistory() {
        return chatHistoryItem;
    }

    public void setChatHistory(List<ChatHistoryItem> chatHistoryItem) {
        this.chatHistoryItem = chatHistoryItem;
    }

// Getters and setters for chatHistory and status
}
