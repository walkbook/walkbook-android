package com.example.walkbookandroid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoinRequest {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("introduction")
    @Expose
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
