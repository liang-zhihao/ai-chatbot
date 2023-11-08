package com.unimelb.aichatbot.modules.account.service;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("access_token")
    String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
