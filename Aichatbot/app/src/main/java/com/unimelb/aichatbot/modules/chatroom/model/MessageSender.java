package com.unimelb.aichatbot.modules.chatroom.model;

import com.google.gson.annotations.SerializedName;
import com.unimelb.aichatbot.network.dto.ChatWithBotRequest;
import com.unimelb.aichatbot.socketio.dto.RoomMessageData;

public interface MessageSender {


    public void sendMessage(ChatWithBotRequest chatWithBotRequest);

    public void sendMessage(RoomMessageData roomMessageData);

}
