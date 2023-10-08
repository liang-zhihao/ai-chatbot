package com.unimelb.aichatbot.modules.responsObject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserChatHistory {

    @SerializedName("data")
    private Data data;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private int status;
    @SerializedName("success")
    private boolean success;

    // Getters and setters...

    public static class Data {
        @SerializedName("chat_history")
        private List<ChatMessage> chatHistory;

        // Getters and setters...
    }

    public static class ChatMessage {
        @SerializedName("content")
        private String content;
        @SerializedName("role")
        private String role;
        @SerializedName("time")
        private String time;

        @Override
        public String toString() {
            return "ChatMessage{" +
                    "content='" + content + '\'' +
                    ", role='" + role + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserChatHistory{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", success=" + success +
                '}';
    }
}
