package com.unimelb.aichatbot.modules.chat.model.type;

import com.google.gson.annotations.SerializedName;

public enum SenderType {
    @SerializedName("ME")
    ME,
    @SerializedName("OTHER")
    OTHER,
    @SerializedName("BOT")
    BOT
}
