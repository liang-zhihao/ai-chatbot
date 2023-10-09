package com.unimelb.aichatbot.modules.chatHistory.requestObject;

public class LoginRequest {
    private String user_id;
    private String password;

    public LoginRequest(String user_id, String password) {
        this.user_id = user_id;
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "user_id='" + user_id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
