import styles from './AddVideo.module.css'
import AddVideoModal from './AddVideoModal';


function AddVideo({ addVideo, darkMode, addVideoToCurrentList }) {
    return (
        <>
            <div className={`${styles.addVideo} d-flex align-items-center container-fluid`}>
                <button type="button" className={`d-flex align-items-center ${styles.addVideoBtn}`} data-bs-toggle="modal" data-bs-target='#addVideoModal'>
                    <i className={`bi bi-plus-square ${styles.addVideoIcon}`}></i>
                </button>
            </div>
            <div className="modal fade" id="addVideoModal" tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <AddVideoModal addVideo={addVideo} darkMode={darkMode} addVideoToCurrentList={addVideoToCurrentList}/>
            </div>
        </>



    );
}
export default AddVideo;