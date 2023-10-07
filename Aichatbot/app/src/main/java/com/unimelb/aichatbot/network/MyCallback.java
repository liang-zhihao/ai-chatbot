package com.unimelb.aichatbot.network;

import androidx.annotation.NonNull;

import com.unimelb.aichatbot.util.GsonHelper;

public abstract class MyCallback<T> implements retrofit2.Callback<BaseResponse<T>> {

    public abstract void onSuccess(BaseResponse<T> result);

    public abstract void onError(BaseResponse error, @NonNull Throwable t);

    @Override
    public void onResponse(@NonNull retrofit2.Call<BaseResponse<T>> call, @NonNull retrofit2.Response<BaseResponse<T>> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            assert response.errorBody() != null;
            try {
                BaseResponse errorResponse = GsonHelper.getErrorResponse(response.errorBody());
                onError(errorResponse, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onFailure(@NonNull retrofit2.Call<BaseResponse<T>> call, @NonNull Throwable t) {
        onError(null, t);
    }
}

