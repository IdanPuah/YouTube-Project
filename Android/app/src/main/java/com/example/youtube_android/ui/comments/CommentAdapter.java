package com.example.youtube_android.ui.comments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.example.youtube_android.R;
import com.example.youtube_android.data.model.Comment;
import com.example.youtube_android.data.model.User;
import com.example.youtube_android.data.model.VideoViewModel;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private User currentUser;
    private VideoViewModel videoViewModel;
    private LifecycleOwner lifecycleOwner;

    public CommentAdapter(Context context, List<Comment> comments, User currentUser, VideoViewModel videoViewModel, LifecycleOwner lifecycleOwner) {
        super(context, 0, comments);
        this.currentUser = currentUser;
        this.videoViewModel = videoViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);
        }

        Comment comment = getItem(position);
        TextView commentTextView = convertView.findViewById(R.id.commentText);
        TextView authorTextView = convertView.findViewById(R.id.authorName);
        ImageView profilePicImageView = convertView.findViewById(R.id.profilePic);
        ImageButton editCommentButton = convertView.findViewById(R.id.editCommentButton);
        ImageButton deleteCommentButton = convertView.findViewById(R.id.deleteCommentButton);

        if (comment != null) {
            commentTextView.setText(comment.getText());
            authorTextView.setText(comment.getAuthor().getUsername());
//            loadProfileImage(profilePicImageView, comment.getAuthor());

            // Load profile image using Glide or Picasso
            if (comment.getProfileImgString() != null) {
                // Decode base64 to Bitmap
                byte[] decodedString = Base64.decode(comment.getProfileImgString().substring(comment.getProfileImgString().indexOf(",") + 1), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                // Use Glide to load the Bitmap into ImageView
                Glide.with(getContext())
                        .load(decodedByte) // Load Bitmap directly
                        .placeholder(R.drawable.ic_profile_default) // Placeholder image
                        .error(R.drawable.ic_profile_default) // Error image
                        .into(profilePicImageView);
            } else {
                profilePicImageView.setImageResource(R.drawable.ic_profile_default); // Default image
            }

            if (currentUser != null && comment.getAuthor().getUsername().equals(currentUser.getUsername())) {
                editCommentButton.setVisibility(View.VISIBLE);
                deleteCommentButton.setVisibility(View.VISIBLE);

//                editCommentButton.setOnClickListener(v -> {
//                    if (listener != null) {
//                        listener.onEditComment(comment);
//                    }
//                });
                editCommentButton.setOnClickListener(v -> {
                    // in case the user not write the comment
                    if (currentUser == null || !comment.getAuthor().getId().equals(currentUser.getId())){
                        return;
                    }

                    EditCommentDialogFragment dialog = new EditCommentDialogFragment(
                            comment, // Pass the entire Comment object
                            updatedComment -> {
                                // Handle the updated comment here
                                if (listener != null) {
                                    listener.onEditComment(updatedComment); // Pass the updated Comment
                                }
                            }
                    );
                    dialog.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "EditCommentDialog");
                });

                deleteCommentButton.setOnClickListener(v -> {
                    // in case the user not write the comment
                    if (currentUser == null || !comment.getAuthor().getId().equals(currentUser.getId())){
                        return;
                    }

                    if (listener != null) {
                        listener.onDeleteComment(comment);
                    }
                });
            } else {
                editCommentButton.setVisibility(View.GONE);
                deleteCommentButton.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    public void updateComments(List<Comment> newComments) {
        clear();
        addAll(newComments);
        notifyDataSetChanged();
    }


    // Define a listener interface
    public interface CommentActionListener {
        void onEditComment(Comment comment);
        void onDeleteComment(Comment comment);
    }

    // Add a listener field and setter
    private CommentActionListener listener;

    public void setCommentActionListener(CommentActionListener listener) {
        this.listener = listener;
    }

    private void loadProfileImage(ImageView profilePicImageView, User author) {
        if (author.getPhoto() != null) {
            Bitmap bitmap = author.getPhoto().getBitmap();
            if (bitmap != null) {
                profilePicImageView.setImageBitmap(bitmap);
            } else {
                // Fallback to default image if decoding fails
                profilePicImageView.setImageResource(R.drawable.ic_profile_default);
            }
        } else {
            profilePicImageView.setImageResource(R.drawable.ic_profile_default);
        }
    }
}
