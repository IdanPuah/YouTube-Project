package com.example.youtube_android.data.remote.retrofit;

import com.google.gson.annotations.SerializedName;

public class DeleteCommentRequest {
    @SerializedName("commentId")
    private String commentId;

    public DeleteCommentRequest(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
