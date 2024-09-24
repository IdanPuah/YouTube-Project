package com.example.youtube_android.data.model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.youtube_android.data.local.dao.VideoDao;
import com.example.youtube_android.data.local.database.LocalAppDB;
import com.example.youtube_android.data.remote.retrofit.VideoAPI;
import com.example.youtube_android.data.remote.retrofit.VideoRequest;
import com.example.youtube_android.data.repository.VideoRepository;
import com.example.youtube_android.util.LoggedManager;
import com.example.youtube_android.util.MyApplication;

import java.util.List;

public class VideoViewModel extends ViewModel {
    private LiveData<List<Video>> videos;

    private VideoRepository videoRepository;
    private LoggedManager loggedManager;
    private MyApplication myApplication;



    public VideoViewModel() {
        Context applicationContext = MyApplication.getAppContext();
        VideoAPI videoAPI = new VideoAPI(applicationContext);
        VideoDao videoDao = LocalAppDB.getInstance(MyApplication.getAppContext()).videoDao();
        this.myApplication = new MyApplication();
        this.loggedManager = LoggedManager.getInstance();

        videoRepository = new VideoRepository(videoAPI, videoDao);

        videos = videoRepository.getTwentyVideos();
    }

    public LiveData<List<Video>> getVideos() {
        return videos;
    }

    public LiveData<List<Video>> getTwentyVideos() {
        videos = videoRepository.getTwentyVideos();
        return videos;}

    public LiveData<List<Video>> addVideo(VideoRequest videoToAddRequest){
        return videoRepository.addVideo(videoToAddRequest, loggedManager);
    }

    public LiveData<Video> getVideo(String _id){
        return  videoRepository.getVideo(_id, loggedManager);
    }
    public LiveData<List<Video>> getSearchVideos(String query){return videoRepository.getSearchVideos(query);}

    public LiveData<List<Video>> deleteVideo(Video video){return  videoRepository.deleteVideo(video, loggedManager);}

    public LiveData<List<Video>> editVideo(String id,int position, VideoRequest videoRequest){
        return videoRepository.editVideo(id, position, videoRequest, loggedManager);
    }
    public LiveData<List<Video>> getUserVideos(){return videoRepository.getUserVideos(loggedManager);}

    public LiveData<List<Comment>> getVideoComments(String videoId){return videoRepository.getVideoComments(videoId);}
    public LiveData<Boolean> addComment(String videoId, Comment comment){return videoRepository.addComment(videoId, comment, loggedManager);}
    public LiveData<Boolean> deleteComment(String commentId){return videoRepository.deleteComment(commentId, loggedManager);}

    public LiveData<Boolean> editComment(String commentId, String commentText){return videoRepository.editComment(commentId, commentText, loggedManager);}

    public void handleLikeChange(String videoId, String action){videoRepository.handleLikeChange(videoId, action, loggedManager);}

    public LiveData<Integer> getLikeStatus(String videoId){return videoRepository.getLikeStatus(videoId, loggedManager);}

    public LiveData<List<Video>> getRelatedVideos(String videoId) { return videoRepository.getRelatedVideos(videoId, loggedManager);}
}
