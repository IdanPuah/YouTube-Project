import React, { useContext, useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, useNavigate, Navigate } from 'react-router-dom';
import SignInPage from './signInPage/SignInPage.js';
import VideoPage from './videoPage/VideoPage.js';
import SignUpPage from './sign_up_page/SignUpForm.js';
import Home from './home_page/Home.js';
import Layout from './Layout.js';
import { AuthContext } from './auth/AuthContext.js';

const AppRoutes = () => {
  const { currentUser, isLoggedIn, setCurrentUser, setIsLoggedIn } = useContext(AuthContext);

  const [videoList, setVideoList] = useState([]);
  const [fullVideoList, setFullVideoList] = useState([]);
  const [videoUploaded, setVideoUploaded] = useState(true);
  const [darkMode, setDarkMode] = useState(false);
  const [videoToPlay, setVideoToPlay] = useState({});

  const setCurrentLoggedInUser = (user) => {
    try {
      const userImg = `data:image/png;base64,${bufferToBase64(user.photo.data.data)}`;
      const currUserLoggedIn = {
        username: user.username,
        email: user.email,
        photo: userImg,
        uploads: user.uploads,
        _id: user._id,
      };
      setCurrentUser(currUserLoggedIn);
      setIsLoggedIn(true);
    } catch (error) {
      setIsLoggedIn(false);
      setCurrentUser({});
    }
  };

  function bufferToBase64(buffer) {
    let binary = '';
    const bytes = new Uint8Array(buffer);
    const len = bytes.byteLength;
    for (let i = 0; i < len; i++) {
      binary += String.fromCharCode(bytes[i]);
    }
    return window.btoa(binary);
  }

  const SignInPageWrapper = () => {
    const navigate = useNavigate();
    if (isLoggedIn) {
      navigate('/');
    } else {
      return (
        <SignInPage
          setIsLoggedIn={setIsLoggedIn}
          setCurrentUser={setCurrentUser}
          isLoggedIn={isLoggedIn}
          currentUser={currentUser}
          darkMode={darkMode}
          setCurrentLoggedInUser={setCurrentLoggedInUser}
        />
      );
    }
  };

  // const doSearch = (q) => {
  //   setVideoList(fullVideoList.filter((video) => video.title.includes(q)));
  // };

  const doSearch = (searchVideos) => {
    setVideoList(searchVideos);
  }

  const addVideoToCurrentList = (video) => {
    setFullVideoList((prevList) => [...prevList, video]);
    setVideoList((prevList) => [...prevList, video]);
  };

  const addVideo = (video) => {
    let videoImg = '';
    if (video.thumbnail && video.thumbnail.data) {
      videoImg = `data:image/png;base64,${bufferToBase64(video.thumbnail.data.data)}`;
    } else {
      console.error('Video thumbnail data is undefined or null:', video);
    }

    let videoSrc = '';
    if (video.videoSrc && video.videoSrc.data) {
      videoSrc = `data:image/png;base64,${bufferToBase64(video.videoSrc.data.data)}`;
    } else {
      console.error('Video source data is undefined or null:', video);
    }

    const newVideo = {
      title: video.title,
      author: video.username,
      description: video.description,
      year: new Date().toISOString(),
      category: video.category,
      src: videoSrc,
      img: videoImg,
      views: '0',
      likes: '0',
      comments: '0',
    };
    setVideoUploaded(true);
    setFullVideoList([newVideo, ...fullVideoList]);
    setVideoList([newVideo, ...fullVideoList]);
  };

  const deleteVideo = (videoId) => {
    if (!isLoggedIn) {
      alert('Please sign in to perform this action.');
      return;
    }
    setFullVideoList(fullVideoList.filter((video) => video.id !== videoId));
    setVideoList(videoList.filter((video) => video.id !== videoId));
  };

  const getRandomVideos = () => {
    const shuffled = fullVideoList.sort(() => 0.5 - Math.random());
    return shuffled.slice(0, 15);
  };

  const SignOutHandler = () => {
    setIsLoggedIn(false);
    setCurrentUser({});
    localStorage.removeItem('token');
  };

  const handleEditVideo = (videoId, newTitle, newDescription, newCategory, newThumbnail) => {
    const newVideoList = videoList.map((video) => {
      if (video.id === videoId) {
        video.title = newTitle;
        video.description = newDescription;
        video.category = newCategory;
        video.img = newThumbnail;
      }
      return video;
    });
    setVideoList(newVideoList);
    setFullVideoList(newVideoList);
  };

  useEffect(() => {
    const fetchVideos = async () => {
      try {
        const response = await fetch('http://localhost:3300/api/videos');
        if (response.ok) {
          const videos = await response.json();
          setFullVideoList(videos);
          setVideoList(videos);
        } else {
          console.error('Error fetching videos:', response.statusText);
        }
      } catch (error) {
        console.error('Error fetching videos:', error);
      }
    };

    fetchVideos();
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={
            <Layout
              currentUser={currentUser}
              isLoggedIn={isLoggedIn}
              doSearch={doSearch}
              addVideo={addVideo}
              darkMode={darkMode}
              setDarkMode={setDarkMode}
              SignOutHandler={SignOutHandler}
              setVideoList={setVideoList}
              setFullVideoList={setFullVideoList}
              addVideoToCurrentList={addVideoToCurrentList}
            />
          }
        >
          <Route index element={<Navigate to="/home" />} />
          <Route
            path="/home"
            element={
              <Home
                currentUser={currentUser}
                isLoggedIn={isLoggedIn}
                videoList={videoList}
                fullVideoList={fullVideoList}
                setVideoList={setVideoList}
                setFullVideoList={setFullVideoList}
                darkMode={darkMode}
                setVideoToPlay={setVideoToPlay}
                deleteVideo={deleteVideo}
                handleEditVideo={handleEditVideo}
              />
            }
          ></Route>

          <Route
            path="video/:id/:pid"
            element={
              <VideoPage
                setVideoToPlay={setVideoToPlay}
                videoToPlay={videoToPlay}
                darkMode={darkMode}
                getRandomVideos={getRandomVideos}
                currentUser={currentUser}
              />
            }
          ></Route>
        </Route>
        <Route
          path="/SignUp"
          element={
            <SignUpPage
              darkMode={darkMode}
              setCurrentLoggedInUser={setCurrentLoggedInUser}
            />
          }
        ></Route>
        <Route path="/SignIn" element={<SignInPageWrapper />}></Route>
      </Routes>
    </BrowserRouter>
  );
};

export default AppRoutes;