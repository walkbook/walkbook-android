package com.example.walkbookandroid;

public class User {
    int userId;
    String username;
    String nickname;
    String gender;
    int age;
    String location;
    String introduction;

    public User(int userId, String username, String nickname, String gender, int age, String location, String introduction) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.location = location;
        this.introduction = introduction;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getLocation() {
        return location;
    }

    public String getIntroduction() {
        return introduction;
    }
}
