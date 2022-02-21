package com.example.walkbookandroid;

import com.example.walkbookandroid.main.CommentRequest;
import com.example.walkbookandroid.main.CommentResponse;
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
    Call<PostsResponse> getPosts(@Header("X-AUTH-TOKEN") String token, @Query("page") int pageNum, @Query("size") int size, @Query("sort") String sort);

    @GET("api/post/{id}")
    Call<PostResponse> getPost(@Header("X-AUTH-TOKEN") String token, @Path("id") String postId);

    @GET("api/post/search")
    Call<PostsResponse> getSearchPosts(@Header("X-AUTH-TOKEN") String token, @Query("keyword") String keyword, @Query("sort") String sort);

    @Headers({"Content-Type: application/json"})
    @POST("api/post")
    Call<PostResponse> createPost(@Header("X-AUTH-TOKEN") String token, @Body PostRequest postRequest);

    @Headers({"Content-Type: application/json"})
    @PUT("api/post/{id}/edit")
    Call<PostResponse> editPost(@Header("X-AUTH-TOKEN") String token, @Path("id") int postId, @Body PostRequest postRequest);

    @DELETE("api/post/{id}/delete")
    Call<BaseResponse> deletePost(@Header("X-AUTH-TOKEN") String token, @Path("id") int postId);

    @Headers({"Content-Type: application/json"})
    @POST("api/post/{id}/like")
    Call<BaseResponse> likePost(@Header("X-AUTH-TOKEN") String token, @Path("id") int postId);

    @Headers({"Content-Type: application/json"})
    @POST("api/post/{id}/comment")
    Call<CommentResponse> createComment(@Header("X-AUTH-TOKEN") String token, @Path("id") int postId, @Body CommentRequest commentRequest);

    @Headers({"Content-Type: application/json"})
    @DELETE("api/post/{id}/comment/{cid}")
    Call<BaseResponse> deleteComment(@Header("X-AUTH-TOKEN") String token, @Path("id") int postId, @Path("cid") int commentId);
}
