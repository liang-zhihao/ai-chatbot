package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

public class DeleteChatHistoryRequest {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("chatbot_id")
    private String chatbotId;

    // Constructors, getters, and setters
}

