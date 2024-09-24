package com.example.youtube_android.ui.comments;

import com.example.youtube_android.data.model.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentManager {
    private static final Map<Integer, List<Comment>> videoComments = new HashMap<>();

    public static void addComment(int videoId, Comment comment) {
        List<Comment> comments = videoComments.get(videoId);
        if (comments == null) {
            comments = new ArrayList<>();
            videoComments.put(videoId, comments);
        }
        comments.add(0, comment);
    }

    public static List<Comment> getComments(int videoId) {
        return videoComments.getOrDefault(videoId, new ArrayList<>());
    }
}