package com.example.youtube_android.ui.signup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.youtube_android.R;
import com.example.youtube_android.data.local.dao.UserDao;
import com.example.youtube_android.data.local.database.LocalAppDB;
import com.example.youtube_android.data.model.UserViewModel;
import com.example.youtube_android.ui.main.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 1001;
    private static final int REQUEST_GALLERY = 102;
    private static final int REQUEST_PERMISSION = 103;
    private EditText edtEmail, edtUserName, edtPassword, passwordVer, edtID;
    private Button btnSignUp, btnSelectImage;
    private ImageView loadingSpinner, imgProfilePreview;
    private Animation animRotate;
    private Uri imgProfileUri;
    private String imgProfileBase64;  // Store Base64 string here
    private LocalAppDB localDB;
    private UserDao userDao;
    private UserViewModel userViewModel;
    private Uri imgProfilePath;  // Store image file path here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        localDB = LocalAppDB.getInstance(this);
        userDao = localDB.userDao();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        edtEmail = findViewById(R.id.signUpEmail);
        edtUserName = findViewById(R.id.signUpUserName);
        edtPassword = findViewById(R.id.signUpPassword);
        passwordVer = findViewById(R.id.signUpPasswordVerification);
        imgProfilePreview = findViewById(R.id.imgProfilePreview);
        imgProfilePreview.setVisibility(View.GONE);
        loadingSpinner = findViewById(R.id.loadingSpinner);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);

        btnSignUp = findViewById(R.id.signUpButton);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        btnSelectImage.setOnClickListener(v -> {
            // Check permissions
            if (checkAndRequestPermissions()) {
                selectImageFromGallery();
            }
        });

        btnSignUp.setOnClickListener(v -> {
            if (validateInput()) {
                // Show the loading spinner and start animation
                loadingSpinner.setVisibility(View.VISIBLE);
                loadingSpinner.startAnimation(animRotate);

                // Pass the Base64 image string along with other details
                userViewModel.signUp(
                        edtUserName.getText().toString(),
                        edtEmail.getText().toString(),
                        edtPassword.getText().toString(),
                        imgProfileUri
                );
            }
        });
        // Observe the sign-up result
        userViewModel.getSignUpResult().observe(this, result -> {
            // Stop the loading spinner and handle the result
            loadingSpinner.clearAnimation();
            loadingSpinner.setVisibility(View.GONE);

            if (result != null) {
                switch (result) {
                    case 0: // Success
                        Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
                        Intent mainActivity = new Intent(SignUpActivity.this, MainActivity.class);
                        mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainActivity);
                        finish();
                        break;
                    case 1: // Email already exists
                        Toast.makeText(this, "Email already exists.", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(this, "Username already exists.", Toast.LENGTH_SHORT).show();
                        break;
                    case 3: // Network error or null response
                        Toast.makeText(this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case 4: // General failure
                        Toast.makeText(this, "Sign-up failed due to out of memory. Please try delete cache.", Toast.LENGTH_SHORT).show();
                        break;
                    case 5: // General failure
                        Toast.makeText(this, "Sign-up failed. Please try again.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "If the problem consist please grant all image permissions to the app!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        // Stop the loading spinner and handle the result
                        loadingSpinner.clearAnimation();
                        loadingSpinner.setVisibility(View.GONE);
                }

            }
        });
    }


    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                imgProfileUri = data.getData();

                // Grant permission for the specific URI
                grantUriPermission(getPackageName(), imgProfileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Check if the selected image URI is valid
                if (imgProfileUri != null) {
                    try {
                        // Load the image into the ImageView using Glide
                        Glide.with(this)
                                .load(imgProfileUri)
                                .into(imgProfilePreview);

                        // Show the ImageView after loading the image
                        imgProfilePreview.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        imgProfilePreview.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cursor.getString(index);
                }
            } finally {
                cursor.close();
            }
        }
        return path != null ? path : uri.getPath();
    }

    // Helper method to get the real path from URI
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private boolean validateInput() {
        String email = this.edtEmail.getText().toString().trim();
        String userName = this.edtUserName.getText().toString().trim();
        String password = this.edtPassword.getText().toString().trim();
        String passwordVer = this.passwordVer.getText().toString().trim();

        if (email.isEmpty() || userName.isEmpty() || password.isEmpty() || passwordVer.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!passwordVer.equals(password)) {
            Toast.makeText(this, "Password and Verification must be the same", Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean hasLetter = password.matches(".*[a-zA-Z]+.*");
        boolean hasDigit = password.matches(".*\\d+.*");
        if (!(hasLetter && hasDigit)) {
            Toast.makeText(this, "Password must contain at least one char and number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION);
                return false;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            boolean permissionGranted = false;

            // Check if any permission was granted
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    break;
                }
            }

            if (permissionGranted) {
                Toast.makeText(this, "Permission granted. You can now select a profile image.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You can still select specific images from your gallery.", Toast.LENGTH_SHORT).show();
            }
            // Proceed with selecting the image
            selectImageFromGallery();
        }
    }


}
