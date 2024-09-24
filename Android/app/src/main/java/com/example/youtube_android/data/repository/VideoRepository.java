package com.example.youtube_android.data.repository;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.youtube_android.data.local.dao.VideoDao;
import com.example.youtube_android.data.model.Comment;
import com.example.youtube_android.data.model.User;
import com.example.youtube_android.data.model.Video;
import com.example.youtube_android.data.remote.retrofit.CommentResponse;
import com.example.youtube_android.data.remote.retrofit.VideoAPI;
import com.example.youtube_android.data.remote.retrofit.VideoRequest;
import com.example.youtube_android.data.remote.retrofit.VideoResponse;
import com.example.youtube_android.util.LoggedManager;
import com.example.youtube_android.util.MyApplication;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

public class VideoRepository {
    private VideoDao videoDao;
    private VideoListData videoListData;
    private VideoAPI videoAPI;

    private MutableLiveData<List<VideoResponse>> videoResponse;
    Context applicationContext = MyApplication.getAppContext();

    public VideoRepository(VideoAPI videoAPI, VideoDao videoDao) {
        videoResponse = new MutableLiveData<>();
        this.videoDao = videoDao;
        videoListData = new VideoListData();
        this.videoAPI = videoAPI;
    }

    class VideoListData extends MutableLiveData<List<Video>> {
        public VideoListData() {
            super();
            loadData();
        }

        private void loadData() {
            new Thread(() -> {
                List<Video> videos = videoDao.getTwentyVideos();
                if (videos != null) {
                    postValue(videos);
                } else {
                    postValue(new ArrayList<>());
                }
            }).start();
        }

        @Override
        protected void onActive() {
            super.onActive();
            videoAPI.getTwentyVideos(videoResponse);

            // Observe changes in videoListData and update the local database
            videoResponse.observeForever(responseVideos -> {

                if (responseVideos != null) {

                    // Map VideoResponse to Video using toVideo method
                    List<Video> videos = new ArrayList<>();
                    for (VideoResponse videoResponse : responseVideos) {
                        videos.add(videoResponse.toVideo());
                    }

                    Log.d("VideoRepository", "Number of videos received: " + videos.size());

                    new Thread(() -> {
                        try {
                            // just for now. need to pass videos in videoDao.insertIfNotExists(tempList)
                            List<Video> tempList = new ArrayList<>();
                            tempList.add(videos.get(0));
                            // just for now
                            videoDao.insertIfNotExists(tempList);
                            videoListData.postValue(videos);
                        } catch (Exception e) {
                            e.printStackTrace(); // Handle exception
                        }
                    }).start();
                }

            });
        }
    }

    public LiveData<List<Video>> getTwentyVideos() {
        videoAPI.getTwentyVideos(videoResponse);
        return videoListData;
    }

    public LiveData<List<Video>> addVideo(VideoRequest videoToAddRequest, LoggedManager currentUser) {
        MutableLiveData<VideoResponse> isAddedResponse = new MutableLiveData<>();
        videoAPI.addVideo(currentUser.getToken(), isAddedResponse, videoToAddRequest);

        isAddedResponse.observeForever(isAdd -> {
            if (isAdd != null) {
                new Thread(() -> {
                    // Access the current list in videoListData
                    List<Video> currentList = videoListData.getValue();
                    if (currentList == null) {
                        currentList = new ArrayList<>();
                    }

                    // Add the new video to the list
                    currentList.add(isAdd.toVideo());

                    // Update the videoListData with the new list
                    videoListData.postValue(currentList);

                    // Optionally, insert the new video into the database
                    videoDao.insertIfNotExists(currentList);

                }).start();
            } else {
                Toast.makeText(applicationContext, "Adding video failed, please try again", Toast.LENGTH_SHORT).show();
            }
        });
        return videoListData;
    }

    public LiveData<Video> getVideo(String _id, LoggedManager loggedManager) {
        MutableLiveData<VideoResponse> isGetResponse = new MutableLiveData<>();
        String userId = "null";
        if (loggedManager.getCurrentUser() != null){
            userId = loggedManager.getCurrentUser().getId();
        }
        videoAPI.getVideo(_id,userId, isGetResponse);

        MutableLiveData<Video> gotVideo = new MutableLiveData<>();

        isGetResponse.observeForever(isGet -> {
            new Thread(() -> {
                if (isGet != null) {
                    gotVideo.postValue(isGet.toVideoPage());
                } else {
                    gotVideo.postValue(null);
                }
            }).start();
        });

        return gotVideo;
    }

    public LiveData<List<Video>> getSearchVideos(String query) {
        MutableLiveData<List<VideoResponse>> searchVideosResponse = new MutableLiveData<>();
        videoAPI.getSearchVideos(query, searchVideosResponse);

        MutableLiveData<List<Video>> searchVideos = new MutableLiveData<>();

        searchVideosResponse.observeForever(videoResponses -> {
            new Thread(() -> {
                if (videoResponses != null) {
                    List<Video> tempList = new ArrayList<>();
                    for (VideoResponse videoResponse : videoResponses) {
                        tempList.add(videoResponse.toVideo());
                    }

                    searchVideos.postValue(tempList);
                }
            }).start();
        });

        return searchVideos;
    }

    public LiveData<List<Video>> deleteVideo(Video videoToDelete, LoggedManager loggedManager) {
        MutableLiveData<VideoResponse> videoToDeleteResponse = new MutableLiveData<>();
        videoAPI.deleteVideo(loggedManager.getToken(), videoToDelete, loggedManager.getCurrentUser().getId(), videoToDeleteResponse);

        MutableLiveData<Boolean> isDeleted = new MutableLiveData<>();

        videoToDeleteResponse.observeForever(video -> {
            if (video != null) {
                new Thread(() -> {
                    // Access the current list in videoListData
                    List<Video> currentList = videoListData.getValue();
                    if (currentList == null) {
                        currentList = new ArrayList<>();
                    }

                    // Add the new video to the list
                    currentList.remove(video.toVideo());

                    // Update the videoListData with the new list
                    videoListData.postValue(currentList);

                    // Optionally, insert the new video into the database
                    videoDao.delete(video.toVideo());

                    // update boolean value to return to client
                    isDeleted.postValue(true);

                }).start();
            } else {
                isDeleted.postValue(false);
            }
        });

        return videoListData;
    }

    public LiveData<List<Video>> editVideo(String videoId, int position, VideoRequest videoRequest, LoggedManager loggedManager) {
        MutableLiveData<VideoResponse> videoToEditResponse = new MutableLiveData<>();
        videoAPI.editVideo(loggedManager.getToken(), videoRequest, videoId, videoToEditResponse, loggedManager.getCurrentUser().getId());

//        MutableLiveData<Boolean> isEdit = new MutableLiveData<>();

        videoToEditResponse.observeForever(video -> {
            if (video != null) {
                new Thread(() -> {
                    // Access the current list in videoListData
                    List<Video> currentList = videoListData.getValue();
                    if (currentList == null) {
                        currentList = new ArrayList<>();
                    }

                    Video editVideo =  video.toVideo();
                    currentList.get(position).setTitle(editVideo.getTitle());
                    currentList.get(position).setCategory(editVideo.getCategory());
                    currentList.get(position).setDescription(editVideo.getDescription());

                    // Update the videoListData with the new list
                    videoListData.postValue(currentList);

                    // Optionally, insert the new video into the database
//                    videoDao.insertIfNotExists(currentList);
                    videoDao.update(editVideo);

                }).start();

            }
        });
        return videoListData;
    }

    public LiveData<List<Video>> getUserVideos(LoggedManager loggedManager){
        MutableLiveData<List<VideoResponse>> userVideoResponse = new MutableLiveData<>();
        videoAPI.getUserVideos(loggedManager.getCurrentUser().getId(), userVideoResponse);

        userVideoResponse.observeForever(videos -> {
            if (videos != null) {
                new Thread(() -> {
                    List<Video> userVideos = new LinkedList<>();
                    for (VideoResponse videoResponse : videos) {
                        userVideos.add(videoResponse.toVideo());
                    }

                    videoListData.postValue(userVideos);

                }).start();

            }
        });
        return videoListData;
    }
    public LiveData<List<Comment>> getVideoComments(String videoId) {
        MutableLiveData<List<CommentResponse>> commentsResponse = new MutableLiveData<>();
        MutableLiveData<List<Comment>> comments = new MutableLiveData<>();

        videoAPI.getVideoComments(videoId, commentsResponse);

        // Observe commentsResponse and process the data
        commentsResponse.observeForever(new Observer<List<CommentResponse>>() {
            @Override
            public void onChanged(List<CommentResponse> commentResponses) {
                if (commentResponses != null) {
                    // Process comments in a background thread
                    Executors.newSingleThreadExecutor().execute(() -> {
                        List<Comment> tempComments = new ArrayList<>();

                        for (CommentResponse responseItem : commentResponses) {
                            // Create User object if needed
                            User user = new User();
                            user.setId(responseItem.getUserId());
                            user.setUsername(responseItem.getUsername());

                            // Convert userImg string to Uri
                            Uri userImgUri = responseItem.getProfileImg() != null ? Uri.parse(responseItem.getProfileImg()) : null;
                            Log.d("video repository", "repository img: "+ userImgUri);

                            // Create Comment object
                            Log.d("video repository", "repository comment id: "+ responseItem.getId());
                            Log.d("video repository", "repository video id: "+ responseItem.getVideoId());
                            Comment comment = new Comment(responseItem.getId(), user, responseItem.getComment(), responseItem.getVideoId(), responseItem.getDate(), userImgUri, responseItem.getProfileImg());
                            tempComments.add(comment);
                        }

                        // Post processed comments to LiveData
                        comments.postValue(tempComments);
                    });
                }
            }
        });

        return comments;
    }

    public LiveData<Boolean> addComment(String videoId, Comment comment, LoggedManager loggedManager){
        MutableLiveData<Boolean> isCommentAdded = new MutableLiveData<>();
        videoAPI.addComment(loggedManager.getToken(), videoId, comment, isCommentAdded);

        return isCommentAdded;
    }

    public LiveData<Boolean> deleteComment(String commentId, LoggedManager loggedManager){
        MutableLiveData<Boolean> isCommentDelete = new MutableLiveData<>();
        videoAPI.deleteComment(loggedManager.getToken(), commentId, isCommentDelete);

        return isCommentDelete;
    }

    public LiveData<Boolean> editComment(String commentId, String commentText, LoggedManager loggedManager){
        MutableLiveData<Boolean> isCommentEdit = new MutableLiveData<>();
        videoAPI.editComment(loggedManager.getToken(), commentId, commentText, isCommentEdit);

        return isCommentEdit;
    }

    public void handleLikeChange(String videoId, String action, LoggedManager loggedManager){
        videoAPI.handleLikeChange(loggedManager.getToken(), videoId, action);
    }

    public LiveData<Integer> getLikeStatus(String videoId, LoggedManager loggedManager){
        MutableLiveData<Integer> likeStatus = new MutableLiveData<>();
        videoAPI.getLikeStatus(loggedManager.getToken(), videoId, likeStatus);

        return likeStatus;
    }

    public LiveData<List<Video>> getRelatedVideos(String videoId, LoggedManager loggedManager) {
        MutableLiveData<List<VideoResponse>> relatedVideosResponse = new MutableLiveData<>();
        String userId = "null";
        Log.d("video repository: ","logged manager" + loggedManager.getCurrentUser());

        if (loggedManager.getCurrentUser() != null) {
            Log.d("video repository: ","logged manager id" + loggedManager.getCurrentUser().getId());
            userId = loggedManager.getCurrentUser().getId();
        }

        // Fetch the related videos from the API
        videoAPI.getRelatedVideos(videoId, userId, relatedVideosResponse);

        MutableLiveData<List<Video>> relatedVideos = new MutableLiveData<>();

        // Observe relatedVideosResponse
        relatedVideosResponse.observeForever(videoResponses -> {
            if (videoResponses != null) {
                List<Video> tempList = new ArrayList<>();
                for (VideoResponse videoResponse : videoResponses) {
                    tempList.add(videoResponse.toVideo());
                }
                relatedVideos.postValue(tempList);
            } else {
                relatedVideos.postValue(new ArrayList<>()); // Empty list if null
            }
        });

        return relatedVideos;
    }



//    public LiveData<List<Comment>> getVideoComments(String videoId){
//        MutableLiveData<List<CommentResponse>> commentsResponse = new MutableLiveData<>();
//        MutableLiveData<List<Comment>> comments = new MutableLiveData<>();
//
//        videoAPI.getVideoComments(videoId, commentsResponse);
//
//        commentsResponse.observeForever(commentResponses -> {
//            if (commentResponses != null){
//                new Thread(() -> {
//                    List<Comment> tempComments = new ArrayList<>();
//
//                    for (CommentResponse responseItem : commentResponses) {
//                        // Create User object if needed
//                        User user = new User();
//                        user.setId(responseItem.getUserId());
//                        user.setUsername(responseItem.getUsername());
//
//                        // Convert userImg string to Uri
//                        Uri userImgUri = responseItem.getProfileImg() != null ? Uri.parse(responseItem.getProfileImg()) : null;
//
//                        // Create Comment object
//                        Comment comment = new Comment(user, responseItem.getComment(), responseItem.getVideoId(), responseItem.getDate(), userImgUri);
//                        tempComments.add(comment);
//                    }
//
//                    comments.postValue(tempComments);
//                }).start();
//            }
//        });
//        return comments;
//    }
}
