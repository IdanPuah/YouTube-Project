import styles from './VideoCard.module.css';
import { useNavigate } from 'react-router-dom';

function VideoCard({ darkMode, setVideoToPlay, ...video }) {

    //console.log("VideoCard");
    //console.log(video);
    const navigate = useNavigate();
    ////////////////////
    const thumbnail = video.thumbnail;
    const videoTitle = video.title;
    const channelName = video.username;
    const videoViews = video.views;
    const id = video.creatorId;
    const pid = video._id;
    
    //console.log(thumbnail, videoTitle, channelName, videoViews, publicationDate)
    ////////////////////
    const moveToVideo = () => {
        
        console.log("move to video: ", video);
        
        setVideoToPlay(video);
        navigate(`/video/${id}/${pid}`);
    }

    const getPublicationYear = (dateString) => {
        const date = new Date(dateString);
        return date.getFullYear();
    };
    const publicationDate = getPublicationYear(video.date);

    return (
        <div className={`row ${styles.suggestions}`} onClick={moveToVideo}>
            <div className={`col ${styles.thumbnailDiv} d-flex align-items-center`}>
                <div className={styles.imgDiv}>
                    <img className={`${styles.thumbnail} img-fluid w-100`} src={thumbnail} alt="thumbnail"></img>
                </div>

            </div>
            <div id="videoInfo" className={`col-6 ${styles.videoInfo}`}>
                <div id="info" className={styles.info}>
                    <p className={`h6 ${darkMode ? styles.darkModeText : ''}`} id="video-title">{videoTitle}</p>
                    <p className={`${darkMode ? styles.darkModeText : ''}`} id="channel-name">{channelName}</p>
                    <p className={`${darkMode ? styles.darkModeText : ''}`} id="video-views">views: {videoViews} • {publicationDate}</p>
                </div>
            </div>
        </div>
    );
}

export default VideoCard;