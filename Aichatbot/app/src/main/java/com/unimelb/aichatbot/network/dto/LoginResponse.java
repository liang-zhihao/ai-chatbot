package com.unimelb.aichatbot.network.dto;

import com.google.gson.annotations.SerializedName;

//public class LoginResponse {
//    @SerializedName("access_token")
//    String accessToken;
//
//    public String getAccessToken() {
//        return accessToken;
//    }
//
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }
//}

public class LoginResponse {
    @SerializedName("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

