package com.unimelb.aichatbot.modules.chatroom.model.type;

import com.google.gson.annotations.SerializedName;

public enum SenderType {
    @SerializedName("ME")
    ME,
    @SerializedName("OTHER")
    OTHER,
    @SerializedName("BOT")
    BOT
}
