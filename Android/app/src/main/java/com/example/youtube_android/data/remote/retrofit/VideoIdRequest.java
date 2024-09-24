package com.example.youtube_android.data.remote.retrofit;

import com.google.gson.annotations.SerializedName;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class VideoIdRequest {
    @SerializedName("videoId")
    private RequestBody videoId;

    public VideoIdRequest(String videoId) {
        this.videoId = RequestBody.create(MediaType.parse("text/plain"), videoId);
//        this.videoId = RequestBody.create(videoId, okhttp3.MediaType.parse("text/plain"));
    }

    public RequestBody getVideoId() {
        return videoId;
    }
}
