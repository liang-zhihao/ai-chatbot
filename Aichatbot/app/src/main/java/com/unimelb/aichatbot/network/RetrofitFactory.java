package com.unimelb.aichatbot.network;

import android.content.Context;

import com.unimelb.aichatbot.BuildConfig;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.util.LoginManager;
import com.unimelb.aichatbot.util.SharedPreferencesHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitFactory {
    private static final Map<String, Retrofit> OTHERS = new ConcurrentHashMap<>(2);
    private static final String BASE_URL = BuildConfig.SERVER_URL;
    // private static final String BASE_URL = BuildConfig.SERVER_LOCAL_URL;

    private RetrofitFactory() {
        OTHERS.put("default", buildRetrofit(new OkHttpClient.Builder()));
    }

    private static Retrofit buildRetrofit(OkHttpClient.Builder httpClient) {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public static <T> T createWithAuth(Class<T> cls, Context applicationContext) {
        Interceptor authorizationInterceptor = chain -> {
            Request original = chain.request();
            LoginManager loginManager = LoginManager.getInstance(applicationContext.getApplicationContext());
            // Get the token dynamically from SharedPreferences
            String token = loginManager.getAccessToken();
            // Add Authorization header
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .method(original.method(), original.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
        OkHttpClient.Builder httpClientWithAuth = new OkHttpClient.Builder().addInterceptor(authorizationInterceptor);
        Retrofit retrofitWithAuth = buildRetrofit(httpClientWithAuth);
        return retrofitWithAuth.create(cls);
    }

    public static <T> T create(Class<T> cls) {
        Retrofit retrofit = buildRetrofit(new OkHttpClient.Builder());
        return retrofit.create(cls);
    }
}
