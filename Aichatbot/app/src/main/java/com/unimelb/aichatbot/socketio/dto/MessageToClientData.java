package com.unimelb.aichatbot.socketio.dto;

import com.google.gson.annotations.SerializedName;

public class MessageToClientData {
    @SerializedName("room_id")

    private String roomId;
    @SerializedName("message")

    private String message;


    private String from;
}
