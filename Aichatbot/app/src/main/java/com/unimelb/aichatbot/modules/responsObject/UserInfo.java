package com.unimelb.aichatbot.modules.responsObject;

import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("data")
    private Data data;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private int status;
    @SerializedName("success")
    private boolean success;


    // getters and setters...

    public static class Data {
        @SerializedName("access_token")
        private String accessToken;


        public String getAccessToken() {
            return accessToken;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "accessToken='" + accessToken + '\'' +
                    '}';
        }


    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", success=" + success +
                '}';
    }

    public Data getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }
}
