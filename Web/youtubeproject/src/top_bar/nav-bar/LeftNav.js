import styles from './LeftNav.module.css'
import LeftBar from '../../left_bar/LeftBar';

function LeftNav({ darkMode, setDarkMode, currentUser, setVideoList, setFullVideoList }) {
    return (
        <div className='d-flex align-items-center'>
            <a data-bs-toggle="offcanvas" href="#offcanvasExample" role="button" aria-controls="offcanvasWithBackdrop">
                <i className={`bi bi-list ${styles.listIcon} h2`}></i>
            </a>

            <div className={`offcanvas offcanvas-start ${styles.LeftBarWidth} ${styles.navColor}`} tabIndex="-1" id="offcanvasExample" aria-labelledby="offcanvasExampleLabel" data-bs-backdrop="true">
                <LeftBar darkMode={darkMode} setDarkMode={setDarkMode} currentUser={currentUser} setVideoList={setVideoList} setFullVideoList={setFullVideoList}/>
            </div>


        </div>
    );
}
export default LeftNav;