package com.unimelb.aichatbot.network;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MessageService {


    OkHttpClient client = new OkHttpClient();
    private static final String URL_SEND_MESSAGE = "https://your-chat-server.com/send_message";
    private static final String URL_RECEIVE_MESSAGES = "https://your-chat-server.com/receive_messages";
//    private OkHttpClient client;

    public MessageService() {
        client = new OkHttpClient();
    }

    public void sendMessage(String message, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add("message", message)
                .build();

        Request request = new Request.Builder()
                .url(URL_SEND_MESSAGE)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void receiveMessages(Callback callback) {
        Request request = new Request.Builder()
                .url(URL_RECEIVE_MESSAGES)
                .build();

        client.newCall(request).enqueue(callback);
    }


}
