package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

public class CreateChatbotResponse {
    @SerializedName("chatbot_id")
    private String chatbotId;

    @SerializedName("status")
    private String status;

    // Constructors, getters, and setters
}