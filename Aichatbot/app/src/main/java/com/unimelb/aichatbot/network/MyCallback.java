package com.unimelb.aichatbot.network;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.util.Log;

import androidx.annotation.NonNull;

import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.internal.Util;
import okhttp3.internal.http2.ConnectionShutdownException;
import okio.BufferedSource;
import retrofit2.Response;

import com.google.gson.Gson;
import com.unimelb.aichatbot.network.dto.ErrorResponse;
import com.unimelb.aichatbot.util.GsonHelper;

import java.io.IOException;

import okio.BufferedSource;
import retrofit2.Response;

public abstract class MyCallback<T> implements retrofit2.Callback<BaseResponse<T>> {
    private static final String TAG = "MyCallback";

    public abstract void onSuccess(BaseResponse<T> result);

    public abstract void onError(ErrorResponse error, @NonNull Throwable t);

    // NOTE TL;DR Use the code below to get error body string from response. It can be used repeatedly and will not return an empty string after the first use.
    public static String getErrorBodyString(Response<?> response) throws IOException {
        // Get a copy of error body's BufferedSource.
        BufferedSource peekSource = response.errorBody().source().peek();
        // Get the Charset, as in the original response().errorBody().string() implementation
        MediaType contentType = response.errorBody().contentType(); //
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        charset = Util.bomAwareCharset(peekSource, charset);
        // Read string without consuming data from the original BufferedSource.
        return peekSource.readString(charset);
    }

    @Override
    public void onResponse(@NonNull retrofit2.Call<BaseResponse<T>> call, @NonNull retrofit2.Response<BaseResponse<T>> response) {
        Log.d(TAG, "Sent a request: " + call.request());
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            assert response.errorBody() != null;
            try {


                String responseString = getErrorBodyString(response);
                Gson gson = new Gson();
                ErrorResponse errorResponse = gson.fromJson(responseString, ErrorResponse.class);
                assert errorResponse != null;
                onError(errorResponse, null);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onFailure(@NonNull retrofit2.Call<BaseResponse<T>> call, @NonNull Throwable t) {
        onError(null, t);
    }
}

