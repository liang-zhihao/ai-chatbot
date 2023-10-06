package com.unimelb.aichatbot.modules.chat.services;

import com.unimelb.aichatbot.modules.chat.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("messages")
    Call<List<Message>> getAllMessages();

    //TODO more endpoints
 

    @POST("sendmessage")
    Call<Void> sendMessage(@Body Message message);

}

