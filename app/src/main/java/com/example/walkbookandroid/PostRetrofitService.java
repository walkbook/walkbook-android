package com.example.walkbookandroid;

import com.example.walkbookandroid.main.PostRequest;
import com.example.walkbookandroid.main.PostResponse;
import com.example.walkbookandroid.main.PostsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostRetrofitService {
    @GET("api/post/page")
    Call<PostsResponse> getPosts(@Query("page") int pageNum, @Query("size") int size, @Query("sort") String sort);

    @GET("api/post/{id}")
    Call<PostResponse> getPost(@Path("id") String postId);

    @Headers({"Content-Type: application/json"})
    @POST("api/post")
    Call<PostResponse> createPost(@Header("X-AUTH-TOKEN") String token, @Body PostRequest postRequest);

    @Headers({"Content-Type: application/json"})
    @PUT("api/post/{id}/edit")
    Call<PostResponse> editPost(@Header("X-AUTH-TOKEN") String token, @Path("id") int postId, @Body PostRequest postRequest);

    @DELETE("api/post/{id}/delete")
    Call<BaseResponse> deletePost(@Header("X-AUTH-TOKEN") String token, @Path("id") int postId);
}
