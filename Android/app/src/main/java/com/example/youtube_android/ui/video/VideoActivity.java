package com.example.youtube_android.ui.video;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.youtube_android.R;
import com.example.youtube_android.adapters.VideoListAdapter;
import com.example.youtube_android.data.model.Comment;
import com.example.youtube_android.data.model.User;
import com.example.youtube_android.data.model.Video;
import com.example.youtube_android.data.model.VideoViewModel;
import com.example.youtube_android.ui.comments.CommentAdapter;
import com.example.youtube_android.ui.comments.CommentSectionHandler;
import com.example.youtube_android.util.LoggedManager;


public class VideoActivity extends AppCompatActivity implements CommentAdapter.CommentActionListener, LikeDislikeHandler.LikeHandleListener {

    private Video video;
    private User currentUser;
    private boolean isUploader = false;
    private TextView title, creator, category, description, year;
    private ImageView thumbnail;
    private VideoViewModel videoViewModel;
    private VideoListAdapter videoListAdapter;
    private RecyclerView relatedVideosRecyclerView;
    private Button toggleCommentsButton;
    private ListView commentsListView;

    private boolean areCommentsVisible = false;


    private VideoPlaybackHandler videoPlaybackHandler;
    private LikeDislikeHandler likeDislikeHandler;
    private com.example.youtube_android.ui.comments.CommentSectionHandler commentSectionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);

        relatedVideosRecyclerView = findViewById(R.id.relatedVideosRecyclerView);
        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);

        videoListAdapter = new VideoListAdapter(this, videoViewModel, this);
        relatedVideosRecyclerView.setAdapter(videoListAdapter);
        relatedVideosRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        ProgressBar progressBar = findViewById(R.id.loadingSpinner);
        LinearLayout contentLayout = findViewById(R.id.contentLayout);
        commentsListView = findViewById(R.id.commentsListView);


        // Initially show videos and hide comments
        relatedVideosRecyclerView.setVisibility(View.VISIBLE);
        commentsListView.setVisibility(View.GONE);

        // Show the spinner while loading data
        progressBar.setVisibility(View.VISIBLE); // Show loading spinner
        contentLayout.setVisibility(View.GONE);

        adjustWindowInsets();

        // Get the Video object from the Intent
        String videoId = getIntent().getStringExtra("EXTRA_VIDEO");
        videoViewModel.getVideo(videoId).observe(this, video -> {
            if (video != null) {
                Log.d("video activity", "video activity: id: " + video.get_id());
                videoViewModel.getVideoComments(video.get_id()).observe(this, videoComments -> {
                    this.video = video;
                    video.setComments(videoComments); // Set the comments on the video
                    Log.d("video activity", "video activity comments:" + video.getComments().toString());

                    initializeView();
                    toggleCommentsButton.setOnClickListener(view -> toggleCommentsVisibility());

                    videoViewModel.getLikeStatus(video.get_id()).observe(this, likeStatus ->{

//                       this.video = video;
                       initializeView();
                       videoPlaybackHandler = new VideoPlaybackHandler(this, video);
                       likeDislikeHandler = new LikeDislikeHandler(this, video, this, likeStatus, currentUser);
                       commentSectionHandler = new CommentSectionHandler(this, video, currentUser, this, videoViewModel, this);

                       // Now load suggestions videos
                       loadRelatedVideos();

                       progressBar.setVisibility(View.GONE); // Hide loading spinner
                       contentLayout.setVisibility(View.VISIBLE);
                       relatedVideosRecyclerView.setVisibility(View.VISIBLE);

                   });

                });
            } else {
                Log.e("video activity", "video could not be loaded");
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void toggleCommentsVisibility() {
        if (relatedVideosRecyclerView.getVisibility() == View.VISIBLE) {
            // Hide videos and show comments
            relatedVideosRecyclerView.setVisibility(View.GONE);
            commentsListView.setVisibility(View.VISIBLE);
            toggleCommentsButton.setText("Hide Comments");
        } else {
            // Show videos and hide comments
            relatedVideosRecyclerView.setVisibility(View.VISIBLE);
            commentsListView.setVisibility(View.GONE);
            toggleCommentsButton.setText("Show Comments");
        }
    }




    void initializeView() {
        title = findViewById(R.id.videoName);
        creator = findViewById(R.id.userNameTextView);
        category = findViewById(R.id.videoCategory);
        description = findViewById(R.id.videoDescriptionTextView);
        year = findViewById(R.id.videoYear);
        thumbnail = findViewById(R.id.userImageView);
        toggleCommentsButton = findViewById(R.id.toggleCommentsButton);

//        video = getIntent().getParcelableExtra("EXTRA_VIDEO");
//        Log.d("VideoActivity", "Video title: " + video.getTitle());
//        Log.d("VideoActivity", "Comments size: " + video.getComments().size());
        if (video != null) {
            populateViewsWithVideoData();
        }

        // Initialize the current user
        LoggedManager loggedManager = LoggedManager.getInstance();
        currentUser = loggedManager.getCurrentUser();

        if (currentUser == null) {
            loggedManager.setLogged(false);
            isUploader = false;
        } else {
            isUploader = video.getCreator().equals(currentUser.getUsername());
        }
    }

    private void populateViewsWithVideoData() {
        if (title != null) {
            title.setText(video.getTitle());
        }
        if (creator != null) {
            creator.setText(video.getCreator());
        }
        if (category != null) {
            category.setText('#' + video.getCategory());
        }
        if (description != null) {
            description.setText(video.getDescription());
        }
        if (year != null) {
            year.setText(String.valueOf(video.getYear()));
        }
        if (thumbnail != null) {
            Glide.with(this).load(video.getCreatorImg()).into(thumbnail);
        }
    }

    private void adjustWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onEditComment(Comment comment) {
        Log.d("video activity", "edit comment");
        Log.d("video activity", "edit comment id: " + comment.get_id());
        Log.d("video activity", "edit comment text: " + comment.getText());
        videoViewModel.editComment(comment.get_id(), comment.getText()).observe(this, isCommentEdited -> {
            if (isCommentEdited){
                videoViewModel.getVideoComments(video.get_id()).observe(this, comments -> {
                    video.setComments(comments);
                    if (commentSectionHandler != null) {
                        commentSectionHandler.updateComments(comments);
                    }

                });
            }
        });
    }

    @Override
    public void onDeleteComment(Comment comment) {
        Log.d("video activity", "delete comment");
        videoViewModel.deleteComment(comment.get_id()).observe(this, isCommentDeleted -> {
            if (isCommentDeleted){
                videoViewModel.getVideoComments(video.get_id()).observe(this, comments -> {
                    video.setComments(comments);
                    if (commentSectionHandler != null) {
                        commentSectionHandler.updateComments(comments);
                    }

                });
            }
        });
        // Handle delete comment
    }

    @Override
    public void handleLikeChange(String action){
        Log.d("video activity", "handle likes");
        videoViewModel.handleLikeChange(video.get_id(), action);
    }

    private void loadRelatedVideos() {
        // Assuming you have a method in your ViewModel to fetch related videos
        videoViewModel.getRelatedVideos(video.get_id()).observe(this, relatedVideos -> {
            Log.d("videoActivity","related videos: " + relatedVideos);
            if (relatedVideos != null) {
                videoListAdapter.setVideos(relatedVideos); // Update the adapter with the related videos
            } else {
                Log.e("video activity", "related videos could not be loaded");
            }
        });
    }


}