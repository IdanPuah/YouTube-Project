

import MenuCategory from './categories_menu/MenuCategory';
import styles from './LeftBar.module.css';
import { useNavigate } from 'react-router-dom';



function LeftBar({darkMode, setDarkMode, currentUser, setVideoList, setFullVideoList }) {
    const navigate = useNavigate();
    
    function changeDarkModeStatus() {
        setDarkMode(!darkMode);
        //console.log(darkMode);
    }

    const fetchVideosHomeButtun = async () => {
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
  };

    const showUserVideos = async () => {
        console.log('Show user videos');
        try {
            
            const id = currentUser._id;
            console.log('User ID:', id);
            if (!id) {
                console.error('User ID not found');
                navigate('/SignIn'); // Assuming navigate is a function for routing
                return;
            }
            
            const url = `http://localhost:3300/api/users/${id}/videos`;
            const response = await fetch(url, {
                method: 'GET', // Explicitly specify the GET method
                headers: {
                    'Content-Type': 'application/json'
                    // Add any additional headers if needed
                }
            });
            console.log('Response received');
            console.log('Response status:', response.status);
            
            if (response.ok) {
                const videos = await response.json();
                console.log('Videos fetched:');
                console.log(videos);
                setVideoList(videos); 
            } else {
                console.error('Error fetching videos:', response.statusText);
            }
        } catch (error) {
            console.error('Error fetching videos:', error);
        } 
    }
    

    return (
        <div className={`list-group`}>
            <div className={`d-flex align-items-center justify-content-center`}>
                <img alt="" src='youtubeLogo.jpg' className={styles.logo}></img>
            </div>
            <MenuCategory icon={<i className="bi bi-house-door"></i>} categoryName={'Home'} darkMode={darkMode} onClickHandler={fetchVideosHomeButtun} />
            <MenuCategory icon={<i className="bi bi-substack"></i>} categoryName={'Subscriptions'} />
            <MenuCategory icon={<i className="bi bi-clock-history"></i>} categoryName={'History'} />
            <MenuCategory icon={<i className="bi bi-view-list"></i>} categoryName={'My Videos'} onClickHandler={showUserVideos} />
            <MenuCategory icon={<i className="bi bi-heart"></i>} categoryName={'Liked Movies'} />
            <MenuCategory icon={<i className="bi bi-boombox"></i>} categoryName={'Music'} />
            <MenuCategory icon={<i className="bi bi-gear"></i>} categoryName={'Setting'} />
            <button className={`list-group-item w-100 d-flex align-items-center ${styles.darkMode}`}>
            <h5 className={`${styles.darkModeCategory} `}>Dark Mode</h5>
                <div className={`form-check form-switch d-flex align-items-center`}>
                    <input className="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckDefault" onClick={changeDarkModeStatus}></input>
                </div>
            </button>


        </div>


    );
}
export default LeftBar;