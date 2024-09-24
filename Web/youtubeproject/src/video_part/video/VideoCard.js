import styles from './VideoCard.module.css'
import { useNavigate } from 'react-router-dom';
import EditVideo from './EditVideo';
import React, { useState } from 'react';



function VideoCard({key, handleEditVideo, deleteVideo, darkMode, setVideoToPlay, setVideoList, setFullVideoList, currentUser, ...video }) {
    
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const title = video.title;
    const author = video.username;
    const published = video.published;
    const img = video.thumbnail;
    
    const deleteCard = async () => {
        console.log('video card delete id: ', video._id);
        
        try {
            const token = localStorage.getItem('token');
            const url = `http://localhost:3300/api/users/${currentUser._id}/videos/${video._id}`; 
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Authorization': `${token}`
                },
            });
            if (response.ok) {
                const videoToDelete = await response.json();
                console.log('delete video to client');
                console.log(videoToDelete);
                console.log('delete video to client');
                deleteVideo(videoToDelete._id);
                navigate("/home");
            } else if (response.status === 403) {
                console.error('Error deleting video:', response.statusText);
                alert('You are not authorized to delete this video');
            }
            else {
                console.error('Error fetching videos:', response.statusText);
            }
        } catch (error) {
            console.error('Error fetching videos:', error);
        }
    }

    const moveToVideo = async () => {
        console.log('move to video');
        console.log(video);
        console.log(video._id);
        const id = video.creatorId; // Replace with the actual user ID
        const pid = video._id; // Replace with the actual video ID
        navigate(`/video/${id}/${pid}`);
    }

    const editVideo = async (formData) => {
        console.log('save changes');

        const token = localStorage.getItem('token');
        console.log(currentUser._id);

        try {
            console.log('video id: ', video._id);
            const url = `http://localhost:3300/api/users/${currentUser._id}/videos/${video._id}`;
            const response = await fetch(url, {
                method: 'PUT',
                body: formData,
                headers: {
                    'Authorization': `${token}`
                },
            });
            if (response.ok) {
                const videoToEdit = await response.json();
                console.log('edit video to client');
                console.log(videoToEdit);
                console.log('edit video to client');
                // setLoading(false);
                handleEditVideo(videoToEdit._id, videoToEdit.title, videoToEdit.description, videoToEdit.category, videoToEdit.thumbnail);

            } else if (response.status === 403) {
                console.error('Error edit video:', response.statusText);
                alert('You are not authorized to delete this video');
                navigate("/home");
            }
            else {
                console.error('Error fetching videos:', response.statusText);
            }
        } catch (error) {
            console.error('Error fetching videos:', error);
        }

        // e.preventDefault();
        // console.log(newTitle, newDescription, newCategory);

        // handleEditVideo(video.id, newTitle, newDescription, newCategory, newThumbnailURL);
    }

    const modalId = `EditVideoModal${video._id}`;

    return (
        <div className={`card ${styles.videoCard} d-flex col-xl-3 col-lg-4 col-md-4 col-sm-6 ${darkMode ? styles.darkMode : ''}`}  >
            <button className={`${styles.videoButtun}  ratio ratio-16x9 ${darkMode ? styles.imgDarkMode : ''}`} onClick={moveToVideo}>
                <img src={img} className={`object-fit-cover rounded img-fluid card-img-top`} alt="..."></img>
            </button>
            <div className={`${styles.myCardBody} `}>
                <div className='row text-center upper-line'>
                    <div className={`col-11 ${styles.videoButtun}`} onClick={moveToVideo}>
                        <h5 className="card-title">{title}</h5>
                        <p className="card-text">{author} {published}</p>
                    </div>
                    <div className={`${styles.dropMenuIcon} col-1 d-flex align-items-top`}>
                        <div className="dropdown">
                            <button className={`d-flex justify-content-start ${styles.moreOptionBtn} ${darkMode ? styles.darkModeMoreOptionBtn : ''}`} type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <i className="bi bi-three-dots-vertical"></i>
                            </button>
                            <ul className="dropdown-menu">
                                <li><button className="dropdown-item" type="button" onClick={deleteCard}>Delete video</button></li>
                                <li><button className="dropdown-item" type="button" data-bs-toggle="modal" data-bs-target={`#${video._id}`}>Edit</button></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <div className="modal fade" id={video._id} tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <EditVideo currentUser={currentUser} handleEditVideo={handleEditVideo} darkMode={darkMode} video={video} editVideo={editVideo} />
            </div>
        </div>
    );
}
export default VideoCard;