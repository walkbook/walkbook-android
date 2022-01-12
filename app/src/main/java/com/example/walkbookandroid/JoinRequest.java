package com.example.walkbookandroid;

import com.google.gson.annotations.SerializedName;

public class JoinRequest {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("gender")
    private String gender;
    @SerializedName("age")
    private String age;
    @SerializedName("location")
    private String location;
    @SerializedName("introduction")
    private String introduction;

    public JoinRequest(String username, String password, String nickname, String gender, String age, String location, String introduction) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.location = location;
        this.introduction = introduction;
    }
}
