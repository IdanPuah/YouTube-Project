package com.example.youtube_android.data.remote.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentResponseList {
    @SerializedName("comments")
    private List<CommentResponse> comments;

    public List<CommentResponse> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponse> comments) {
        this.comments = comments;
    }
}
