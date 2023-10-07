package com.unimelb.aichatbot.util;

import com.google.gson.Gson;
import com.unimelb.aichatbot.network.BaseResponse;

import java.io.IOException;

import okhttp3.ResponseBody;

public class GsonHelper {
    public static BaseResponse getErrorResponse(ResponseBody errorBody) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(errorBody.string(), BaseResponse.class);
    }
}
