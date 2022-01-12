package com.example.walkbookandroid;

public class JoinResponse {
    boolean success;
    int code;
    String msg;
    User data;

    public User getData() {
        return data;
    }
}
