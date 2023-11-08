package com.unimelb.aichatbot.modules.newChat.model;

import com.unimelb.aichatbot.network.dto.UserInfoResponse;

import java.util.List;

public class FriendListResponse {
    public List<UserInfoResponse> friends;

    public List<UserInfoResponse> getFriends() {
        return friends;
    }

    @Override
    public String toString() {
        return "FriendListResponse{" +
                "friends=" + friends +
                '}';
    }

    public void setFriends(List<UserInfoResponse> friends) {
        this.friends = friends;
    }
}
