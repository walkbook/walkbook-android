package com.example.walkbookandroid;

public class LoginResponse extends DataResponse<User> {
    String token;

    public String getToken() {
        return token;
    }
}
