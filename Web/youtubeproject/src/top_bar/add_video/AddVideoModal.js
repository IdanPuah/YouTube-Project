
import React, { useState } from 'react';
import styles from './AddVideoModal.module.css';


function AddVideoForm({ addVideo, darkMode, addVideoToCurrentList }) {
  // const [videoFile, setVideoFile] = useState(null);
  // const [thumbnailFile, setThumbnailFile] = useState(null);

  const [thumbnailURL, setThumbnailURL] = useState('');
  const [videoURL, setVideoURL] = useState('');
  const [loading, setLoading] = useState(false); // Loading state

  const handleSaveChanges = async (e) => {
    e.preventDefault();
    setLoading(true); // Start loading
    const formData = new FormData(e.target);
    const token = localStorage.getItem('token'); // Retrieve the token

    try {
      const response = await fetch('http://localhost:3300/api/users/:id/videos', {
        method: 'POST',
        body: formData,
        headers: {
          'Authorization': `${token}` // Include the token in the header
        }
      });
      console.log("Response status:", response.status); // Log the response status
      if (response.ok) {
        const data = await response.json();
        console.log(data);
        addVideoToCurrentList(data); // Update the video list
        setLoading(false); // Stop loading
        document.querySelector('.btn-close').click(); // Close modal
      } else {
        const errorData = await response.json();
        console.log("Error uploading video", errorData);
        setLoading(false); // Stop loading
      }
    } catch (error) {
      console.error('Network error:', error);
      setLoading(false); // Stop loading
      console.log('Network error, please try again later');
    }
  };

  const handleVideoUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      const url = URL.createObjectURL(file);
      setVideoURL(url);
    }
  };

  const handleThumbnailChange = (e) => {
    const file = e.target.files[0];
    const reader = new FileReader();
    reader.onload = () => {
      setThumbnailURL(reader.result);
    };
    reader.readAsDataURL(file);
  }

  return (
    <>
      <div className={`modal-dialog modal-dialog-scrollable `}>
        <form className={`needs-validation`} onSubmit={handleSaveChanges}>
          <div className={`modal-content ${darkMode ? styles.formDarkMode : ''}`}>
            <div className={`modal-header ${darkMode ? styles.DarkModeHeader : ''}`}>
              <h1 className="modal-title fs-5" id="exampleModalLabel">Add New Video</h1>
              <button type="button" className={` ${darkMode ? "btn-close btn-close-white" : "btn-close"}`} data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div className="modal-body">

              <label className="form-label">Choose your video file:</label>
              <div className="input-group mb-2">
                <label className={`input-group-text ${darkMode ? styles.inputDarkMode : ''}`} htmlFor="inputGroupFile01">
                  <i className="bi bi-upload"></i>
                </label>
                <input
                  accept='video/*'
                  type="file"
                  className={`form-control ${darkMode ? styles.inputDarkMode : ''}`}
                  id="inputGroupFile01"
                  name="videoSrc"
                  onChange={handleVideoUpload}
                  required
                />
              </div>

              <label className="form-label">Choose your video thumbnail:</label>
              <div className="input-group mb-2">
                <label className={`input-group-text ${darkMode ? styles.inputDarkMode : ''}`} htmlFor="inputGroupFile02">
                  <i className="bi bi-file-image"></i>
                </label>
                <input
                  accept='.jpg, .jpeg, .png'
                  type="file"
                  className={`form-control ${darkMode ? styles.inputDarkMode : ''}`}
                  id="inputGroupFile02"
                  name="thumbnail"
                  onChange={handleThumbnailChange}
                  required
                />
              </div>

              <div className="input-group mb-2">
                <span className={`input-group-text ${darkMode ? styles.inputDarkMode : ''}`} id="basic-addon1">
                  <i className="bi bi-camera-video"></i>
                </span>
                <input
                  type="text"
                  className={`form-control ${darkMode ? styles.inputDarkMode : ''}`}
                  placeholder="Video Title"
                  aria-label="VideoTitle"
                  aria-describedby="basic-addon1"
                  name='title'
                  required
                />
              </div>


              <div className="input-group mb-2">
                <span className={`input-group-text ${darkMode ? styles.inputDarkMode : ''}`} id="basic-addon1">
                  <i className="bi bi-chat-right-quote"></i>
                </span>
                <input
                  type="text"
                  className={`form-control ${darkMode ? styles.inputDarkMode : ''}`}
                  placeholder="Description"
                  aria-label="Description"
                  aria-describedby="basic-addon1"
                  name='description'
                  required
                />
              </div>

              <div className="input-group mb-2">
                <span className={`input-group-text ${darkMode ? styles.inputDarkMode : ''}`} id="basic-addon1">
                  <i className="bi bi-tag"></i>
                </span>
                <select className={`custom-select form-control ${darkMode ? styles.inputDarkMode : ''}`} id="inputGroupSelect03" name='category'>
                  <option className={`${darkMode ? styles.inputDarkMode : ''}`} defaultValue>Choose...</option>
                  <option className={`${darkMode ? styles.inputDarkMode : ''}`} value="Sport">Sport</option>
                  <option className={`${darkMode ? styles.inputDarkMode : ''}`} value="Health">Health</option>
                  <option className={`${darkMode ? styles.inputDarkMode : ''}`} value="Nature">Nature</option>
                  <option className={`${darkMode ? styles.inputDarkMode : ''}`} value="Travel">Travel</option>
                  <option className={`${darkMode ? styles.inputDarkMode : ''}`} value="Sciemce">Sciemce</option>
                </select>
              </div>
            </div>
            <div className={`modal-footer ${darkMode ? styles.DarkModeFooter : ''}`}>
              <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
              <button type="submit" className="btn btn-primary">
                {loading ? <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> : 'Upload'}
              </button>
            </div>
          </div>
        </form>
      </div>

    </>
  );
}

export default AddVideoForm;