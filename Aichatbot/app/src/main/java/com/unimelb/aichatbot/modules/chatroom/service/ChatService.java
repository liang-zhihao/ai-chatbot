package com.unimelb.aichatbot.modules.chatroom.service;

import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.dto.ChatWithBotRequest;
import com.unimelb.aichatbot.network.dto.ChatWithBotResponse;
import com.unimelb.aichatbot.network.dto.UserChatHistoryRequest;
import com.unimelb.aichatbot.network.dto.UserChatHistoryResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatService {

    // @GET("messages")
    // Call<List<Message>> getAllMessages();
    @Headers("Content-Type:application/json")
    @POST("api/chatbot/get_chat_history")
    Call<BaseResponse<UserChatHistoryResponse>> getAllMessages(@Body UserChatHistoryRequest userChatHistoryRequest);
    // TODO more endpoints

    @POST("api/chatbot/send_message")
    Call<BaseResponse<ChatWithBotResponse>> sendMessage(@Body ChatWithBotRequest chatWithBotRequest);

}

