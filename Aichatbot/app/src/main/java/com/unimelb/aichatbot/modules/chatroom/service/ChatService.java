package com.unimelb.aichatbot.modules.chatroom.service;

import com.unimelb.aichatbot.modules.chatroom.model.ChatDetailResponse;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.dto.ChatWithBotRequest;
import com.unimelb.aichatbot.network.dto.ChatWithBotResponse;
import com.unimelb.aichatbot.network.dto.UserChatHistoryResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatService {

    // @GET("messages")
    // Call<List<Message>> getAllMessages();

    @GET("api/chat/get_chat_history")
    Call<BaseResponse<UserChatHistoryResponse>> getAllMessages(@Query("room_id") String roomId);


    @POST("api/chatbot/send_message")
    Call<BaseResponse<ChatWithBotResponse>> sendMessage(@Body ChatWithBotRequest chatWithBotRequest);

    @GET("api/chat/chat_room_details")
    Call<BaseResponse<ChatDetailResponse>> getChatRoomDetails(@Query("room_id") String roomId);

    //     api/chat/find_chatroom_by_friend_id
    @GET("api/chat/find_chatroom_by_friend_id")
    Call<BaseResponse<ChatDetailResponse>> getChatRoomDetailsByFriendId(@Query("friend_id") String friendId, @Query("user_id") String userId);
}

