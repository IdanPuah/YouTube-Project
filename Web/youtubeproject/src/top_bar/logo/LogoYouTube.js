import styles from './LogoYouTube.module.css';
import { useNavigate } from "react-router-dom";


function LogoYouTube({ darkMode, setVideoList, setFullVideoList }) {
  const navigate = useNavigate();


  const handleClick = async () => {
    console.log("clicked");

    try {
      console.log("fetchVideos in home");
      const response = await fetch('http://localhost:3300/api/videos');
      if (response.ok) {
        const videos = await response.json();
        console.log('Videos fetched:');
        console.log(videos);
        setFullVideoList(videos);
        setVideoList(videos);
      } else {
        console.error('Error fetching videos:', response.statusText);
      }
    } catch (error) {
      console.error('Error fetching videos:', error);
    }

    navigate("/home");
  }
  return (
    <button className={`${styles.logoButton} ${darkMode ? styles.darkMode : ''}`} onClick={handleClick}>
      <div className='d-flex align-items-center'>
        <img alt='' src='/youtubeLogo.jpg' className={styles.logo}></img>
      </div>
    </button>
  )
}

export default LogoYouTube;