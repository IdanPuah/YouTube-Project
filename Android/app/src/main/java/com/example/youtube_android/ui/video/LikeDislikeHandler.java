package com.example.youtube_android.ui.video;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube_android.R;
import com.example.youtube_android.data.model.User;
import com.example.youtube_android.data.model.Video;

public class LikeDislikeHandler {
    private final int IS_LIKE = 1;
    private final int IS_NOT_LIKE = 2;
    private int likes;
    private int likeStatus;
    private com.example.youtube_android.data.model.User currentUser;
    private int disLikes;
    private boolean isLiked = false;
    private boolean isDisliked = false;
    private AnimationDrawable likeAnimation;
    private AnimationDrawable dislikeAnimation;
    private final LikeHandleListener likeHandleListener;
//    private final VideoViewModel videoViewModel;
//    private final LifecycleOwner lifecycleOwner;

    public LikeDislikeHandler(Context context, Video video, LikeHandleListener likeHandleListener, int likeStatus, User currentUser) {
        initializeLikeButton(context, video);
        initializeDislikeButton(context, video);
        this.likeHandleListener = likeHandleListener;
        this.likeStatus = likeStatus;
        this.currentUser = currentUser;
//        this.videoViewModel = videoViewModel;
//        this.lifecycleOwner = lifecycleOwner;

    }

    private void initializeLikeButton(Context context, Video video) {
        ImageButton likeButton = ((AppCompatActivity) context).findViewById(R.id.likeButton);
        TextView likesCounter = ((AppCompatActivity) context).findViewById(R.id.likesCounter);
        ImageButton dislikeButton = ((AppCompatActivity) context).findViewById(R.id.dislikeButton);

        if (video != null) {
            likes = video.getLikes();
            disLikes = 0; // Initialize dislikes if needed

            likesCounter.setText(String.valueOf(likes));
            likeButton.setBackgroundResource(R.drawable.like_button_animation);
            likeAnimation = (AnimationDrawable) likeButton.getBackground();

            // in case like already pressed
            if (likeStatus == IS_LIKE) {
                likeButton.setBackgroundResource(R.drawable.ic_like_green);
                likeAnimation.start();
            }

            likeButton.setOnClickListener(v -> {
                if (currentUser == null){
                    return;
                }
                Log.d("like handler", "like handler: click on like");
                Log.d("like handler", "like handler: like status: " + likeStatus);
                // in case like not pressed
                if (likeStatus == IS_NOT_LIKE) {
                    // set the counter
                    likes++;
                    likesCounter.setText(String.valueOf(likes));

                    // set the animation
                    likeButton.setBackgroundResource(R.drawable.ic_like_green);
                    dislikeButton.setBackgroundResource(R.drawable.ic_dislike);
                    likeAnimation.start();

                    // add like in server
                    likeHandleListener.handleLikeChange("add");

                    likeStatus = IS_LIKE;
                }
            });

        }
    }

    private void initializeDislikeButton(Context context, Video video) {
        ImageButton dislikeButton = ((AppCompatActivity) context).findViewById(R.id.dislikeButton);
        ImageButton likeButton = ((AppCompatActivity) context).findViewById(R.id.likeButton);
        TextView likesCounter = ((AppCompatActivity) context).findViewById(R.id.likesCounter);

        if (video != null) {
            dislikeButton.setBackgroundResource(R.drawable.dislike_button_animation);
            dislikeAnimation = (AnimationDrawable) dislikeButton.getBackground();
            likes = video.getLikes();

            dislikeButton.setOnClickListener(v -> {
                if (currentUser == null){
                    return;
                }
                Log.d("like handler", "like handler: click on un like");
                // in case is like press
                if (likeStatus == IS_LIKE) {
                    // set the counter
                    likes--;
                    likesCounter.setText(String.valueOf(likes));

                    //set animation
                    likeButton.setBackgroundResource(R.drawable.ic_like); // Revert to original like icon
                    dislikeButton.setBackgroundResource(R.drawable.ic_dislike_red); // Change to red immediately
                    dislikeAnimation.start(); // Start the animation

                    likeHandleListener.handleLikeChange("remove");

                    likeStatus = IS_NOT_LIKE;
                }
            });
        }
    }

//    private void initializeLikeButton(Context context, Video video) {
//        ImageButton likeButton = ((AppCompatActivity) context).findViewById(R.id.likeButton);
//        TextView likesCounter = ((AppCompatActivity) context).findViewById(R.id.likesCounter);
//        ImageButton dislikeButton = ((AppCompatActivity) context).findViewById(R.id.dislikeButton);
//
//        if (video != null) {
//            likes = video.getLikes();
//            disLikes = 0; // Initialize dislikes if needed
//
//            likesCounter.setText(String.valueOf(likes));
//            likeButton.setBackgroundResource(R.drawable.like_button_animation);
//            likeAnimation = (AnimationDrawable) likeButton.getBackground();
//
//            likeButton.setOnClickListener(view -> {
//                if (isLiked) {
//                    likes--;
//                    likeButton.setBackgroundResource(R.drawable.ic_like); // Revert to original like icon
//                    isLiked = false;
//                } else {
//                    if (isDisliked) {
//                        disLikes--;
//                        dislikeButton.setBackgroundResource(R.drawable.ic_dislike); // Revert to original dislike icon
//                        isDisliked = false;
//                    }
//                    likes++;
//                    likeButton.setBackgroundResource(R.drawable.ic_like_green); // Change to green immediately
//                    likeAnimation.start(); // Start the animation
//                    isLiked = true;
//                }
//                likesCounter.setText(String.valueOf(likes));
//
//            });
//        } else {
//            Log.e("LikeDislikeHandler", "Video object is null");
//        }
//
//    }
//
//    private void initializeDislikeButton(Context context, Video video) {
//        ImageButton dislikeButton = ((AppCompatActivity) context).findViewById(R.id.dislikeButton);
//        ImageButton likeButton = ((AppCompatActivity) context).findViewById(R.id.likeButton);
//        TextView likesCounter = ((AppCompatActivity) context).findViewById(R.id.likesCounter);
//
//        dislikeButton.setBackgroundResource(R.drawable.dislike_button_animation);
//        dislikeAnimation = (AnimationDrawable) dislikeButton.getBackground();
//
//        dislikeButton.setOnClickListener(view -> {
//            if (isDisliked) {
//                disLikes--;
//                dislikeButton.setBackgroundResource(R.drawable.ic_dislike); // Revert to original dislike icon
//                isDisliked = false;
//            } else {
//                if (isLiked) {
//                    likes--;
//                    likeButton.setBackgroundResource(R.drawable.ic_like); // Revert to original like icon
//                    likesCounter.setText(String.valueOf(likes)); // Update likes counter
//                    isLiked = false;
//                }
//                disLikes++;
//                dislikeButton.setBackgroundResource(R.drawable.ic_dislike_red); // Change to red immediately
//                dislikeAnimation.start(); // Start the animation
//                isDisliked = true;
//            }
//
//        });
//    }

    public interface LikeHandleListener {
        public void handleLikeChange(String action);
    }
}
