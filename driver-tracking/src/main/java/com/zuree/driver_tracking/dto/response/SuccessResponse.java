package com.zuree.driver_tracking.dto.response;

public class SuccessResponse<T> implements BaseResponse {
    private final T data;

    public SuccessResponse(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return true;
    }

    public T getData() {
        return data;
    }
}

