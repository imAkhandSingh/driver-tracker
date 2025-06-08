package com.zuree.driver_tracking.dto.response;

import lombok.Getter;

@Getter
public class ErrorResponse implements BaseResponse {
    private final boolean success = false;
    private final ApiError error;

    public ErrorResponse(int code, String message) {
        this.error = new ApiError(code, message);
    }
}

