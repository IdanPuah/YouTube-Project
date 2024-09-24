//import * as videos from './videosServices'; 
import e from 'express';
import Comment from '../models/commentModel.js'; 
import { findVideoById } from './videosServices.js';
//import * as Users from './usersServices'; 


const addNewComment = async (videos, user, comment, videoId) => {
    try {
        const newComment = new Comment({
            userId: user._id,
            username: user.username,
            comment: comment,
            videoId: videoId,
        });

        await newComment.save(); // Save the comment

        videos.comments.push(newComment._id); // Add the comment ID to the video's comments array
        await videos.save(); // Save the video document
        return newComment; // Return the newly created comment
    } catch (error) {
        console.error('Error adding comment:', error);
        throw new Error('Error adding comment');
    }
}

// Get the latest 20 comments for a video
const getBlockOfComments = async (video) => {
    try {
        const tempVideo = await video.populate({
            path: 'comments',
            options: {
                sort: { date: -1 }, // Sort by date in descending order
                limit: 20
            }
        });

        if (!tempVideo) {
            return null;
        }
        return tempVideo.comments;
    } catch (error) {
        console.error('Error fetching comments:', error);
        return null;
    }
}

// Delete a comment return true if successful, false otherwise
const deleteUserComment = async (commentId) => {
    console.log('deleteUserComment');
    try {
        const comment = await findCommentById(commentId); // Call findById on the User model
        if (comment) {
            await Comment.deleteOne({ _id: commentId }); // Use filter object with deleteOne
            return true;
        }
        return false; // Return false if user is not found
    } catch (error) {
        console.error('Error deleting user:', error);
        return false; // Return false in case of error
    }
}


const findCommentById = async (commentId) => {
    return await Comment.findOne({ _id: commentId });
}

const editCommentText = async (commentId, newComment) => {
    try {
        const comment = await findCommentById(commentId);
        if (comment) {
            comment.comment = newComment;
            await comment.save();
            return comment;
        }
        return false;
    } catch (error) {
        console.error('Error editing comment:', error);
        return false;
    }
}





export { addNewComment, getBlockOfComments , deleteUserComment, findCommentById, editCommentText };


