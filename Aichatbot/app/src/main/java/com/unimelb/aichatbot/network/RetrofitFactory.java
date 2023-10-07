package com.unimelb.aichatbot.network;

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
    private static final String BASE_URL = "http://10.0.2.2:3000/";

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

    public static <T> T createWithAuth(Class<T> cls, SharedPreferencesHelper sharePreferenceHelper) {
            Interceptor authorizationInterceptor = chain -> {
                Request original = chain.request();
                // Get the token dynamically from SharedPreferences
                String token = sharePreferenceHelper.getString(SharedPreferencesHelper.KEY_JWT_TOKEN);
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
        Retrofit retrofit =  buildRetrofit(new OkHttpClient.Builder());
        return retrofit.create(cls);
    }
}
