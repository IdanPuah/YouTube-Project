package com.example.youtube_android.data.remote.retrofit;

import com.google.gson.annotations.SerializedName;

public class HandleLikeRequest {
    @SerializedName("videoId")
    private String videoId;
    @SerializedName("action")
    private String action;

    public HandleLikeRequest(String videoId){
        this.videoId = videoId;
    }

    public HandleLikeRequest(String videoId, String action) {
        this.videoId = videoId;
        this.action = action;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getAction() {
        return action;
    }
}
