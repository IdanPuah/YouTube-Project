import { useEffect, useState } from 'react';
import styles from "./VideoInfo.module.css"


function VideoInfo({ darkMode, videoinfo }) {
    //console.log(videoinfo);
    const [likePressed, setLikePressed] = useState(false);
    const [likeCount, setLikeCount] = useState(videoinfo.likes);

    function LikesClicked() {
        if (likePressed) {
            removeLike();
            //setLikePressed(false);
        } else {
            addLike();
            //setLikePressed(true);
        }
    }

    const addLike = async () => {
        const token = localStorage.getItem('token');
        const commentData = {
            videoId: videoinfo._id,
            action: 'add'
        };

        try {
            const response = await fetch(`http://localhost:3300/api/videos/handleLikesChange`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `${token}`
                },
                body: JSON.stringify(commentData)
            });
            console.log("working good");
            if (response.ok) {
                const data = await response.json();
                setLikePressed(true);
                setLikeCount(data.videoInfo.likes);
                console.log('like add successfully:', data);
            } else {
                const errorData = await response.json();
                console.log('Error like add: ', errorData);
            }
        } catch (error) {
            console.error('Network error:', error);
        }
    };

    const removeLike = async () => {
        const token = localStorage.getItem('token');

        const commentData = {
            videoId: videoinfo._id,
            action: 'remove'
        };

        try {
            const response = await fetch(`http://localhost:3300/api/videos/handleLikesChange`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `${token}`
                },
                body: JSON.stringify(commentData)
            });

            if (response.ok) {
                const data = await response.json();
                setLikePressed(false);
                setLikeCount(data.videoInfo.likes);
                console.log('Comment edit successfully:', data);
            } else {
                const errorData = await response.json();
                console.log('Error edit comment:', errorData);
            }
        } catch (error) {
            console.error('Network error:', error);
        }
    };
    
    useEffect(() => {
        const getLikeStatus = async () => {
            console.log('get like status:',videoinfo._id);
            const token = localStorage.getItem('token');
            const commentData = {
                videoId: videoinfo._id,
            };
            try {
                const response = await fetch(`http://localhost:3300/api/videos/getLikeStatus`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `${token}`
                    },
                    body: JSON.stringify(commentData)
                });
    
                if (response.ok) {
                    const data = await response.json();
                    if (data.message === 'like pressed') {
                        console.log('like pressed');
                        setLikePressed(true);
                    } else if(data.message === 'like not pressed') {
                        console.log('like not pressed');
                        setLikePressed(false);
                    }
                } else {
                    const errorData = await response.json();
                    console.log('Error get like status:', errorData);
                }
            } catch (error) {
                console.error('Network error:', error);
            }
        };
        getLikeStatus();
    }, []);

    return (
        <div className="container-fluid">
            <div className="row">
                <div className='col-9'>
                <h1 className={`${darkMode ? styles.darkModeText : ''}`}>{videoinfo.title}</h1>
                </div>
                <div className={`col-3 d-flex align-items-center justify-content-end ${styles.info}`}>
                <p className={`${styles.info}`}>Likes: {likeCount} • Views: {videoinfo.views}</p>
                </div>
            </div>
            <div className={`row ${styles.info}`}>
                <div id="artist-img" className={`col-1 ${styles.myButtons}`}>
                    <img className="img-fluid" src={`${videoinfo.creatorImg}`} alt="artist img" />
                </div>
                <div className="col-5 d-flex align-items-center">
                    <h3 className={`${darkMode ? styles.darkModeText : ''}`}>{videoinfo.username}</h3>
                </div>
                <div className={`col-6 ${styles.myButtons} d-flex justify-content-end`}>
                    <button type="button" className={`btn btn-dark ${styles.MyBtn}`}>
                        Subscribe
                    </button>
                    <button
                        type="button"
                        className={`btn btn-outline-secondary like-btn ${styles.MyBtn} ${likePressed ? styles.btnPressed : ''}`}
                        onClick={LikesClicked}
                    >
                        <i className="bi bi-hand-thumbs-up"></i>
                        {likePressed ? 'Liked' : 'Like'}
                    </button>
                    <button type="button" className={`btn btn-outline-secondary ${styles.MyBtn}`}>
                        <i className="bi bi-share"></i>
                        Share
                    </button>
                </div>
            </div>
            <div id="description" className={`row ${styles.description}`}>
                <div id="video-description" className={`container ${styles.videoDescription} ${darkMode ? styles.darkMode : ''}`}>
                    <p>{videoinfo.description}</p>
                </div>
            </div>
        </div>
    )
}

export default VideoInfo