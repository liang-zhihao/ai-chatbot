package com.unimelb.aichatbot.util;

import com.google.gson.Gson;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.dto.ErrorResponse;

import java.io.IOException;

import okhttp3.ResponseBody;

public class GsonHelper {
    public static ErrorResponse getErrorResponse(ResponseBody errorBody) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(errorBody.string(), ErrorResponse.class);
    }
}
