import styles from './Comment.module.css';
import { useState } from "react"

function Comment({ darkMode, deleteCurrCmt, setCurrentComment,/* comment, id, deleteCurrCmt, setCurrentComment*/ comment, currentUser }) {

    const [isEditing, setIsEditing] = useState(false);
    const [commentText, setCommentText] = useState(comment.comment);

    // const canDelete = currentUser._id === comment.userId;
    const canDelete = currentUser ? currentUser._id === comment.userId : false;


    //console.log(comment);
    function deleteComment() {
        //console.log("deleteComment");
        deleteCurrCmt(comment._id);
    }

    const editComment = () => {
        //console.log("editComment");
        setIsEditing(true);
    };

    const saveComment = () => {
        //console.log("saveComment");
        const newComment = document.getElementById('edit-input').value;

        const editComment = async () => {
            const token = localStorage.getItem('token');
            const commentData = {
                commentId: comment._id,
                newCommentText: newComment
            };
            try {
                const response = await fetch(`http://localhost:3300/editComment`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `${token}`
                    },
                    body: JSON.stringify(commentData)
                });
                if (response.ok) {
                    const data = await response.json();
                    setCommentText(data.editedComment.comment);
                    //console.log('Comment edit successfully:', data);
                } else {
                    const errorData = await response.json();
                    //console.log('Error edit comment:', errorData);
                }
            } catch (error) {
                console.error('Network error:', error);
            }
        };

        editComment();
        setIsEditing(false);
    };

    const cancelEdit = () => {
        //console.log("cancelEdit");
        setIsEditing(false);
    };

    const getTimeAgo = (date) => {
        const currentDate = new Date();
        const commentDate = new Date(date);
        const timeDifference = currentDate - commentDate;

        const daysAgo = Math.floor(timeDifference / (1000 * 60 * 60 * 24));
        const monthsAgo = Math.floor(daysAgo / 30);
        const yearsAgo = Math.floor(daysAgo / 365);

        if (daysAgo === 0) {
            return 'today';
        } else if (daysAgo < 30) {
            return `${daysAgo} days ago`;
        } else if (daysAgo < 365) {
            return `${monthsAgo} month${monthsAgo > 1 ? 's' : ''} ago`;
        } else {
            return `${yearsAgo} year${yearsAgo > 1 ? 's' : ''} ago`;
        }
    };
    return (
        <div className={`container-fluid d-flex ${styles.comment} ${darkMode ? styles.darkMode : ''}`}>
            <div className='col d-flex align-items-center'>
                {isEditing ? (
                    <input
                        id='edit-input'
                        className={`form-control form-control-sm ${styles.input}`}
                        defaultValue={commentText}
                    />
                ) : (
                    <div className={styles.commentContainer}>
                        <h5 className={styles.text}>
                            {comment.username} <span className={styles.dateText}>• {getTimeAgo(comment.date)}</span>
                        </h5>
                        <p className={styles.text}>{commentText}</p>
                    </div>
                )}
            </div>
            <div className='col d-flex justify-content-end'>
                {isEditing ? (
                    <>
                        <button
                            variant='outline-secondary'
                            onClick={saveComment}
                            className={`btn btn-outline-secondary ${styles.commentBtn} ${styles.MyBtn}`}
                        >
                            Save
                        </button>
                        <button
                            variant='outline-danger'
                            onClick={cancelEdit}
                            className={`btn btn-outline-danger ${styles.commentBtn} ${styles.MyBtn}`}
                        >
                            Cancel
                        </button>
                    </>
                ) : (
                    <>
                        {canDelete && (
                            <>
                                <button
                                    type='button'
                                    className={`btn btn-outline-secondary ${styles.commentBtn} ${styles.MyBtn}`}
                                    onClick={editComment}
                                >
                                    <i className='bi bi-pencil-square'></i>
                                </button>
                                <button
                                    type='button'
                                    className={`btn btn-outline-danger ${styles.commentBtn} ${styles.MyBtn}`}
                                    onClick={deleteComment}
                                >
                                    <i className='bi bi-trash'></i>
                                </button>
                            </>
                        )}
                    </>
                )}
            </div>
        </div>
    )
}

export default Comment;