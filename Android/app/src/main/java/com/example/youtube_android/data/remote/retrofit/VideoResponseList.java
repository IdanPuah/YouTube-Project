package com.example.youtube_android.data.remote.retrofit;

import java.util.List;

public class VideoResponseList {
    private List<VideoResponse> videos;

    public List<VideoResponse> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoResponse> videos) {
        this.videos = videos;
    }
}
