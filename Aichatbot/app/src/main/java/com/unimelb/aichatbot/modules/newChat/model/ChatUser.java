package com.unimelb.aichatbot.modules.newChat.model;

import com.unimelb.aichatbot.modules.common.model.FriendListItem;

public class ChatUser extends FriendListItem {

    private boolean isSelected;

    public ChatUser(String name, String avatarUrl, String desc, String userId) {
        super(avatarUrl, name, desc, userId);
        this.isSelected = false;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "ChatUser{" +
                "name='" + getName() + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
    // Getters, setters omitted for brevity
}

