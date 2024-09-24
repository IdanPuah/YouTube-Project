import React, { useContext } from 'react';
import { AuthContext } from './auth/AuthContext.js'; // Adjust the path as needed
import { useLocation, Outlet } from 'react-router-dom';
import TopBar from './top_bar/TopBar.js';
import styles from './Layout.module.css';


function Layout({ /*currentUser, isLoggedIn,*/ doSearch, addVideo, darkMode, setDarkMode, SignOutHandler, setVideoList, setFullVideoList, addVideoToCurrentList }) {
    const { currentUser, isLoggedIn } = useContext(AuthContext);
    //console.log("Layout: ", currentUser , isLoggedIn);
    const location = useLocation();

    const pathsWithTopBar = ["/home", /^\/video\/[^\/]+\/[^\/]+$/]; // Regular expression for the dynamic video path

    const showTopBar = pathsWithTopBar.some(path =>
        typeof path === 'string' ? location.pathname === path : path.test(location.pathname)
    );

    return (
        <div className={`container-fluid ${darkMode ? styles.darkMode : ''}`}>
            {showTopBar && (
                <TopBar
                    currentUser={currentUser}
                    doSearch={doSearch}
                    addVideo={addVideo}
                    isLoggedIn={isLoggedIn}
                    darkMode={darkMode}
                    setDarkMode={setDarkMode}
                    SignOutHandler={SignOutHandler}
                    setVideoList={setVideoList}
                    setFullVideoList={setFullVideoList}
                    addVideoToCurrentList={addVideoToCurrentList}
                />
            )}
            <Outlet />
        </div>
    );
}

export default Layout;