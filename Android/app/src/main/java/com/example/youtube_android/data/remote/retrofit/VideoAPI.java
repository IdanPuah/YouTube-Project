package com.example.youtube_android.data.remote.retrofit;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube_android.R;
import com.example.youtube_android.data.model.Comment;
import com.example.youtube_android.data.model.Video;
import com.example.youtube_android.data.remote.api.ApiService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoAPI {
    private ApiService apiService;
    private Retrofit retrofit;

    public VideoAPI (Context context){
        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.BaseUrl))
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public void getTwentyVideos(MutableLiveData<List<VideoResponse>> videoResponse) {
        // connect to server
        Call<List<VideoResponse>> call = apiService.getTwentyVideos(20);

        call.enqueue(new Callback<List<VideoResponse>>() {
            @Override
            public void onResponse(Call<List<VideoResponse>> call, Response<List<VideoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("VideoAPI", "Response: " + response.body());

                    videoResponse.postValue(response.body());

                } else {
                    videoResponse.postValue(null);
                    Log.e("VideoAPI", "Response Error: " + response.errorBody().toString());
                }

            }

            @Override
            public void onFailure(Call<List<VideoResponse>> call, Throwable t) {
                Log.e("VideoAPI", "Network Error: " + t.getMessage());
            }
        });
    }

    public void addVideo(String token, MutableLiveData<VideoResponse> videoResponse, VideoRequest videoToAddRequest){
        Log.e("VideoRepository", "add video: " + videoToAddRequest);
        Call<VideoResponse> call = apiService.addVideo(token, videoToAddRequest.getTitle(),
                videoToAddRequest.getDescription(), videoToAddRequest.getCategory(),
                videoToAddRequest.getVideoUrl(), videoToAddRequest.getThumbnailUrl());

        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful()){
                    videoResponse.postValue(response.body());
                }
                else{
                    videoResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.e("VideoRepository", "Error adding video: " + t.getMessage());
            }
        });
    }

    public void getVideo(String _id,String userId, MutableLiveData<VideoResponse> videoResponse){
        Call<VideoResponse> call = apiService.getVideo(userId, _id);

        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful()){
                    Log.d("VideoRepository", "got video from server !");
                    videoResponse.postValue(response.body());
                }
                else {
                    Log.d("VideoRepository", "Failed to retrieve video, response code: " + response.code());
                    videoResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.e("VideoRepository", "Error get video: " + t.getMessage());
            }
        });
    }

    public void getSearchVideos(String query, MutableLiveData<List<VideoResponse>> videoResponses){
        Call<List<VideoResponse>> call = apiService.getSearchVideos(query);

        call.enqueue(new Callback<List<VideoResponse>>() {
            @Override
            public void onResponse(Call<List<VideoResponse>> call, Response<List<VideoResponse>> response) {
                if (response.isSuccessful()){
                    Log.d("Video Repository", "search videos back from server");
                    videoResponses.postValue(response.body());
                }
                else {
                    Log.d("Video Repository", "search videos not back from server");
                    videoResponses.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<VideoResponse>> call, Throwable t) {
                Log.e("VideoRepository", "Error search video: " + t.getMessage());
            }
        });
    }

    public void deleteVideo(String token, Video videoToDelete,String userId, MutableLiveData<VideoResponse> videoResponse){
        Call<VideoResponse> call = apiService.deleteVideo(token,userId, videoToDelete.get_id());

        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful()){
                    Log.d("Video Repository", "deleted video back from server");
                    videoResponse.postValue(response.body());
                }
                else{
                    Log.d("Video Repository", "deleted video not back from server");
                    videoResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.e("VideoRepository", "Error in delete video: " + t.getMessage());
            }
        });
    }

    public void editVideo(String token, VideoRequest videoRequest, String videoId, MutableLiveData<VideoResponse> videoResponse, String userId){
        Log.d("VideoApi:", "api service img: " + videoRequest.getThumbnailUrl());
        Call<VideoResponse> call = apiService.editVideo(token, userId, videoId,
                videoRequest.getTitle(), videoRequest.getDescription(), videoRequest.getCategory(), videoRequest.getThumbnailUrl());

        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful()){
                    videoResponse.postValue(response.body());
                }
                else {
                    videoResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.e("VideoRepository", "Error in edit video: " + t.getMessage());
            }
        });
    }

    public void getUserVideos(String userId, MutableLiveData<List<VideoResponse>> listMutableLiveData){
        Call<List<VideoResponse>> call = apiService.getUserVideos(userId);

        call.enqueue(new Callback<List<VideoResponse>>() {
            @Override
            public void onResponse(Call<List<VideoResponse>> call, Response<List<VideoResponse>> response) {
                if (response.isSuccessful()){
                    listMutableLiveData.postValue(response.body());
                }
                else {
                    listMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<VideoResponse>> call, Throwable t) {
                Log.e("VideoRepository", "Error in get user videos: " + t.getMessage());
            }
        });
    }

//    public void getVideoComments(String videoId, CommentCallback callback) {
//        VideoIdRequest videoIdRequest = new VideoIdRequest(videoId);
//        Call<CommentResponse> call = apiService.getVideoComments(videoId); // Assuming you have an API service with this method
//        call.enqueue(new Callback<CommentResponse>() {
//            @Override
//            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Comment> comments = response.body().getComments();
//                    Log.d("video api", "video api " + comments.get(0).getAuthor());
//                    for (Comment comment : comments) {
//                        if (comment.getAuthor() == null) {
//                            Log.w("VideoAPI", "Comment has a null author: " + comment.getText());
//                        } else {
//                            Log.d("VideoAPI", "Comment by user: " + comment.getAuthor().getUsername());
//                        }
//                    }
//                    callback.onSuccess(comments);
//                } else {
//                    try {
//                        String errorResponse = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
//                        Log.e("VideoAPI", "Failed to load comments. HTTP Code: " + response.code() + ", Error: " + errorResponse);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CommentResponse> call, Throwable t) {
//                callback.onFailure(t);
//            }
//        });
//    }

    public void getVideoComments(String videoId, MutableLiveData<List<CommentResponse>> commentResponse) {
        Call<CommentResponseList> call = apiService.getVideoComments(videoId);

        call.enqueue(new Callback<CommentResponseList>() {
            @Override
            public void onResponse(Call<CommentResponseList> call, Response<CommentResponseList> response) {
                if (response.isSuccessful()) {
                    CommentResponseList commentResponseList = response.body();
                    if (commentResponseList != null) {
                        Log.d("video api", "video api response: " + commentResponseList.getComments());
                        commentResponse.postValue(commentResponseList.getComments());
                    } else {
                        Log.d("video api", "No comments found");
                        commentResponse.postValue(null);
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.d("video api", "video api response fail: " + errorBody);
                    } catch (IOException e) {
                        Log.e("video api", "Error reading error body", e);
                    }
                    commentResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<CommentResponseList> call, Throwable t) {
                Log.e("VideoApi", "Error in get video comments: " + t.getMessage());
            }
        });
    }

    public void addComment(String token, String videoId, Comment comment, MutableLiveData<Boolean> isCommentAdded){
        CommentRequest commentRequest = new CommentRequest(videoId, comment.getText());

        Call<CommentResponse> call = apiService.addComment(token, commentRequest);

        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()){
                    isCommentAdded.postValue(true);
                    CommentResponse commentResponse = response.body();
                    System.out.println("Comment added successfully");
                } else {
                    isCommentAdded.postValue(false);
                    System.out.println("Failed to add comment: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void deleteComment(String token, String commentId, MutableLiveData<Boolean> isCommentDeleted){

        Call<CommentResponse> call = apiService.deleteComment(token, commentId);

        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("video api", "video api video delete ");
                    isCommentDeleted.postValue(true);
                } else {
                    Log.d("video api", "video api video not delete: " + response.code() + " " + response.message());
                    try {
                        Log.d("video api", "Response body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isCommentDeleted.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Log.e("VideoApi", "Error in delete video comment: " + t.getMessage());
            }
        });
    }

    public void editComment(String token, String commentId, String commentText, MutableLiveData<Boolean> isCommentEdited){
        EditCommentRequest editCommentRequest = new EditCommentRequest(commentId, commentText);
        Call<CommentResponse> call = apiService.editComment(token, editCommentRequest);

        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("video api", "video api video was edit ");
                    isCommentEdited.postValue(true);
                } else {
                    Log.d("video api", "video api video not edit: " + response.code() + " " + response.message());
                    try {
                        Log.d("video api", "Response body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isCommentEdited.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Log.e("VideoApi", "Error in delete video comment: " + t.getMessage());
            }
        });
    }

    public void handleLikeChange(String token, String videoId, String action){
        HandleLikeRequest handleLikeRequest = new HandleLikeRequest(videoId, action);
        Call<VideoResponse> call = apiService.handleLikeChange(token, handleLikeRequest);

        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("video api", "handle like successful ");
                } else {
                    Log.d("video api", "handle like failed: " + response.code() + " " + response.message());
                    try {
                        Log.d("video api", "Response body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.e("VideoApi", "Error in handle likes: " + t.getMessage());
            }
        });
    }

    public void getLikeStatus(String token, String videoId, MutableLiveData<Integer> likeStatus){
        HandleLikeRequest handleLikeRequest = new HandleLikeRequest(videoId);
        Call<ResponseMessage> call = apiService.getLikeStatus(token, handleLikeRequest);

        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("video api", "like status return ");
                    if (response.body().getMessage().equals("like pressed")){
                        likeStatus.postValue(1);
                    }
                    else if (response.body().getMessage().equals("like not pressed")){
                        likeStatus.postValue(2);
                    }
                } else {
                    Log.d("video api", "error in get like status: " + response.code() + " " + response.message());
                    try {
                        Log.d("video api", "Response body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    likeStatus.postValue(0);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e("VideoApi", "Error in get like status: " + t.getMessage());
            }
        });
    }

    public void getRelatedVideos(String videoId, String userId, MutableLiveData<List<VideoResponse>> mutableLiveData) {
        Call<VideoResponseList> call = apiService.getRelatedVideos(userId, videoId);

        call.enqueue(new Callback<VideoResponseList>() {
            @Override
            public void onResponse(Call<VideoResponseList> call, Response<VideoResponseList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mutableLiveData.postValue(response.body().getVideos());
                } else {
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<VideoResponseList> call, Throwable t) {
                Log.e("VideoApi", "Error in get related videos: " + t.getMessage());
            }
        });
    }


//    public void getVideoComments(String videoId, CommentCallback callback) {
//        Call<List<CommentResponse>> call = apiService.getVideoComments(videoId);
//        call.enqueue(new Callback<List<CommentResponse>>() {
//            @Override
//            public void onResponse(Call<List<CommentResponse>> call, Response<List<CommentResponse>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<CommentResponse> commentResponses = response.body();
//                    List<Comment> comments = new ArrayList<>();
//
//                    for (CommentResponse responseItem : commentResponses) {
//                        // Create User object if needed
//                        User user = new User();
//                        user.setId(responseItem.getUserId());
//                        user.setUsername(responseItem.getUsername());
//
//                        // Convert userImg string to Uri
//                        Uri userImgUri = responseItem.getProfileImg() != null ? Uri.parse(responseItem.getProfileImg()) : null;
//
//                        // Create Comment object
//                        Comment comment = new Comment(user, responseItem.getComment(), responseItem.getVideoId(), responseItem.getDate(), userImgUri);
//                        comments.add(comment);
//                    }
//                    Log.d("video api", "video api comment list: " + comments);
//
//                    callback.onSuccess(comments);
//                } else {
//                    callback.onFailure(new Throwable("Error: " + response.code()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<CommentResponse>> call, Throwable t) {
//                callback.onFailure(t);
//            }
//        });
//    }


    public interface CommentCallback {
        void onSuccess(List<Comment> comments);
        void onFailure(Throwable t);
    }
}
