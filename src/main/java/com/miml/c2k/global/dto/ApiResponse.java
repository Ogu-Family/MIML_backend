package com.miml.c2k.global.dto;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    int code;
    String message;
    T data;

    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> create(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }
}
