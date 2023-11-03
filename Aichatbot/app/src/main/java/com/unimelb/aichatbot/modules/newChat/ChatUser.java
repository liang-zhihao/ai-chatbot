package com.unimelb.aichatbot.modules.newChat;

import com.unimelb.aichatbot.modules.common.model.FriendListItem;

public class ChatUser extends FriendListItem {

    private boolean isSelected;

    public ChatUser(String name, String avatarUrl, String desc) {
        super(avatarUrl, name, desc);
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

