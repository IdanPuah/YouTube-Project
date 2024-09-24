import { useEffect , useState } from 'react';
import VideoCard from './videoCard/VideoCard';

function Suggestions({ darkMode, setVideoToPlay, getRandomVideos, videoId, currentUser }) {
    const [suggestions, setSuggestions] = useState([]);
    //console.log('currentUser:', currentUser._id);
    
    useEffect(() => {
        const getSuggestions = async () => {
            try {
                console.log('in use effect');
                console.log('videoId:', videoId);
                // let userId;
                // if (!currentUser || !currentUser._id){
                //     console.log('no user');
                //     userId = null;
                    
                // }
                // else{
                //     userId = currentUser._id;
                // }
                const userId = currentUser?._id || null;
                 
                
                const response = await fetch(`http://localhost:3300/api/users/${userId}/suggestions/${videoId}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                if (response.ok) {
                    const data = await response.json();
                    console.log('suggestions:', data);
                    setSuggestions(data.videos);
                } else {
                    const errorData = await response.json();
                    //console.log('Error suggestions: ', errorData);
                }
            } catch (error) {
                console.error('Network error:', error);
            }
        };
        getSuggestions();
    }, [videoId]);
    //console.log(randomVideos);
    const suggestionList = suggestions.map((video, key) => {
        //console.log(video);
        return <VideoCard {...video} key={key} darkMode={darkMode} setVideoToPlay={setVideoToPlay} />
    });
    return (

        <div className="col">
            {suggestionList}
        </div>
    )

}

export default Suggestions;