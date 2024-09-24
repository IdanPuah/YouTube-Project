
import React from 'react';

function Video({ videoSrc }) {
    
    console.log('Video.js');
    console.log(videoSrc);
    
    return (
        <div className="ratio ratio-16x9">
            <video controls autoPlay>
                <source src={videoSrc} type="video/mp4" />
                Your browser does not support the video tag.
            </video>
        </div>
    );
}

export default Video;
