package com.example.walkbookandroid.main;

import com.google.gson.annotations.SerializedName;

public class EditProfileRequest {
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("location")
    private String location;
    @SerializedName("introduction")
    private String introduction;

    public EditProfileRequest(String nickname, String location, String introduction) {
        this.nickname = nickname;
        this.location = location;
        this.introduction = introduction;
    }
}
