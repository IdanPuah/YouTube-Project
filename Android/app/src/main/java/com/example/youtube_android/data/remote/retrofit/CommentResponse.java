package com.example.youtube_android.data.remote.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CommentResponse {
    @SerializedName("_id")
    private String id;
    @SerializedName("username")
    private String username;

    @SerializedName("userId")
    private String userId;

    @SerializedName("comment")
    private String comment;

    @SerializedName("videoId")
    private String videoId;

    @SerializedName("date")
    private Date date;

    @SerializedName("userImg")  // Base64-encoded image string
    private String profileImg;

    // Getters and setters
    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
