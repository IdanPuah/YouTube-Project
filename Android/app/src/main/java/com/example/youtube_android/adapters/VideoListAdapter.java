package com.example.youtube_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.youtube_android.R;
import com.example.youtube_android.data.model.Video;
import com.example.youtube_android.data.model.VideoViewModel;
import com.example.youtube_android.data.remote.retrofit.VideoRequest;
import com.example.youtube_android.ui.video.VideoActivity;
import com.example.youtube_android.util.LoggedManager;

import java.util.List;


public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvCreator;
        private final ImageView ivImg;
        public ImageView threeDotsMenu;

        public static final int REQUEST_VIDEO_ACTIVITY = 2;

        private VideoViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCreator = itemView.findViewById(R.id.tvCreator);
            ivImg = itemView.findViewById(R.id.ivImg);
            threeDotsMenu = itemView.findViewById(R.id.three_dots_menu);
        }
    }

    private final LayoutInflater mInflater;
    private List<Video> videos;
    private Context context;
    //    private VideosList videoList;
    private VideoViewModel videoViewModel;
    private LifecycleOwner lifecycleOwner;

    public VideoListAdapter(Context context, VideoViewModel videoViewModel, LifecycleOwner lifecycleOwner) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
//        this.videoList = VideosList.getInstance();
        this.videoViewModel = videoViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.video_layout, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        if (videos != null) {
            final Video current = videos.get(position);
            holder.tvTitle.setText(current.getTitle());
            holder.tvCreator.setText(current.getCreator());

            Log.d("VideoAdapter", "Image URL: " + current.getUriImg());

            Glide.with(holder.itemView.getContext())
                    .load(current.getUriImg())
                    .override(Target.SIZE_ORIGINAL, 200)
                    .into(holder.ivImg);

            holder.threeDotsMenu.setOnClickListener(view -> showPopupMenu(view, position));

            holder.ivImg.setOnClickListener(v -> {

                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("EXTRA_VIDEO", current.get_id());
                if (context instanceof AppCompatActivity) {
                    ((AppCompatActivity) context).startActivityForResult(intent, VideoViewHolder.REQUEST_VIDEO_ACTIVITY);
                } else {
                    context.startActivity(intent);
                }
            });
        }
    }

    public void setVideos(List<Video> v) {
        videos = v;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (videos != null) {
            return videos.size();
        } else {
            return 0;
        }
    }

    public List<Video> getVideos() {
        return videos;
    }

    private void showPopupMenu(View view, int position) {
        LoggedManager loggedManager = LoggedManager.getInstance();
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.popup_menu_video, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.delete_option) {

                // check if the creator of the video is the current user
                if (loggedManager.getIsLogged() && loggedManager.getCurrentUser().getUsername().equals(videos.get(position).getCreator())){
                    videoViewModel.deleteVideo(videos.get(position)).observe(lifecycleOwner, videoList -> {
                        videos = videoList;
                        setVideos(videos);
                    });
                    return true;
                }else{
                    Toast.makeText(context,"Just the creator can delete the video",Toast.LENGTH_SHORT).show();
                }

            }
            if (item.getItemId() == R.id.editVideo) {
                if (loggedManager.getIsLogged() && loggedManager.getCurrentUser().getUsername().equals(videos.get(position).getCreator())) {
                    Toast.makeText(context, "edit video", Toast.LENGTH_SHORT).show();
                    editVideoShow(position);
//                    setVideos(videoList.getVideosList());

//                notifyDataSetChanged();
                    return true;
                } else {
                    Toast.makeText(context, "Just the creator can edit the video", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });
        popupMenu.show();
    }

    private void editVideoShow(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = mInflater.inflate(R.layout.dialog_edit_video, null);
        builder.setView(dialogView);

        EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        editTextTitle.setText(videos.get(position).getTitle());

        EditText editTextCategory = dialogView.findViewById(R.id.editTextCategory);
        editTextCategory.setText(videos.get(position).getCategory());

        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        editTextDescription.setText(videos.get(position).getDescription());


        // Set other fields as needed

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Handle save action
            String updatedTitle = editTextTitle.getText().toString();
            String updatedCategory = editTextCategory.getText().toString();
            String updatedDescription = editTextDescription.getText().toString();

            Log.d("adapter", "adapter img1: " + videos.get(position).getUriImg());
            Log.d("adapter", "Creating thumbnail with URI: " + videos.get(position).getUriImg().toString());

            VideoRequest videoToUpdate = new VideoRequest(updatedTitle, updatedDescription, updatedCategory,
                    videos.get(position).getUriImg());
            Log.d("adapter", "adapter img2: " + videoToUpdate.getThumbnailUrl());

            videoViewModel.editVideo(videos.get(position).get_id(), position, videoToUpdate).observe(lifecycleOwner, videosList -> {
                if (videosList != null){
                    videos = videosList;
                    setVideos(videos);
                }
                else {
                    Toast.makeText(context, "Just the creator can edit the video", Toast.LENGTH_SHORT).show();
                }
            });

//            videos.get(position).setTitle(updatedTitle);
//            videos.get(position).setCategory(updatedCategory);
//            videos.get(position).setDescription(updatedDescription);


            // Notify adapter of the change
            notifyDataSetChanged();

            // Dismiss dialog or perform any other actions
            dialog.dismiss();

            // Update the video details as needed
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Handle cancel action or dismiss dialog
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
