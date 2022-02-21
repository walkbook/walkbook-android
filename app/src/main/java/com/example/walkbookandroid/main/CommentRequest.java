package com.example.walkbookandroid.main;

import com.google.gson.annotations.SerializedName;

public class CommentRequest {
    @SerializedName("content")
    private String content;

    public CommentRequest(String content) {
        this.content = content;
    }
}
