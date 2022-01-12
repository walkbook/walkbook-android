package com.example.walkbookandroid;

public class LoginResponse {
    boolean success;
    int code;
    String msg;
    String token;
    User data;

    public String getToken() {
        return token;
    }

    public User getData() {
        return data;
    }
}
