package com.example.walkbookandroid;

import com.google.gson.annotations.SerializedName;

public class PostRequest {
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("startLocation")
    private String startLocation;
    @SerializedName("finishLocation")
    private String finishLocation;
    @SerializedName("tmi")
    private String tmi;

    public PostRequest(String title, String description, String startLocation, String finishLocation, String tmi) {
        this.title = title;
        this.description = description;
        this.startLocation = startLocation;
        this.finishLocation = finishLocation;
        this.tmi = tmi;
    }
}
