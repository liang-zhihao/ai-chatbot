package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

public class ChatHistoryItem {
    @SerializedName("content")
    private String content;

    @SerializedName("role")
    private String role;
    // 2023-10-07 18:11:10
    @SerializedName("time")
    private String time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    // Getters and setters for content, role, and time
}

