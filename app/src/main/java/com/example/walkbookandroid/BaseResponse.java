package com.example.walkbookandroid;

public class BaseResponse {
    boolean success;
    int code;
    String msg;

    public boolean isSuccessful() { return success; }

    public int getCode() { return code; }

    public String getMsg() { return msg; }
}
