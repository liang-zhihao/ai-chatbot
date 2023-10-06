package com.unimelb.aichatbot.modules.chat.model.type;

import com.google.gson.annotations.SerializedName;

public enum MessageType {
    @SerializedName("TEXT")
    TEXT, // For markdown/text messages
    @SerializedName("VIDEO")

    VIDEO,
    @SerializedName("VOICE")
    VOICE,
    @SerializedName("MAP")
    MAP,
    @SerializedName("IMAGE")
    IMAGE
}

