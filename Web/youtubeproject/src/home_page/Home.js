import styles from "./Home.module.css";
import videos from "../data/videos.json";
import VideoPart from "../video_part/VideoPart.js";
import CloudBar from "./cloud-bar/CloudBar.js";
import MiniLeftBar from "../left_bar/MiniLeftBar.js";
import { useEffect, useRef } from "react";

function Home({
  currentUser,
  videoList,
  fullVideoList,
  setVideoList,
  setFullVideoList,
  darkMode,
  setVideoToPlay,
  deleteVideo,
  handleEditVideo,
}) {
  //console.log("Home.js");
  //const isFirstRun = useRef(true);

  useEffect(() => {
    const fetchVideos = async () => {
      try {
        const response = await fetch("http://localhost:3300/api/videos");
        if (response.ok) {
          const videos = await response.json();
          //console.log("Videos fetched:");
          //console.log(videos);
          //setFullVideoList(videos);
          setVideoList(videos);
        } else {
          console.error("Error fetching videos:", response.statusText);
        }
      } catch (error) {
        console.error("Error fetching videos:", error);
      }
    };

    fetchVideos();
  }, [fullVideoList]); // Empty dependency array ensures this runs only once

  //console.log("Home , dark mode : " + darkMode);
  // to back to home
  const toggleHome = () => {
    setVideoList(fullVideoList);
  };
  // set the category to the video list
  const clickCategory = function (category) {
    setVideoList(fullVideoList.filter((video) => video.category === category));
  };
  //console.log(darkMode);
  return (
    <div className={`container-fluid ${darkMode ? styles.darkMode : ""}`}>
      <div className="d-flex flex-row">
        <div className={`d-flex`}>
          <MiniLeftBar
            toggleHome={toggleHome}
            darkMode={darkMode}
            setVideoList={setVideoList}
            setFullVideoList={setFullVideoList}
          />
        </div>
        <div className={`${styles.videos}`}>
          <div className={`${styles.cloudBar} container-fluid`}>
            <CloudBar clickCategory={clickCategory} darkMode={darkMode} />
          </div>
          <VideoPart
            currentUser={currentUser}
            darkMode={darkMode}
            videos={videoList}
            setVideoToPlay={setVideoToPlay}
            deleteVideo={deleteVideo}
            handleEditVideo={handleEditVideo}
            setVideoList={setVideoList}
            setFullVideoList={setFullVideoList}
          />
        </div>
      </div>
    </div>
  );
}
export default Home;
