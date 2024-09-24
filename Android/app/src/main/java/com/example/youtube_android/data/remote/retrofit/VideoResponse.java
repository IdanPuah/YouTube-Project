package com.example.youtube_android.data.remote.retrofit;

import android.net.Uri;

import com.example.youtube_android.data.model.Video;
import com.google.gson.annotations.SerializedName;

public class VideoResponse {
    @SerializedName("_id")
    private String _id;

    @SerializedName("title")
    private String title;

    @SerializedName("creatorId")
    private String creatorId;

    @SerializedName("username")
    private String username;

    @SerializedName("category")
    private String category;
    @SerializedName("description")
    private String description;

    @SerializedName("thumbnail")
    private String thumbnail;  // Assuming this is the base64 encoded string

    @SerializedName("creatorImg")
    private String creatorImg;
    @SerializedName("videoSrc")
    private String videoSrc;

    @SerializedName("views")
    private int views;

    @SerializedName("likes")
    private int likes;

    public Video toVideo() {
        Video video = new Video();
        video.set_id(_id);
        video.setTitle(title);
        video.setCreator(username);
        video.setCategory(category);
        video.setUriImg(Uri.parse(thumbnail)); // Assuming this is a URL
//        video.setViews(views);
        video.setLikes(likes);
//        video.setDate(date);
        return video;
    }

    public Video toVideoPage(){
        Video video = new Video();
        video.set_id(_id);
        video.setTitle(title);
        video.setCreatorImg(Uri.parse(creatorImg));
        video.setCreator(username);
        video.setCategory(category);
        video.setVideoSrcString(videoSrc); // Assuming this is a URL
        video.setViews(views);
        video.setLikes(likes);
        video.setDescription(description);
//        video.setDate(date);
        return video;
    }
}
