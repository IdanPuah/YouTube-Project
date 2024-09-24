package com.example.youtube_android.data.remote.api;

import com.example.youtube_android.data.remote.retrofit.CommentRequest;
import com.example.youtube_android.data.remote.retrofit.CommentResponse;
import com.example.youtube_android.data.remote.retrofit.CommentResponseList;
import com.example.youtube_android.data.remote.retrofit.EditCommentRequest;
import com.example.youtube_android.data.remote.retrofit.HandleLikeRequest;
import com.example.youtube_android.data.remote.retrofit.LoginRequest;
import com.example.youtube_android.data.remote.retrofit.ResponseMessage;
import com.example.youtube_android.data.remote.retrofit.UpdateUserRequest;
import com.example.youtube_android.data.remote.retrofit.VideoResponse;
import com.example.youtube_android.data.remote.retrofit.VideoResponseList;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/tokens")
    Call<ResponseBody> login(@Body LoginRequest loginRequest);


    // Video
    @GET("/api/videos")
    Call<List<VideoResponse>> getTwentyVideos(@Query("limit") int limit);

    @GET("/api/users/{id}/videos/{pid}")
    Call<VideoResponse> getVideo(
            @Path("id") String id,
            @Path("pid") String pid
    );

    @Multipart
    @POST("/api/users/{id}/videos")
    Call<VideoResponse> addVideo(
            @Header("Authorization") String token,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("category") RequestBody category,
            @Part MultipartBody.Part videoSrc,
            @Part MultipartBody.Part thumbnail
    );

    @DELETE("/api/users/{id}/videos/{pid}")
    Call<VideoResponse> deleteVideo(
            @Header("Authorization") String token,
            @Path("id") String userId,
            @Path("pid") String videoId
    );


    @GET("/api/videos/{query}")
    Call<List<VideoResponse>> getSearchVideos(@Path("query") String query);

    @Multipart
    @PUT("/api/users/{id}/videos/{pid}")
    Call<VideoResponse> editVideo(
            @Header("Authorization") String token,
            @Path("id") String userId,
            @Path("pid") String videoId,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("category") RequestBody category,
            @Part MultipartBody.Part thumbnail
    );

    @GET("/api/users/{id}/videos")
    Call<List<VideoResponse>> getUserVideos(
            @Path("id") String userId);


    @Multipart
    @POST("/api/users")
    Call<ResponseBody> signUp(
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part photo
    );

    @FormUrlEncoded
    @POST("/getLatestComments")
    Call<CommentResponseList> getVideoComments(
            @Field("videoId") String videoIdRequest
    );

    @POST("/addComment")
    Call<CommentResponse> addComment(
            @Header("Authorization") String token,
            @Body CommentRequest commentBody
    );

    @DELETE("/deleteComment")
    Call<CommentResponse> deleteComment(
            @Header("Authorization") String token,
            @Query("commentId") String commentId
    );

    @POST("/editComment")
    Call<CommentResponse> editComment(
            @Header("Authorization") String token,
            @Body EditCommentRequest request
    );

    @POST("/api/videos/handleLikesChange")
    Call<VideoResponse> handleLikeChange(
            @Header("Authorization") String token,
            @Body HandleLikeRequest body
    );

    @POST("/api/videos/getLikeStatus")
    Call<ResponseMessage> getLikeStatus(
            @Header("Authorization") String token,
            @Body HandleLikeRequest body
    );

    @PUT("/api/users/{id}")
    Call<ResponseMessage> updateUser(
            @Header("Authorization") String token,
            @Path("id") String userId,
            @Body UpdateUserRequest updateUserRequest
            );

    @DELETE("/api/users/{id}")
    Call<ResponseMessage> deleteUser(
            @Header("Authorization") String token,
            @Path("id") String userId
    );

    @GET("/api/users/{userId}/suggestions/{videoId}")
    Call<VideoResponseList> getRelatedVideos(
            @Path("userId") String userId,
            @Path("videoId") String videoId
    );
}




