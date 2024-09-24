package com.example.youtube_android.ui.addvideo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.youtube_android.R;
import com.example.youtube_android.data.model.VideoViewModel;
import com.example.youtube_android.util.LoggedManager;
import com.example.youtube_android.data.model.Video;

public class addVideoActivity extends AppCompatActivity {
    private static final int REQUEST_GALLERY = 101;
    private static final int REQUEST_VIDEO = 102;
    private Uri selectedImage = null;
    private Uri selectVideo;
    private VideoViewModel videoViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        ImageView logo = findViewById(R.id.LogoButton);

        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);

        // set logo button
        logo.setOnClickListener(v -> {
            finish();
        });


        Button uploadImageButton = findViewById(R.id.uploadImageButton);
        Button uploadVideo = findViewById(R.id.uploadVideoButton);
        Button addVideoButton = findViewById(R.id.addVideoButton);

        uploadImageButton.setOnClickListener(v -> dispatchTakePictureIntent());

        uploadVideo.setOnClickListener(v -> uploadVideo());

        addVideoButton.setOnClickListener(v -> {
            if (validateInput()) {
                addVideo();
            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    private void uploadVideo() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_VIDEO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                if (data != null && data.getData() != null) {
                    this.selectedImage = data.getData();
                    grantUriPermission(getPackageName(), this.selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }
            if (requestCode == REQUEST_VIDEO) {
                if (data != null && data.getData() != null) {
                    this.selectVideo = data.getData();
                    grantUriPermission(getPackageName(), this.selectVideo, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

            }
        }
    }


    private boolean validateInput() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText creatorEditText = findViewById(R.id.creatorEditText);
        EditText categoryEditText = findViewById(R.id.categoryEditText);
        EditText descriptionEditText = findViewById(R.id.descriptionEditText);
        EditText yearEditText = findViewById(R.id.yearEditText);

        String title = titleEditText.getText().toString();
        String creator = creatorEditText.getText().toString();
        String category = categoryEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String year = yearEditText.getText().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please fill title", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (creator.isEmpty()) {
            Toast.makeText(this, "Please fill creator", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (category.isEmpty()) {
            Toast.makeText(this, "Please fill category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.isEmpty()) {
            Toast.makeText(this, "Please fill description", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (year.isEmpty()) {
            Toast.makeText(this, "Please fill year", Toast.LENGTH_SHORT).show();
            return false;
        }

        int yearCheck;
        try {
            yearCheck = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid year", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (this.selectVideo == null) {
            Toast.makeText(this, "Please upload video", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addVideo() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText creatorEditText = findViewById(R.id.creatorEditText);
        EditText categoryEditText = findViewById(R.id.categoryEditText);
        EditText descriptionEditText = findViewById(R.id.descriptionEditText);
        EditText yearEditText = findViewById(R.id.yearEditText);

        String title = titleEditText.getText().toString();
        String creator = creatorEditText.getText().toString();
        String category = categoryEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        int year = Integer.parseInt(yearEditText.getText().toString());
        int likes = 0;
        if (this.selectedImage == null) {
            int tempImg = R.drawable.default_image;
            this.selectedImage = Uri.parse("android.resource://" + getPackageName() + "/" + tempImg);
            ;
        }

        LoggedManager loggedManager = LoggedManager.getInstance();
        Video newVideo = new Video(title, creator, category, description, year, loggedManager.getCurrentUser());

        Intent resultIntent = new Intent();
        resultIntent.putExtra("newVideo", newVideo);
        resultIntent.putExtra("imgUri", selectedImage);
        resultIntent.putExtra("videoUri", selectVideo);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


}
