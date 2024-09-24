import React, { useContext } from 'react';
import { AuthContext } from '../auth/AuthContext'; // Adjust the path as needed
import LeftNav from "./nav-bar/LeftNav";
import LogoYouTube from "./logo/LogoYouTube";
import SearchBar from "./search_bar/SearchBar";
import AddVideo from "./add_video/AddVideo";
import styles from './TopBar.module.css';
import SelfDetails from "./self_picture/SelfDeatails";
import Notifications from "./notifications/Notifications";
import { Link } from "react-router-dom";


function TopBar({/*currentUser,isLoggedIn,*/ doSearch, addVideo, darkMode, setDarkMode, SignOutHandler, setVideoList, setFullVideoList, addVideoToCurrentList }) {
  //console.log("TopBar");
  //console.log("is user log in: "+isLogin);
  //console.log(currentUser);
  const { currentUser, isLoggedIn } = useContext(AuthContext);
  return (

    <div className={`container-fluid`}>
      <div className='d-flex justify-content-between'>
        <div className={`${styles.leftDiv} d-flex justify-content-start`}>
          <LeftNav darkMode={darkMode} setDarkMode={setDarkMode} currentUser={currentUser} setVideoList={setVideoList} setFullVideoList={setFullVideoList}/>
          <LogoYouTube darkMode={darkMode} setVideoList={setVideoList} setFullVideoList={setFullVideoList}/>

        </div>
        <div className={`${styles.centerDiv} d-flex align-items-center`}>
          <SearchBar doSearch={doSearch} darkMode={darkMode} />
        </div>
        <div className={`${styles.rightDiv} d-flex justify-content-end`}>
          {isLoggedIn ? (
            <>
              <AddVideo addVideo={addVideo} currentUser={currentUser} darkMode={darkMode} addVideoToCurrentList={addVideoToCurrentList}/>
              <Notifications />
              <SelfDetails currentUser={currentUser} SignOutHandler={SignOutHandler}/>
            </>
          ) : (
            <div className="d-flex align-items-center">
              <Link to="/SignIn">
                <button type="button" className={`btn btn-outline-primary ${styles.myBtn}`}>
                  <i className={`bi bi-person-circle ${styles.icon}`}></i>
                  Sign In
                </button>
              </Link>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
export default TopBar;