package com.example.walkbookandroid;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitService {
    @Headers({"Content-Type: application/json"})
    @POST("api/user/signup")
    Call<JoinResponse> join(@Body JoinRequest joinRequest);
}
