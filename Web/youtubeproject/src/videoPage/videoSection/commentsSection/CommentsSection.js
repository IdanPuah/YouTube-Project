import styles from "./CommentsSection.module.css";

import Comment from "./comment/Comment";
import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
function CommentsSection({ darkMode, videoinfo, currentUser }) {
  const navigate = useNavigate();
  const [comments, setComments] = useState([]);

  const addComment = async () => {
    const newComment = document.getElementById("newCommentInput").value;
    document.getElementById("newCommentInput").value = "";

    const videoId = videoinfo._id; // Replace with the actual video ID
    const token = localStorage.getItem("token");

    const commentData = {
      videoId: videoId,
      comment: newComment,
    };

    try {
      const response = await fetch(`http://localhost:3300/addComment`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `${token}`,
        },
        body: JSON.stringify(commentData),
      });
      if (response.status === 403) {
        // Redirect to the sign-in page if the response status is 403
        navigate("/signIn");
      }
      if (response.ok) {
        const data = await response.json();
        //console.log("Comment added successfully:", data);
        const newCommentData = {
          comment: data.newComment.comment,
          _id: data.newComment._id,
          userId: currentUser._id,
        };
        setComments((prevComments) => [data.newComment, ...prevComments,]);
      } else {
        const errorData = await response.json();
        console.log("Error adding comment:", errorData);
      }
    } catch (error) {
      console.error("Network error:", error);
    }
  };

  // useEffect to fetch video comments on page load
  useEffect(() => {
    const fetchComments = async () => {
      const videoId = videoinfo._id; // Replace with the actual video ID
      const token = localStorage.getItem("token");

      const commentData = {
        videoId: videoId,
      };

      try {
        const response = await fetch(`http://localhost:3300/getLatestComments`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${token}`
          },
          body: JSON.stringify(commentData)
        });


        if (response.ok) {
          const data = await response.json();
          setComments(data.comments); // Assuming the server returns an array of comments
        } else {
          const errorData = await response.json();
          console.error("Error fetching comments:", errorData);
        }
      } catch (error) {
        console.error("Network error:", error);
      }
    };

    fetchComments();
  }, [videoinfo._id]); // Empty dependency array means this runs once on component mount

  const deleteComment = function (id) {
    //setComments(comments.filter((comment) => comment.id !== id));
    const deleteComments = async () => {
      const token = localStorage.getItem('token');
      const commentData = {
        commentId: id,
      };
      try {
        const response = await fetch(`http://localhost:3300/deleteComment?commentId=${encodeURIComponent(id)}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${token}`
          },
          // body: JSON.stringify(commentData)
        });

        if (response.ok) {
          const data = await response.json();
          //console.log('Comment has been deleted successfully:', data);
          setComments(comments.filter((comment) => comment._id !== id));
        } else {
          const errorData = await response.json();
          console.log('Error deleting comment:', errorData);
        }
      } catch (error) {
        console.error('Network error:', error);
      }
    };

    // Call the function to test it
    deleteComments();

  };


  useEffect(() => {
    //console.log(comments);
  }, [comments]);

  let commentsList = comments.slice().reverse().map((comment, key) => {
    return (
      <Comment
        key={key}
        darkMode={darkMode}
        deleteCurrCmt={deleteComment}
        /*
        comment={comment.comment}
        id={comment.id}
        
        key={key}
        setCurrentComment={setCurrentComment}
        */
        comment={comment}
        currentUser={currentUser}
      />
    );
  });

  return (
    <div>
      <div className="row">
        <h1 className={`${darkMode ? styles.darkModeText : ""}`}>Comments</h1>
        <div className="col-10">
          <input
            className={`form-control ${styles.newCommentText} ${darkMode ? styles.darkMode : ""
              }`}
            id="newCommentInput"
          ></input>
        </div>
        <div className="col-2 d-flex align-items-end">
          <button
            id="SubmitButton"
            type="submit"
            className={`btn btn-primary ${styles.submitButton} ${styles.MyBtn}`}
            onClick={addComment}
          >
            Submit
          </button>
        </div>
      </div>
      <div
        id="all-comments"
        className={`row flex-column-reverse ${styles.allComments}`}
      >
        {commentsList}
      </div>
    </div>
  );
}

export default CommentsSection;
