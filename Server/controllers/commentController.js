import mongoose from 'mongoose';
import { findVideoById, deleteCommentFromPost } from '../services/videosServices.js';
import { findById } from '../services/usersServices.js';
import { addNewComment, getBlockOfComments,deleteUserComment, findCommentById, editCommentText } from '../services/commentsServices.js';
import User from '../models/usersModel.js'; 
import express from 'express';

async function addComment(req, res) {
    console.log('addComment');
    
    try {
        const currentUserId = req.userObjectID;
        const { videoId, comment } = req.body;

        const video = await findVideoById(videoId);
        if (!video) {
            return res.status(404).json({ message: 'Video not found' });
        }

        const user = await findById(currentUserId);
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        } 
        const newComment = await addNewComment(video, user, comment, videoId);
        if (newComment) {
            return res.status(200).json({ message: 'Comment added successfully', newComment });
        } else {
            return res.status(500).json({ message: 'Error adding comment' });
        }
    } catch (error) {
        console.error('Error adding comment:', error);
        res.status(500).json({ message: 'Error adding comment' });
    }
}

async function getLatestComments(req, res) {
    console.log('getLatestComments');
    const { videoId } = req.body;

    if (!videoId) {
        return res.status(400).json({ message: 'Video ID is required' });
    }

    const video = await findVideoById(videoId);
    if (!video) {
        return res.status(404).json({ message: 'Video not found' });
    }

    try {
        const comments = await getBlockOfComments(video);

        if (comments && comments.length > 0) {
            // Fetch the user images for each comment
            const userIds = comments.map(comment => comment.userId);
            const users = await User.find({ _id: { $in: userIds } }, 'photo');

            // Create a map of userId to profileImg for easy access
            const userImgMap = {};
            users.forEach(user => {
                userImgMap[user._id] = user.photo ? `data:${user.photo.contentType};base64,${user.photo.data.toString('base64')}` : null;
            });

            // Attach the profileImg to each comment
            const commentsWithUserImg = comments.map(comment => ({
                ...comment._doc, // Spread the existing comment fields
                userImg: userImgMap[comment.userId] || null // Add userImg field
            }));

            // console.log(commentsWithUserImg);
            return res.status(200).json({ message: 'Comments fetched successfully', comments: commentsWithUserImg });
        } else {
            return res.status(200).json({ message: 'No comments found', comments: [] });
        }
    } catch (error) {
        console.error('Error fetching comments:', error);
        return res.status(500).json({ message: 'Error fetching comments' });
    }
}

// async function getLatestComments(req, res) {
//     console.log('getLatestComments');
//     const { videoId } = req.body;

//     if (!videoId) {
//         return res.status(400).json({ message: 'Video ID is required' });
//     }

//     const video = await findVideoById(videoId);
//     if (!video) {
//         return res.status(404).json({ message: 'Video not found' });
//     }

//     try {
//         const comments = await getBlockOfComments(video);
//         if (comments) {
//             console.log(comments);
//             return res.status(200).json({ message: 'Comments fetched successfully', comments });
//         } else {
//             return res.status(500).json({ message: 'Error fetching comments' });
//         }
//     } catch (error) {
//         console.error('Error fetching comments:', error);
//         return res.status(500).json({ message: 'Error fetching comments' });
//     }
// }

async function deleteComment(req, res) {
    console.log('deleteComment');
    const currentUserId = req.userObjectID;
    const { commentId } = req.query;
    if (!commentId) {
        return res.status(400).json({ message: 'Comment ID is required' });
    }

    try {
        console.log('comment id: ' + commentId);
        const comment = await findCommentById(commentId);
        if (!comment) {
            return res.status(404).json({ message: 'Comment not found' });
        }
        const userIdAsObjectId = new mongoose.Types.ObjectId(currentUserId);
        if (!comment.userId.equals(userIdAsObjectId)) {
            return res.status(403).json({ message: 'You are not allowed to delete this comment' });
        }
        console.log('comment video id: ' + comment.videoId);
        const video = await findVideoById(comment.videoId);
        if (!video) {
            return res.status(404).json({ message: 'Video not found' });
        }
        if(!await deleteCommentFromPost(video._id, commentId)){
            return res.status(500).json({ message: 'Error deleting comment' });
        }
        if (deleteUserComment(commentId)) {
            return res.status(200).json({ message: 'Comment deleted successfully'});
        } else {
            return res.status(500).json({ message: 'Error deleting comment' });
        }
    } catch (error) {
        console.error('Error deleting comment:', error);
        return res.status(500).json({ message: 'Error deleting comment' });
    }
}

async function editComment(req, res) {
    console.log('editComment');
    try {
        const currentUserId = req.userObjectID;
        const {commentId , newCommentText } = req.body;
        console.log('commentId: ' + commentId);
        console.log('newCommentText: ' + newCommentText);
        if (!newCommentText) {
            return res.status(400).json({ message: 'Comment text is required' });
        }
        try {
            const comment = await findCommentById(commentId);
            if (!comment) {
                return res.status(404).json({ message: 'Comment not found' });
            }
            const userIdAsObjectId = new mongoose.Types.ObjectId(currentUserId);
            if (!comment.userId.equals(userIdAsObjectId)) {
                return res.status(403).json({ message: 'You are not allowed to edit this comment' });
            }
            const editedComment = await editCommentText(commentId, newCommentText);
            return res.status(200).json({ message: 'Comment edited successfully', editedComment });
            
        } catch (error) {
            console.error('Error deleting comment:', error);
            return res.status(500).json({ message: 'Error deleting comment' });
        }
    } catch (error) {
        console.error('Error adding comment:', error);
        res.status(500).json({ message: 'Error adding comment' });
    }
}



export { addComment , getLatestComments, deleteComment, editComment};