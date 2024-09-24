
import styles from './VideoPart.module.css';
import VideoCard from "./video/VideoCard";
import React, { useEffect } from 'react';

function VideoPart({ currentUser, videos, darkMode, setVideoToPlay, deleteVideo, handleEditVideo, setVideoLits, setFullVideoList }) {

  useEffect(() => {
    console.log('aviv update');
  }, [videos]);
  //console.log("VideoPart , dark mode : " + darkMode);
  const videosList = videos.map((video, key) => {
    //console.log(video);
    return <VideoCard
      {...video}
      key={key}
      darkMode={darkMode}
      setVideoToPlay={setVideoToPlay}
      deleteVideo={deleteVideo}
      handleEditVideo={handleEditVideo}
      setVideoLits={setVideoLits}
      setFullVideoList={setFullVideoList}
      currentUser={currentUser} />;
  });


  return (
    <div className={`container-fluid ${styles.videoPartWrapper}`}>
      <div className={`${styles.videoPartContainer} row overflow-auto`}>
        {videosList}
      </div>

    </div>
  );
}
export default VideoPart;