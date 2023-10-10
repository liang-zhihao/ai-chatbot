package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

public class DeleteChatHistoryResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    // Getters and setters for message and status
}

