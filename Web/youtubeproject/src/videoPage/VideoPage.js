import styles from './VideoPage.module.css';
import Suggestions from './videoSuggestionsSection/VideoSuggestionsSection.js';
import VideoSection from './videoSection/VideoSection.js';
import Spinner from '../loading/spinner.js';
import { useEffect, useState, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

function VideoPage({ videoToPlay, darkMode /* ,setVideoToPlay*/, getRandomVideos, currentUser }) {
  const navigate = useNavigate();
  const  { id, pid } = useParams();
  const [video, setVideo] = useState(null);
  const [key, setKey] = useState(0); // Add a key state
  useEffect(() => {
    //window.location.reload()
  }, [key]);
  useEffect(() => {
    const fetchVideo = async () => {
      console.log('fetch video');
      const url = `http://localhost:3300/api/users/${id}/videos/${pid}`; // Construct your URL with the id and pid parameters
      try {
        const response = await fetch(url, {
          method: 'GET', // Explicitly specify the GET method (optional as GET is default)
        });

        if (response.ok) {
          const resVideo = await response.json();
          setVideo(resVideo);
          setKey(prevKey => prevKey + 1); // Update the key to force re-render
          //console.log('get video to client');
          //console.log(resVideo);
          
        } else {
          console.error('Error fetching videos:', response.statusText);
        }
      } catch (error) {
        console.error('Error fetching videos:', error);
      }
    };

    fetchVideo();
  }, [id ,pid]);

  const setVideoToPlay = (newVideo) => {
    //console.log('set video to play');
    navigate(`/video/${newVideo.creatorId}/${newVideo._id}`);
  }

  return (
    <div id='videoPageContainer' className={`container-fluid text-cent ${styles.videoPageContainer}`}>
      <div className="row">
        <div className="col-9">
          {video ? (
            <VideoSection key={key} videoinfo={video} darkMode={darkMode} currentUser={currentUser}/>
          ) : (
            <div className="d-flex justify-content-center align-items-center">
              <Spinner />
            </div>
          )}
        </div>
        <div className="col">
          <Suggestions setVideoToPlay={setVideoToPlay} darkMode={darkMode} getRandomVideos={getRandomVideos} videoId={pid} currentUser={currentUser}/>
        </div>
      </div>
    </div>
  )
}

export default VideoPage;