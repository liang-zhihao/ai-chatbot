package com.unimelb.aichatbot.modules.searchFriend.model;

import com.unimelb.aichatbot.network.dto.UserInfoResponse;

import java.util.List;

public class SearchFriendResponse {
    private List<UserInfoResponse> users;

    public SearchFriendResponse(List<UserInfoResponse> users) {
        this.users = users;
    }

    public List<UserInfoResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoResponse> users) {
        this.users = users;
    }
// Getters and setters for message and status
}
