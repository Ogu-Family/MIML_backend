package com.miml.c2k.global.dto;

public class ApiResponse<T> {
    int code;
    String message;
    T data;
}
