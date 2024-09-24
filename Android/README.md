# YouTube Android Project

# todo #
add for readme:
  give permesions
  not upload heavy photos
  



This is an Android application for YouTube-like features, including video playback, comments, and more.

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)

## Features

### Home Page

- **Display Videos**: View a list of videos that have been uploaded.
- 
  <img src="https://github.com/IdanPuah/YouTube-project-android/assets/130215814/8140bde0-dadc-482b-8b4e-dd701f408f98" width="150">

- **Dark/Light Mode**: Automatically switch between dark and light themes based on user preference.
- **Dark Mode Button**: A specific button to toggle between dark and light modes manually.
- **Search for Videos**: Search for videos by title or keywords.
- **Filter by Categories**: Filter videos by categories to find specific content.

### User Management

- **Create New User**:
  - **Signup**: Register a new user with a username and password.
  - 
    <img src="https://github.com/IdanPuah/YouTube-project-android/assets/130215814/d285a066-045c-4cd9-9e5e-b828584da84e" width="150">

  - **Validation**: Ensure the username is unique and the password meets security requirements.
  - **Profile Image**: Upload a user profile image or use a default image if none is provided.

- **Sign In**:
  - **Login**: Sign in to an existing user account.
  - **User Abilities**: Authenticated users can comment, like videos, and more.
  - 
  <img src="https://github.com/IdanPuah/YouTube-project-android/assets/130215814/5044050e-3c46-440d-b7f6-d598dbd803a0" width="150">

- **Logout**:
  - **Logout**: Users can log out of their accounts.
  - 
  <img src="https://github.com/IdanPuah/YouTube-project-android/assets/130215814/aff560b8-2b74-42a2-9260-378e1f957921" width="150">

### Watch Video Page

- **Video Playback**: Watch the selected video with controls for play, pause, and more.
- **Like/Dislike**: Authenticated users can like or dislike videos.
- **Comments**: Authenticated users can comment on videos.
  - **Edit Comments**: Users can edit their own comments.
  - **Delete Comments**: Users can delete their own comments.
  - 
  <img src="https://github.com/IdanPuah/YouTube-project-android/assets/130215814/3b176aed-7ad5-49b4-ad77-f7bb31c1536c" width="150">
  <img src="https://github.com/IdanPuah/YouTube-project-android/assets/130215814/7916542d-082f-4f20-9b2b-1699e2e91302" width="150">

### Video Management

- **Edit Video**: Users can edit the details of the videos they have uploaded.
- **Delete Video**: Users can delete the videos they have uploaded.

## The workflow process

Start and Work Distribution: We began by dividing the project into three main parts, as described: the main page and video uploading, the registration and login page, and the video page itself. Each person worked on their tasks independently.

Periodic Integration and Review: Every five days, we conducted status evaluations and merged our work into a central branch that was not the main branch.

Continuing Independent Work: After each merge, everyone pulled the updates and continued working independently on their own branches.

Final Integration and Refinements: Once we had a basic framework in place, we merged everything into the main branch. From there, we performed further refinements to ensure the highest quality product possible within the available timeframe.


## Requirements

- **Android Studio**: Version 4.0 or later
- **Gradle**: Version 6.5 or later
- **JDK**: Version 8 or later
- **Android Version**: Works on Android 10 (API level 29) and above

## Installation

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/IdanPuah/YouTube-project-android.git
    cd YouTube-project-android
    ```

2. **Open the Project in Android Studio**:
    - Open Android Studio.
    - Select `Open an existing Android Studio project`.
    - Navigate to the directory where you cloned the repository and select it.

3. **Build the Project**:
    - Click on `Build` in the menu bar.
    - Select `Rebuild Project`.

4. **Run the Project**:
    - Connect your Android device or start an emulator.
    - Click on the `Run` button (green triangle) in the toolbar.

## Usage

### User Authentication

- **Login**: Users can log in using their credentials.
- **Register**: New users can register by providing a username, password, and profile picture.
- **Logout**: Users can log out of their accounts.

### Video Features

- **Play Video**: Click on a video thumbnail to play the video.
- **Pause/Resume**: Use the play/pause button to control playback.
- **Adjust Settings**: Change video resolution and other settings from the settings menu.

### Comments

- **Add Comment**: Users can add comments to videos.
- **Edit/Delete Comment**: Users can edit or delete their own comments.

### User Profiles

- **View Profile**: Users can view their profile details.
- **Edit Profile**: Users can change their profile picture and information.

### Video Management

- **Edit Video**: Users can edit the details of the videos they have uploaded.
- **Delete Video**: Users can delete the videos they have uploaded.
