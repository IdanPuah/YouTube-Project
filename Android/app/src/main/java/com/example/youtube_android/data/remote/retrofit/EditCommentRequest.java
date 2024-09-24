package com.example.youtube_android.data.remote.retrofit;

import com.google.gson.annotations.SerializedName;

public class EditCommentRequest {
    @SerializedName("commentId")
    private String commentId;

    @SerializedName("newCommentText")
    private String newCommentText;

    public EditCommentRequest(String videoId, String comment) {
        this.commentId = videoId;
        this.newCommentText = comment;
    }

    public String getVideoId() {
        return commentId;
    }

    public void setVideoId(String videoId) {
        this.commentId = videoId;
    }

    public String getComment() {
        return newCommentText;
    }

    public void setComment(String comment) {
        this.newCommentText = comment;
    }
}
