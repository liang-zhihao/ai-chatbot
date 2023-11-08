package com.unimelb.aichatbot.modules.chatroom.model;

import androidx.annotation.NonNull;

import com.unimelb.aichatbot.modules.chatroom.model.type.MessageType;
import com.unimelb.aichatbot.modules.chatroom.model.type.SenderType;
import com.unimelb.aichatbot.util.DateParser;

import java.util.Date;
// UUID
import java.util.Objects;
import java.util.UUID;

public class Message {
    private String id = UUID.randomUUID().toString();
    private String content; // content of the message
    private MessageType type; // type of the message
    private String senderId; // sender of the message
    private Date timestamp; // timestamp of the message

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    private String senderName; // name of the sender
    private String avatarUrl; // avatar of the sender
    private SenderType senderType; // to check if the message is sent by the user

    public Message(String content, MessageType type, String senderId, SenderType senderType, String timestampStr, String senderName) {
        this.content = content;
        this.type = type;
        this.senderId = senderId;
        this.timestamp = DateParser.parse(timestampStr);
        this.senderType = senderType;
        this.senderName = senderName;
    }

    public Message(String content, MessageType type, String senderId, SenderType senderType, Date timestamp, String senderName) {
        this.content = content;
        this.type = type;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.senderType = senderType;
        this.senderName = senderName;
    }

    // Getters and Setters for the fields
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    // Create Getter and Setter for senderType
    public SenderType getSenderType() {
        return senderType;
    }

    public void setSenderType(SenderType senderType) {
        this.senderType = senderType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Message)) {
            return false;
        }
        Message message = (Message) o;
        return Objects.equals(id, message.getId());
    }

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", sender='" + senderId + '\'' +
                ", timestamp=" + timestamp +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", senderType=" + senderType +
                '}';
    }
}
