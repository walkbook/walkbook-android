package com.example.walkbookandroid;

import com.example.walkbookandroid.main.PostRequest;
import com.example.walkbookandroid.main.PostResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostRetrofitService {
    @GET("api/post/{id}")
    Call<PostResponse> getPost(@Path("id") String postId);

    @Headers({"Content-Type: application/json"})
    @POST("api/post/create")
    Call<PostResponse> create(@Header("X-AUTH-TOKEN") String token, @Body PostRequest postRequest);

    @Headers({"Content-Type: application/json"})
    @PUT("api/post/{id}/edit")
    Call<PostResponse> edit(@Header("X-AUTH-TOKEN") String token, @Path("id") String postId, @Body PostRequest postRequest);

    @DELETE("api/post/{id}/delete")
    Call<BaseResponse> delete(@Header("X-AUTH-TOKEN") String token, @Path("id") String postId);
}
