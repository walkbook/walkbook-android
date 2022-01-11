package com.example.walkbookandroid;

import com.google.gson.annotations.SerializedName;

public class JoinResponse {
    boolean success;
    int code;
    String msg;

    @SerializedName("data")
    int data;

    public boolean isSuccess() {
        return success;
    }

    public void setUserId(int userId) {
        this.data = userId;
    }

    public int getUserId() {
        return data;
    }
}
