package com.example.walkbookandroid;

public class BaseResponse<T> {
    String success;
    int code;
    String msg;
    T data;
}
