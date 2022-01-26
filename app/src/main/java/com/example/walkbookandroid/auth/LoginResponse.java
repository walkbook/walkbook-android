package com.example.walkbookandroid.auth;

import com.example.walkbookandroid.DataResponse;
import com.example.walkbookandroid.User;

public class LoginResponse extends DataResponse<User> {
    String token;

    public String getToken() {
        return token;
    }
}
