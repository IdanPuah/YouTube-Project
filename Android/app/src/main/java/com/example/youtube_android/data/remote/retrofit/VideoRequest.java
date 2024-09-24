package com.example.youtube_android.data.remote.retrofit;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;

import com.example.youtube_android.util.MyApplication;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VideoRequest {
    @SerializedName("title")
    private RequestBody title;
    @SerializedName("description")
    private RequestBody description;

    @SerializedName("category")
    private RequestBody category;
    @SerializedName("videoSrc")
    private MultipartBody.Part videoUrl;
    @SerializedName("thumbnail")
    private MultipartBody.Part thumbnailUrl;

    public VideoRequest(String title, String description, String category, Uri thumbnailUri) {
        this.title = RequestBody.create(MediaType.parse("text/plain"), title);
        this.description = RequestBody.create(MediaType.parse("text/plain"), description);
        this.category = RequestBody.create(MediaType.parse("text/plain"), category);
        this.thumbnailUrl = createMultipartBodyPart(MyApplication.getAppContext(), "thumbnail", thumbnailUri, "image/*");
    }

    public VideoRequest(String title, String description, String category, Uri videoUri, Uri thumbnailUri) {
        this.title = RequestBody.create(MediaType.parse("text/plain"), title);
        this.description = RequestBody.create(MediaType.parse("text/plain"), description);
        this.category = RequestBody.create(MediaType.parse("text/plain"), category);

//        File videoFile = new File(videoUrl.getPath());
//        File thumbnailFile = new File(thumbnailUrl.getPath());
//        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
//        RequestBody thumbnailBody = RequestBody.create(MediaType.parse("image/*"), thumbnailFile);

        Log.d("VideoRequest", "Creating thumbnail with URI: " + thumbnailUri.toString());

        this.videoUrl = createMultipartBodyPart(MyApplication.getAppContext(), "videoSrc", videoUri, "video/*");
        this.thumbnailUrl = createMultipartBodyPart(MyApplication.getAppContext(), "thumbnail", thumbnailUri, "image/*");
    }

    private MultipartBody.Part createMultipartBodyPart(Context context, String partName, Uri fileUri, String mimeType) {
        File file;
        try {
            if ("content".equals(fileUri.getScheme())) {
                // Handle content URIs
                InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
                file = new File(context.getCacheDir(), getFileNameFromContentUri(context, fileUri));
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                }
            } else if ("file".equals(fileUri.getScheme())) {
                // Handle file URIs
                file = new File(fileUri.getPath());
            } else if (fileUri.toString().startsWith("data:")) {
                // Handle Base64-encoded data URIs
                String base64Data = fileUri.toString().substring(fileUri.toString().indexOf(",") + 1);
                byte[] decodedBytes = Base64.decode(base64Data, Base64.DEFAULT);

                file = new File(context.getCacheDir(), getFileNameFromBase64Uri(fileUri.toString()));
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    outputStream.write(decodedBytes);
                }
            } else {
                throw new IllegalArgumentException("Unsupported URI scheme: " + fileUri.getScheme());
            }

            RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
        } catch (IOException e) {
            Log.e("VideoRequest", "Error creating MultipartBody.Part", e);
            return null;
        }
    }

    private String getFileNameFromBase64Uri(String base64Uri) {
        // Generate a file name from the Base64 URI
        // This is a simple implementation. Adjust as needed.
        return "image_" + System.currentTimeMillis() + ".jpg";
    }

    private String getFileNameFromContentUri(Context context, Uri uri) {
        String fileName = "unknown";
        try {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (displayNameIndex != -1) {
                    fileName = cursor.getString(displayNameIndex);
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("VideoRequest", "Error accessing file name from content URI", e);
        }
        return fileName;
    }
//    private MultipartBody.Part createMultipartBodyPart(Context context, String partName, Uri fileUri, String mimeType) {
//        try {
//            InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
//            File file = new File(context.getCacheDir(), getFileName(context, fileUri));
//            OutputStream outputStream = new FileOutputStream(file);
//
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//
//            inputStream.close();
//            outputStream.close();
//
//            RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);
//            return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private String getFileName(Context context, Uri uri) {
        String fileName = "unknown";
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                File file = new File(uri.getPath());
                fileName = file.getName();
                inputStream.close();
            }
        } catch (IOException e) {
            Log.e("VideoRequest", "Error accessing file", e);
        }
        return fileName;
    }


    public RequestBody getTitle() {
        return title;
    }

    public RequestBody getDescription() {
        return description;
    }

    public RequestBody getCategory() {
        return category;
    }

    public MultipartBody.Part getVideoUrl() {
        return videoUrl;
    }

    public MultipartBody.Part getThumbnailUrl() {
        return thumbnailUrl;
    }
}
