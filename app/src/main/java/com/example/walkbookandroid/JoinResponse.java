package com.example.walkbookandroid;

public class JoinResponse {
    boolean success;
    int code;
    String msg;
    User data;

    public class User {
        int userId;
        String username;
        String nickname;
        char gender;
        int age;
        String location;
        String introduction;

        public User(int userId, String username, String nickname, char gender, int age, String location, String introduction) {
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
    }

    public User getData() {
        return data;
    }
}
