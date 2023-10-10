package com.unimelb.aichatbot.modules.account.service;

import com.unimelb.aichatbot.network.dto.LoginRequest;
import com.unimelb.aichatbot.network.dto.LoginResponse;
import com.unimelb.aichatbot.network.dto.SignUpRequest;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.dto.ChatWithBotRequest;
import com.unimelb.aichatbot.network.dto.ChatWithBotResponse;
import com.unimelb.aichatbot.network.dto.CreateChatbotRequest;
import com.unimelb.aichatbot.network.dto.CreateChatbotResponse;
import com.unimelb.aichatbot.network.dto.DeleteChatHistoryRequest;
import com.unimelb.aichatbot.network.dto.DeleteChatHistoryResponse;
import com.unimelb.aichatbot.network.dto.UserChatHistoryRequest;
import com.unimelb.aichatbot.network.dto.UserChatHistoryResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AccountService {
    // Get chat history
    @GET("api/chatbot/get_chat_history")
    Call<BaseResponse<UserChatHistoryResponse>> getChatHistory(@Body UserChatHistoryRequest chatHistoryRequest);

    // Create chatbot
    @POST("api/chatbot/create_chatbot")
    Call<BaseResponse<CreateChatbotResponse>> createChatbot(@Body CreateChatbotRequest createChatbotRequest);

    // Send a message to a chatbot
    @POST("api/chatbot/send_message")
    Call<BaseResponse<ChatWithBotResponse>> sendMessage(@Body ChatWithBotRequest messageRequest);


    // Delete user chat history with a chatbot
    @POST("api/chatbot/delete_chat_history")
    Call<BaseResponse<DeleteChatHistoryResponse>> deleteChatHistory(@Body DeleteChatHistoryRequest deleteRequest);

    // Register a user
    @POST("api/register")
    Call<BaseResponse<Void>> register(@Body SignUpRequest registerRequest);

    //        Call<BaseResponse<LoginResponse>> login(@Body LoginRequest loginRequest);
    // Authenticate and login a user
    @POST("api/login")
    Call<BaseResponse<LoginResponse>> login(@Body LoginRequest loginRequest);

}
