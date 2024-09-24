package com.example.youtube_android.data.remote.retrofit;

import com.google.gson.annotations.SerializedName;

public class CommentRequest {
    @SerializedName("videoId")
    private String videoId;

    @SerializedName("comment")
    private String comment;

    public CommentRequest(String videoId, String comment) {
        this.videoId = videoId;
        this.comment = comment;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
