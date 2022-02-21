package com.example.walkbookandroid;

import com.example.walkbookandroid.auth.JoinRequest;
import com.example.walkbookandroid.auth.JoinResponse;
import com.example.walkbookandroid.auth.LoginRequest;
import com.example.walkbookandroid.auth.LoginResponse;
import com.example.walkbookandroid.auth.UserResponse;
import com.example.walkbookandroid.main.EditProfileRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserRetrofitService {
    @Headers({"Content-Type: application/json"})
    @POST("api/user/signup")
    Call<JoinResponse> join(@Body JoinRequest joinRequest);

    @Headers({"Content-Type: application/json"})
    @POST("api/user/signin")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("api/user/{id}")
    Call<UserResponse> getUser(@Path("id") int userId);

    @PUT("api/user/{id}")
    Call<UserResponse> editUser(@Header("X-AUTH-TOKEN") String token, @Path("id") int userId, @Body EditProfileRequest editProfileRequest);
}
