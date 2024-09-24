package com.example.youtube_android.ui.comments;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.example.youtube_android.R;
import com.example.youtube_android.data.model.Comment;
import com.example.youtube_android.data.model.User;
import com.example.youtube_android.data.model.Video;
import com.example.youtube_android.data.model.VideoViewModel;

import java.util.List;

public class CommentSectionHandler {

    private List<Comment> comments;
    private CommentAdapter commentAdapter;
    private User currentUser;
    private String videoId;
    VideoViewModel videoViewModel;
    private LifecycleOwner lifecycle;

    public CommentSectionHandler(Context context, Video video, User currentUser, CommentAdapter.CommentActionListener listener, VideoViewModel videoViewModel, LifecycleOwner lifecycle) {
        this.comments = video.getComments();
        this.currentUser = currentUser;
        initializeCommentSection(context, listener);
        this.videoId = video.get_id();
        this.videoViewModel = videoViewModel;
        this.lifecycle = lifecycle;
    }

    public void initializeCommentSection(Context context, CommentAdapter.CommentActionListener listener) {
        if (context instanceof AppCompatActivity) {
            ListView commentsListView = ((AppCompatActivity) context).findViewById(R.id.commentsListView);
            commentAdapter = new CommentAdapter(context, comments, currentUser, videoViewModel, lifecycle);
            commentsListView.setAdapter(commentAdapter);
            commentAdapter.setCommentActionListener(listener);
        } else {
            throw new IllegalArgumentException("Context must be an instance of AppCompatActivity");
        }
        EditText commentEditText = ((AppCompatActivity) context).findViewById(R.id.commentEditText);
        ImageButton submitCommentButton = ((AppCompatActivity) context).findViewById(R.id.imageButton);

        submitCommentButton.setOnClickListener(view -> {
            if (currentUser != null) {
                String commentText = commentEditText.getText().toString();
                if (!commentText.isEmpty()) {
                    Comment newComment = new Comment(commentText, currentUser, videoId);
                    addComment(newComment);
                    commentEditText.setText("");
                }
            } else {
                showLoginAlertDialog(context);
            }
        });
    }

    private void addComment(Comment newComment) {
        videoViewModel.addComment(videoId, newComment).observe(lifecycle, isCommentAdd ->{
            if (isCommentAdd){
                videoViewModel.getVideoComments(videoId).observe(lifecycle, newComments -> {
                    comments.add(0,newComments.get(0));
                    commentAdapter.notifyDataSetChanged();
                });
//        comments.add(0, newComment);  // Add the comment at the beginning of the list
            }
        });
    }

    private void showLoginAlertDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Login Required")
                .setMessage("You have to be logged in to comment.")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void updateComments(List<Comment> newComments) {
        if (commentAdapter != null) {
            commentAdapter.updateComments(newComments);
        }
    }

}
