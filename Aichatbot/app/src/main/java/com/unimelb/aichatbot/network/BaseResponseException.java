package com.unimelb.aichatbot.network;

public class BaseResponseException extends Exception {
    private final BaseResponse<?> errorResponse;

    public BaseResponseException(BaseResponse<?> errorResponse) {
        super(errorResponse.getMessage()); // Use the message from BaseResponse as the exception message
        this.errorResponse = errorResponse;
    }

    public BaseResponse<?> getErrorResponse() {
        return errorResponse;
    }

    @Override
    public String toString() {
        return "BaseResponseException: " + super.toString();
    }
}