package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserChatHistoryResponse {
    @SerializedName("chat_history")
    private List<ChatHistoryItemDto> chatHistoryItem;

    public List<ChatHistoryItemDto> getChatHistory() {
        return chatHistoryItem;
    }

    public void setChatHistory(List<ChatHistoryItemDto> chatHistoryItem) {
        this.chatHistoryItem = chatHistoryItem;
    }

// Getters and setters for chatHistory and status
}
