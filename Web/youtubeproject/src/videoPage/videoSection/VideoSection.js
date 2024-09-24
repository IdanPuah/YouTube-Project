import styles from './VideoSection.module.css';
import Video from './Video';
import VideoInfo from './videoInfo/VideoInfo';
import CommentsSection from './commentsSection/CommentsSection';
function VideoSection({ darkMode, videoinfo, currentUser}) {
    console.log("VideoSection: ", videoinfo);
    return (
        <div id='left-div'>
            <div id="video-div" className={`row ${styles.videoDiv}`}>
                <Video videoSrc={videoinfo.videoSrc}/>
            </div>
            <div id="video-information" className="row">
                <VideoInfo darkMode={darkMode} videoinfo={videoinfo}/>
            </div>
            <div id="comments" className="row">
                <CommentsSection darkMode={darkMode} videoinfo={videoinfo} currentUser={currentUser}/>
            </div>
        </div>
    )
}

export default VideoSection;