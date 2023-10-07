package com.unimelb.aichatbot.network;

import androidx.annotation.NonNull;

import com.unimelb.aichatbot.modules.chat.service.ChatService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:3000/";
    private static Retrofit retrofit = null;

    public static ChatService getService() {
        if (retrofit == null) {
            // Create an instance of OkHttpClient
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            // Add an Interceptor to the OkHttpClient.
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();

                // Get the token from your TokenProvider. TODO
                String token = "1";

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // <-- using the log level
                    .build();
        }
//        TODO  retrofit factory
        return retrofit.create(ChatService.class);
    }
}

