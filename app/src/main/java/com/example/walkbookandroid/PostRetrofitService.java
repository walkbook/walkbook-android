package com.example.walkbookandroid;

import com.example.walkbookandroid.main.PostRequest;
import com.example.walkbookandroid.main.PostResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PostRetrofitService {
    @Headers({"Content-Type: application/json"})
    @POST("api/post/create")
    Call<PostResponse> create(@Header("X-AUTH-TOKEN") String token, @Body PostRequest postRequest);
}
