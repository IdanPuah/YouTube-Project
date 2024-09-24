
import React, { useState } from 'react';
import styles from './EditVideo.module.css';


function EditVideo({ currentUser, handleEditVideo, darkMode, video, editVideo }) {
    console.log('edit video');
    console.log(video);
    const [newThumbnailURL, setnewThumbnailURL] = useState('');
    const [newTitle, setNewTitle] = useState("");
    const [newDescription, setNewDescription] = useState("");
    const [newCategory, setNewCategory] = useState("");

    const handleSaveChanges = (e) => {
        console.log('save changes');

        e.preventDefault();
        // setLoading(true);
        const formData = new FormData(e.target);
        editVideo(formData);
    }

    const handleThumbnailChange = (e) => {
        const file = e.target.files[0];
        const reader = new FileReader();
        reader.onload = () => {
            setnewThumbnailURL(reader.result);
        };
        reader.readAsDataURL(file);
    }

    const handleTitleChange = (e) => {
        setNewTitle(e.target.value);
    };

    const handleDescriptionChange = (e) => {
        setNewDescription(e.target.value);
    };

    const handleCategoryChange = (e) => {
        setNewCategory(e.target.value);
    };

    return (
        <>
            <div className={`modal-dialog modal-dialog-scrollable `}>
                <form className={`needs-validation`} onSubmit={handleSaveChanges}>
                    <div className={`modal-content ${darkMode ? styles.formDarkMode : ''}`}>
                        <div className={`modal-header ${darkMode ? styles.DarkModeHeader : ''}`}>
                            <h1 className="modal-title fs-5" id="exampleModalLabel">Edit Video</h1>
                            <button type="button" className={` ${darkMode ? "btn-close btn-close-white" : "btn-close"}`} data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <label className="form-label">Choose your video thumbnail:</label>
                            <div className="input-group mb-2">
                                <label className={`input-group-text ${darkMode ? styles.inputDarkMode : ''}`} htmlFor="inputGroupFile02">
                                    <i className="bi bi-file-image"></i>
                                </label>
                                <input
                                    accept='.jpg, .jpeg, .png'
                                    name="thumbnail"
                                    type="file"
                                    className={`form-control ${darkMode ? styles.inputDarkMode : ''}`}
                                    id="inputGroupFile02"
                                    onChange={handleThumbnailChange}
                                    required
                                />
                            </div>

                            <div className="input-group mb-2">
                                <span className={`input-group-text ${darkMode ? styles.inputDarkMode : ''}`} id="basic-addon1">
                                    <i className="bi bi-camera-video"></i>
                                </span>
                                <input
                                    defaultValue={newTitle}
                                    type="text"
                                    className={`form-control ${darkMode ? styles.inputDarkMode : ''}`}
                                    placeholder="Video Title"
                                    aria-label="VideoTitle"
                                    onChange={handleTitleChange}
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
                                    defaultValue={newDescription}
                                    type="text"
                                    className={`form-control ${darkMode ? styles.inputDarkMode : ''}`}
                                    placeholder="Description"
                                    onChange={handleDescriptionChange}
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
                                <select value={newCategory} className={`custom-select form-control ${darkMode ? styles.inputDarkMode : ''}`} id="inputGroupSelect03" name='category' onChange={handleCategoryChange}>
                                    <option className={`${darkMode ? styles.inputDarkMode : ''}`} value>Choose...</option>
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
                            <button type="submit" className="btn btn-primary" data-bs-dismiss="modal">Upload</button>
                        </div>
                    </div>
                </form>
            </div>

        </>
    );
}

export default EditVideo;