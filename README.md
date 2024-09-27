# YouTube-Project
The YouTube Project is a full-stack multimedia platform designed to allow users to upload, view, and manage videos across multiple devices. This project encompasses three core components: a web-based platform, a mobile application for Android, and a backend server built using Node.js and C++.

Web Platform (React): The web application is developed using React and provides users with an intuitive interface to search for videos, view content, and upload videos. It features a user-friendly layout similar to YouTube’s design, including navigation components such as a search bar, left sidebar, and a cloud bar for categories. The project leverages responsive design to ensure a seamless experience across different screen sizes.

Android App: The mobile application is built for Android, offering similar functionality to the web platform with additional features for mobile users. The app allows users to browse videos, upload new content, and filter by categories. It uses Room for local database management and Glide for efficient image handling. The app also includes user authentication, displaying either a login button or profile image in the bottom bar based on the user’s logged-in state.

Backend Server (Node.js and C++): The backend is designed using a combination of Node.js for handling API requests and C++ for performance-critical tasks. The server handles user authentication, video uploads, and media streaming. Node.js serves as the primary API layer, ensuring smooth communication between the client and server, while C++ is utilized for processing-intensive tasks such as video encoding and serving media files.

Database: The project integrates with both NoSQL (MongoDB) and SQL databases for user data and video metadata storage, ensuring flexibility and scalability.

This project demonstrates the integration of modern web technologies, mobile development, and robust backend solutions to create a seamless multimedia experience.
## Web Application

### Home Page
 
<img src="https://github.com/avivyair/YoutubeProject/assets/145895129/e4f3f985-ffbc-4458-8bd5-091532530e36" width="300">
<img src="https://github.com/avivyair/YoutubeProject/assets/145895129/4de58eb7-609e-431f-8d9d-1090480ed99b" width="300">

### Video Page

<img src="https://github.com/avivyair/YoutubeProject/assets/145895129/12070217-72e8-4a1a-a8e0-b4c1dae8229b" width="300">
<img src="https://github.com/avivyair/YoutubeProject/assets/145895129/ee0b6da9-d0b9-4a9d-b30e-8db8fef88efb" width="300">

### Sign In

<img src="https://github.com/avivyair/YoutubeProject/assets/145895129/98c5700c-65ab-4ad8-9404-f6244753e0f7" width="300">
<img src="https://github.com/avivyair/YoutubeProject/assets/145895129/42411ae2-4398-40de-8ab2-4c74e0f77ffa" width="300">

### Sign Up

<img src="https://github.com/avivyair/YoutubeProject/assets/145895129/9a26ebe0-da3e-40e3-9fea-3ab87f491ee5" width="300">
<img src="https://github.com/avivyair/YoutubeProject/assets/145895129/51299440-812f-454d-b938-58ac3d9dfc20" width="300">

### Add video

<img src="https://github.com/user-attachments/assets/8a62b14e-7df4-44e8-9a5a-da2624d9a199" width="300">

### Edit Video

<img src="https://github.com/user-attachments/assets/d18a7a61-7275-474f-bb7e-fadd8f98c5b0" width="300">

### Delete video

<img src="https://github.com/user-attachments/assets/907bf36f-b645-4053-85ff-3c0c1ca4937a" width="300">

### Search for video

<img src="https://github.com/user-attachments/assets/6855780e-5fcc-45c0-9b52-4e70cdb283d0" width="300">

### Menu

<img src="https://github.com/user-attachments/assets/91c174cb-b3ad-4a2f-b11d-0fca26b951a9" width="300">

## Android Application

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

### Delete video

<img src="https://github.com/user-attachments/assets/11451d92-532a-4a1b-a266-c4279adca141" width="150">

### Search video

<img src="https://github.com/user-attachments/assets/c22c0074-94b3-40f3-9964-e6e43ca28702" width="150">


### Video Management

- **Upload Video**: Users can upload new videos.
- **Watch Video**: Users can watch videos.
- **Edit Video**: Users can edit the details of the videos they have uploaded.
- **Delete Video**: Users can delete the videos they have uploaded.

## The workflow process

Start and Work Distribution: We began by dividing the project into three main parts, as described: the main page and video uploading, the registration and login page, and the video page itself. Each person worked on their tasks independently.

Periodic Integration and Review: Every five days, we conducted status evaluations and merged our work into a central branch that was not the API branch.

Continuing Independent Work: After each merge, everyone pulled the updates and continued working independently on their own branches.

Final Integration and Refinements: Once we had a basic framework in place, we merged everything into the main branch. From there, we performed further refinements to ensure the highest quality product possible within the available timeframe.
