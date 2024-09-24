package com.example.youtube_android.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.youtube_android.R;
import com.example.youtube_android.adapters.VideoListAdapter;
import com.example.youtube_android.data.local.database.LocalAppDB;
import com.example.youtube_android.data.model.User;
import com.example.youtube_android.data.model.UserViewModel;
import com.example.youtube_android.data.model.Video;
import com.example.youtube_android.data.model.VideoViewModel;
import com.example.youtube_android.data.remote.retrofit.VideoRequest;
import com.example.youtube_android.databinding.ActivityMainBinding;
import com.example.youtube_android.ui.addvideo.addVideoActivity;
import com.example.youtube_android.ui.login.LoginActivity;
import com.example.youtube_android.util.LoggedManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int  UPDATE = 1;
    private static final int  NOT_UPDATE_ERROR = 0;
    private static final int  SAME_NAME = 2;
    private static final int  SAME_MAIL = 3;
    private static final int  SAME_PASSWORD = 4;
    private static final int LOGIN_REQUEST_CODE = 1001;
    private ActivityMainBinding binding;
//    private VideosList videos;
    private VideoListAdapter adapter;
    private LoggedManager loggedManager = LoggedManager.getInstance();
    private UserViewModel userViewModel;
    private VideoViewModel videoViewModel;
    private boolean isRefreshing = false;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);

        initDB();
        initUsers();
//        initData();
        setupViews();
        setupListeners();
        setupRecyclerView();
        handleThemePreference();

        videoViewModel.getTwentyVideos().observe(this, videos -> {
            adapter.setVideos(videos);
        });

        // Check if user is logged in and update UI
        updateUIBasedOnLoginStatus();
    }

    private void initDB() {
        LocalAppDB localDB = LocalAppDB.getInstance(this);
    }

    private void initUsers() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

//    private void initData() {
//        videos = VideosList.getInstance();
//        if (!videos.isJsonDataLoaded) {
//            videos.loadJsonDataFromFile(this, "videos.json", getPackageName());
//            Log.d("MainActivity", "pic from json: " + videos.getVideo(1).getUriImg());
//        }
//    }

    private void setupViews() {
        binding.profileImage.setOnClickListener(this::showPopupMenu);
        binding.addVideoImg.setOnClickListener(this::handleAddVideoClick);
        binding.LogoButton.setOnClickListener(v -> adapter.setVideos(videoViewModel.getTwentyVideos().getValue()));
        binding.loginButton.setOnClickListener(this::handleLoginClick);
        binding.myVideos.setOnClickListener(v -> adapter.setVideos(videoViewModel.getUserVideos().getValue()));
        binding.sportButton.setOnClickListener(v -> displayVideosByCategory("Sport"));
        binding.historyButton.setOnClickListener(v -> displayVideosByCategory("History"));
        binding.healthButton.setOnClickListener(v -> displayVideosByCategory("Health"));
        setupSearchView();
    }

    private void setupListeners() {
        binding.toggleThemeButton.setOnClickListener(this::toggleTheme);
    }

    private void setupRecyclerView() {
        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        RecyclerView lstVideos = findViewById(R.id.lstVideos);
        adapter = new VideoListAdapter(this, videoViewModel, this);
        lstVideos.setAdapter(adapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));
//        adapter.setVideos(videos.getVideosList());

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Trigger the refresh action when swipe-to-refresh is activated
            if (!isRefreshing) {
                refreshData();
            }
        });
    }

    private void refreshData() {
        isRefreshing = true; // Set refreshing flag to prevent multiple calls
        swipeRefreshLayout.setRefreshing(true); // Show the refresh icon
        Log.d("refresh:", "in refresh");

        // Trigger your server call here to refresh data
        videoViewModel.getTwentyVideos().observe(this, refreshedVideos -> {
            if (refreshedVideos != null) {
                Log.d("refresh:", "Data received: " + refreshedVideos.size() + " items");
                adapter.setVideos(refreshedVideos); // Assuming your adapter has a setVideos method
            } else {
                Log.d("refresh:", "No data received");
            }
            isRefreshing = false; // Reset the refreshing flag
            swipeRefreshLayout.setRefreshing(false); // Hide the refresh icon
        });
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterVideos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterVideos(newText);
                return true;
            }
        });
    }

    private void handleAddVideoClick(View view) {
        if (loggedManager.getIsLogged()) {
            Intent addVideoActivity = new Intent(this, addVideoActivity.class);
            startActivityForResult(addVideoActivity, 1);
        } else {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
        }
    }

    private void handleLoginClick(View view) {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
    }

    private void handleThemePreference() {
        SharedPreferences preferences = getSharedPreferences("theme_pref", MODE_PRIVATE);
        boolean isNightMode = preferences.getBoolean("night_mode", false);
        AppCompatDelegate.setDefaultNightMode(isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void toggleTheme(View view) {
        boolean nightMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
        saveThemePreference(!nightMode);
    }

    private void saveThemePreference(boolean isNightMode) {
        SharedPreferences preferences = getSharedPreferences("theme_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("night_mode", isNightMode);
        editor.apply();
    }

    private void displayVideosByCategory(String category) {
        List<Video> filteredVideos = new ArrayList<>();
        for (Video video : videoViewModel.getVideos().getValue()) {
            if (video.getCategory().equals(category)) {
                filteredVideos.add(video);
            }
        }
        adapter.setVideos(filteredVideos);
    }

    private void filterVideos(String query) {
        videoViewModel.getSearchVideos(query).observe(this, videoList ->
                adapter.setVideos(videoList));

    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.user_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.LogOutBtn) {
                onLogout();
                return true;
            }
            if (item.getItemId() == R.id.EditUser){
                Log.d("Main Acivity", " edit video");
                showEditUserDialog();
                return true;
            }
            if (item.getItemId() == R.id.DeleteUser){
                deleteUser();
                Log.d("Main Acivity", " delete video");
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    public void deleteUser(){
        userViewModel.deleteUser(loggedManager.getToken(), loggedManager.getCurrentUser().getId()).observe(this, userDelete -> {
            if (userDelete){
                Log.d("Main Acivity", " delete user in main activity");
                onLogout();
            }
        });
    }

    public void onLogout() {
        loggedManager.setCurrentUser(null);
        loggedManager.setLogged(false);
        binding.loginButton.setVisibility(View.VISIBLE);
        binding.profileImage.setVisibility(View.GONE);
    }

    private void setProfileImage(ImageView profileImage) {
        User currentUser = loggedManager.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getProfileImgUri() != null) {
                Uri profileImgUri = Uri.parse(currentUser.getProfileImgUri());
                Glide.with(this)
                        .load(profileImgUri)
                        .override(100, 100)
                        .centerCrop()
                        .placeholder(R.drawable.ic_profile_default)
                        .error(R.drawable.ic_profile_default)
                        .into(profileImage);
            } else if (currentUser.getPhoto() != null) {
                Bitmap photoBitmap = currentUser.getPhoto().getBitmap();
                if (photoBitmap != null) {
                    profileImage.setImageBitmap(photoBitmap);
                } else {
                    profileImage.setImageResource(R.drawable.ic_profile_default);
                }
            } else if (currentUser.getProfileImgRes() != 0) {
                profileImage.setImageResource(currentUser.getProfileImgRes());
            } else {
                profileImage.setImageResource(R.drawable.ic_profile_default);
            }
        } else {
            profileImage.setImageResource(R.drawable.ic_profile_default);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUIBasedOnLoginStatus();
    }

    private void updateUIBasedOnLoginStatus() {
        if (loggedManager.getIsLogged()) {
            binding.profileImage.setVisibility(View.VISIBLE);
            binding.loginButton.setVisibility(View.GONE);
            setProfileImage(binding.profileImage);
        } else {
            binding.loginButton.setVisibility(View.VISIBLE);
            binding.profileImage.setVisibility(View.GONE);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (!videos.isJsonDataLoaded) {
//            videos.loadJsonDataFromFile(this, "videos.json", getPackageName());
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Video newVideo = data.getParcelableExtra("newVideo");
            Uri newUri = data.getParcelableExtra("imgUri");
            Log.d("mainactivity", "onActivityResult img:" + newUri);
            Uri newVideoUri = data.getParcelableExtra("videoUri");
            if (newVideo != null) {
                VideoRequest videoToAddRequest = new VideoRequest(newVideo.getTitle(), newVideo.getDescription(), newVideo.getCategory(), newVideoUri, newUri);
                videoViewModel.addVideo(videoToAddRequest).observe(this, videos -> {
                    List<Video> videoList = videos;
                    adapter.setVideos(videoList);
                });

//                videos.addVideo(tempVideo);
//                adapter.setVideos(videos.getVideosList());
            }
        }
    }


    private void showEditUserDialog() {
        // Create the dialog and inflate the custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_username, null); // Use your updated XML
        builder.setView(dialogView);

        // Find the EditTexts and buttons in the dialog layout
        EditText editUserName = dialogView.findViewById(R.id.editUserName);
        EditText editEmail = dialogView.findViewById(R.id.editEmail);
        EditText editPassword = dialogView.findViewById(R.id.editPassword);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        User currentUser = loggedManager.getCurrentUser();

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Set click listener for the confirm button
        confirmButton.setOnClickListener(v -> {
            String newUserName = editUserName.getText().toString().trim();
            String newEmail = editEmail.getText().toString().trim();
            String newPassword = editPassword.getText().toString().trim();

            // If fields are empty, keep current values
            if (newUserName.isEmpty()) {
                newUserName = null;  // Use the current value if not updated
            }
            if (newEmail.isEmpty()) {
                newEmail = null;        // Use the current value if not updated
            }
            if (newPassword.isEmpty()) {
                newPassword = null;  // Use the current value if not updated
            }

            // Now you can send the values (new or unchanged) to the server
            userViewModel.updateUser(newUserName, newEmail, newPassword).observe(this, isUpdate -> {
                if (isUpdate == UPDATE) {
                    adapter.setVideos(videoViewModel.getTwentyVideos().getValue());
                    Log.d("MainActivity", "User info updated successfully");
                }
                else if (isUpdate == SAME_NAME){
                    Log.d("MainActivity", "name taken");
                    Toast.makeText(this, "User name already taken", Toast.LENGTH_LONG).show();
                }
                else if (isUpdate == SAME_MAIL){
                    Toast.makeText(this, "Email already taken", Toast.LENGTH_LONG).show();
                }
                else if (isUpdate == SAME_PASSWORD){
                    Toast.makeText(this, "Password already taken", Toast.LENGTH_LONG).show();
                }
            });

            dialog.dismiss();
        });

        // Set click listener for the cancel button
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }

//    private void showEditUsernameDialog() {
//        // Create the dialog and inflate the custom layout
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_edit_username, null);
//        builder.setView(dialogView);
//
//        // Find the EditText and buttons in the dialog layout
//        EditText editUserName = dialogView.findViewById(R.id.editUserName);
//        Button confirmButton = dialogView.findViewById(R.id.confirmButton);
//        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
//
//        // Create the dialog
//        AlertDialog dialog = builder.create();
//
//        // Set click listener for the confirm button
//        confirmButton.setOnClickListener(v -> {
//            String newUserName = editUserName.getText().toString().trim();
//            if (!newUserName.isEmpty()) {
//                // Perform action with the new username (e.g., update user info)
//                Log.d("MainActivity", "New username: " + newUserName);
//
//                // change user name in server
//                userViewModel.updateUser(newUserName).observe(this, isUpdate -> {
//                    if (isUpdate){
//                        adapter.setVideos(videoViewModel.getTwentyVideos().getValue());
//                        Log.d("MainActivity", "New username is updated ");
//                    }
//                });
//
//                dialog.dismiss();
//            } else {
//                editUserName.setError("Username cannot be empty");
//            }
//        });
//
//        // Set click listener for the cancel button
//        cancelButton.setOnClickListener(v -> dialog.dismiss());
//
//        // Show the dialog
//        dialog.show();
//    }

}
