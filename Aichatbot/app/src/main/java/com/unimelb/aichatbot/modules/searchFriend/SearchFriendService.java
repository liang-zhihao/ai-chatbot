package com.unimelb.aichatbot.modules.searchFriend;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import com.unimelb.aichatbot.modules.searchFriend.model.SearchFriendRequest;
import com.unimelb.aichatbot.modules.searchFriend.model.SearchFriendResponse;
import com.unimelb.aichatbot.network.BaseResponse;

import java.util.List;

public interface SearchFriendService {
    @POST("searchFriend")
    Call<BaseResponse<SearchFriendResponse>> searchFriendByName(@Body SearchFriendRequest searchFriendRequest);
}
