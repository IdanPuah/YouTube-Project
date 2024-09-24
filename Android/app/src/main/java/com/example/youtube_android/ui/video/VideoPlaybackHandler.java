package com.example.youtube_android.ui.video;

import android.content.Context;
import android.graphics.Outline;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube_android.R;
import com.example.youtube_android.data.model.Video;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VideoPlaybackHandler {

    private VideoView videoView;
    private Uri videoUri;
    private int currentVideoPosition = 0; // To save the current position of the video

    public VideoPlaybackHandler(Context context, Video video) {
        videoView = ((AppCompatActivity) context).findViewById(R.id.videoView);
        initializeVideoView(video);
    }

    private void initializeVideoView(Video video) {
        if (video != null) {
            String base64VideoSrc = video.getVideoSrcString(); // Base64 string from server
            byte[] videoData = decodeBase64(base64VideoSrc);

            File videoFile = null;
            try {
                videoFile = saveVideoToFile(videoData, videoView.getContext()); // Save to file
            } catch (IOException e) {
                Log.e("VideoPlaybackHandler", "Error saving video file: " + e.getMessage());
                return; // Exit early if file saving fails
            }

            if (videoFile != null) {
                videoUri = Uri.fromFile(videoFile);
                Log.d("VideoPlaybackHandler", "Video URI: " + videoUri);

                if (videoUri != null) {
                    videoView.setVideoURI(videoUri);

                    MediaController mediaController = new MediaController(videoView.getContext());
                    mediaController.setAnchorView(videoView);
                    videoView.setMediaController(mediaController);

                    videoView.setOnPreparedListener(mp -> {
                        Log.d("VideoPlaybackHandler", "Video prepared, starting playback");
                        videoView.start(); // Start playback when video is prepared
                    });

                    videoView.setOnErrorListener((mp, what, extra) -> {
                        Log.e("VideoPlaybackHandler", "Error during playback: " + what + ", " + extra);
                        return false; // Return true if you handle the error
                    });

                    // Apply custom outline provider and elevation
                    videoView.setOutlineProvider(new CustomOutlineProvider());
                    videoView.setClipToOutline(true);
                    videoView.setElevation(10f);
                } else {
                    Log.e("VideoPlaybackHandler", "Video URI is null");
                }
            } else {
                Log.e("VideoPlaybackHandler", "Video object is null");
            }
        }
    }

    private byte[] decodeBase64(String base64String) {
        // Check if the string starts with the data URL prefix
        String base64Data = base64String;
        final String prefix = "data:video/mp4;base64,";
        final String prefixForEmulator = "data:video/*;base64,";

        if (base64String.startsWith(prefix)) {
            // Remove the prefix to get the actual Base64 data
            base64Data = base64String.substring(prefix.length());
        } else if (base64String.startsWith(prefixForEmulator)){
            base64Data = base64String.substring(prefixForEmulator.length());
        }


        // Now decode the cleaned Base64 string
        try {
            return Base64.decode(base64Data, Base64.DEFAULT);
        } catch (IllegalArgumentException e) {
            Log.e("VideoPlaybackHandler", "Invalid Base64 string", e);
            return null;
        }
    }



    private File saveVideoToFile(byte[] videoData, Context context) throws IOException {
        File videoFile = new File(context.getCacheDir(), "video.mp4");
        try (FileOutputStream fos = new FileOutputStream(videoFile)) {
            fos.write(videoData);
        }
        return videoFile;
    }


    public void onStart() {
        if (videoView != null) {
            videoView.seekTo(currentVideoPosition); // Seek to the saved position when starting
        }
    }

    public void onResume() {
        if (videoView != null) {
            videoView.seekTo(currentVideoPosition); // Ensure the video seeks to the correct position
            videoView.start(); // Start the video from the saved position
        }
    }

    public void onPause() {
        if (videoView != null) {
            currentVideoPosition = videoView.getCurrentPosition(); // Save current position
            videoView.pause(); // Pause playback in onPause
        }
    }

    public void onStop() {
        if (videoView != null) {
            currentVideoPosition = videoView.getCurrentPosition(); // Save current position again in onStop
            videoView.pause(); // Pause playback in onStop
        }
    }

    public void onDestroy() {
        if (videoView != null) {
            videoView.stopPlayback(); // Stop playback and release resources in onDestroy
        }
    }

    public static class CustomOutlineProvider extends ViewOutlineProvider {
        @Override
        public void getOutline(View view, Outline outline) {
            int margin = 0;
            outline.setRoundRect(margin, margin, view.getWidth() - margin, view.getHeight() - margin, 5);
        }
    }
}
