package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

public class ChatWithBotResponse {
    @SerializedName("reply")
    private String reply;

    @SerializedName("time")
    private String time;

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

