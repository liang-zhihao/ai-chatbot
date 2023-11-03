package com.unimelb.aichatbot.modules.profile.activity.server;

import com.unimelb.aichatbot.modules.profile.activity.request.ChangeUsernameRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/user/change_username")
    Call<Void> changeUsername(@Body ChangeUsernameRequest changeRequest);
}
