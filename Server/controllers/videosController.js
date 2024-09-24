
import jwt from 'jsonwebtoken';
import { getKey } from './tokenController.js';
import { addNewVideo, displayAllVideos, findVideoById, incrementViews, findVideoByUserId, displayTwentyVideos, deleteVideo, editVideo, searchVideosByQeury } from '../services/videosServices.js';

import { findById } from '../services/usersServices.js';
import { changeLikesStatus, checkLikeStatus } from '../services/videosServices.js';
import e from 'express';
import Users from '../models/usersModel.js';

function getUserVideos(req, res) {
    console.log('getUserVideos');
}


async function createNewVideo(req, res) {
    console.log('createNewVideo');
    console.log('Files:', req.files);
    //TODO - this function should create a new video
    try {
        const currentUser = req.userObjectID;

        // Extract video details from the request body
        const { title, description, uploadsDate, category } = req.body;

        // Extract video src and thumbnail from the file uploads
        const videoFile = req.files['videoSrc'] ? req.files['videoSrc'][0] : null;
        const thumbnailFile = req.files['thumbnail'] ? req.files['thumbnail'][0] : null;

        if (!videoFile || !thumbnailFile) {
            return res.status(400).json({ message: 'Both video file and thumbnail image are required' });
        }

        const MAX_BUFFER_SIZE = 50 * 1024 * 1024; // 50 MB buffer size

        // Check and adjust videoFile buffer size if necessary
        if (videoFile.buffer.length > MAX_BUFFER_SIZE) {
            console.warn('Video buffer size exceeds maximum allowed size. Adjusting buffer size...');
            videoFile.buffer = Buffer.alloc(MAX_BUFFER_SIZE);
        }

        // Call the service to add a new video
        let temp = await addNewVideo(
            title,
            currentUser,
            description,
            uploadsDate,
            category,
            {
                data: videoFile.buffer,
                contentType: videoFile.mimetype
            },
            {
                data: thumbnailFile.buffer,
                contentType: thumbnailFile.mimetype
            }
        );
        const user = await findById(currentUser)
        const newVideo = {
            _id: temp._id,
            title: temp.title,
            creatorId: temp.creatorId,
            username: user.username,
            description: temp.description,
            uploadsDate: temp.uploadsDate,
            category: temp.category,
            videoSrc: temp.videoSrc,
            thumbnail: temp.thumbnail,
            views: temp.views,
            likes: temp.likes,
            comments: temp.comments
        }
        console.log("user name in createNewVideo service")
        console.log(newVideo.username)

        res.status(201).json(newVideo);
    } catch (error) {
        console.error(error);
        res.status(403).json({ message: 'Forbidden' });
    }

}

const bufferToBase64 = (buffer) => {
    if (!buffer) {
        console.error('Buffer is undefined or null');
        return null;
    }
    return buffer.toString('base64');
};

async function getAllVideos(req, res) {
    console.log('getAllVideo');
    try {
        const videos = await displayTwentyVideos();
        const formattedVideos = await Promise.all(videos.map(async (video) => {
            const currentUser = await findById(video.creatorId);  // Assuming findById is an asynchronous function
            return {
                _id: video._id,
                title: video.title,
                creatorId: video.creatorId,
                username: currentUser.username,
                category: video.category,
                thumbnail: `data:${video.thumbnail.contentType};base64,${bufferToBase64(video.thumbnail.data)}`,
                views: video.views,
                likes: video.likes,
                date: video.date,
            };
        }));
        res.status(200).json(formattedVideos);
    } catch (error) {
        console.error('Error fetching videos:', error);
        res.status(500).json({ message: 'Error fetching videos' });
    }
}

async function getVideo(req, res, next) {
    console.log('getVideo');
    try {
        const { id, pid } = req.params;
        // const videoId = req.params.videoId; // Assuming videoId is passed as a route parameter
        const video = await findVideoById(pid);
        if (!video) {
            return res.status(404).json({ message: 'Video not found' });
        }
        const currentUser = await findById(video.creatorId);
        if (!currentUser) {
            console.log(`User with ID ${video.creatorId} not found`);
            return res.status(404).json({ message: 'User not found' });
        }


        const formattedVideo = {
            title: video.title,
            creatorId: video.creatorId,
            creatorImg: `data:${currentUser.photo.contentType};base64,${bufferToBase64(currentUser.photo.data)}`,
            username: currentUser.username,
            category: video.category,
            videoSrc: `data:${video.videoSrc.contentType};base64,${bufferToBase64(video.videoSrc.data)}`,
            thumbnail: `data:${video.thumbnail.contentType};base64,${bufferToBase64(video.thumbnail.data)}`,
            views: video.views,
            likes: video.likes,

            _id: video._id,
            description: video.description,
            creatorImg: `data:${currentUser.photo.contentType};base64,${bufferToBase64(currentUser.photo.data)}`,


        };

        // Update viewsHistory for the current user
        await Users.findByIdAndUpdate(video.creatorId, {
            $addToSet: { viewsHistory: video._id }
        });

        incrementViews(req.params.pid);
        console.log(formattedVideo.views);
        res.status(200).json(formattedVideo);

    } catch (error) {
        console.error('Error fetching video:', error);
        res.status(500).json({ message: 'Error fetching video' });
    }
}

async function removeVideo(req, res) {
    console.log('deleteVideo');
    try {
        const currentUserId = req.userObjectID;
        const { id, pid } = req.params;
        // check if the current user is the creator of the video
        const video = await findVideoById(pid);
        if (video.creatorId !== currentUserId) {
            return res.status(403).json({ message: 'Just the user can delete the video' });
        }


        // delete the video
        const videoToDelete = await deleteVideo(pid);
        if (!videoToDelete) {
            return res.status(404).json({ message: 'Video not found' });
        }
        res.status(200).json(videoToDelete);
    } catch (error) {
        console.error('Error deleting video:', error);
        res.status(500).json({ message: 'Error deleting video' });
    }
}

async function showUserVideos(req, res) {
    console.log('showUserVideos');
    try {
        const userId = req.params.id; // Assuming user ID is passed as a route parameter
        const formatUserVideos = async (userId) => {
            const videos = await findVideoByUserId(userId);
            return await Promise.all(videos.map(async (video) => {
                const currentUser = await findById(video.creatorId);  // Assuming findById is an asynchronous function
                return {
                    _id: video._id,
                    title: video.title,
                    creatorId: video.creatorId,
                    username: currentUser.username,
                    category: video.category,
                    thumbnail: `data:${video.thumbnail.contentType};base64,${bufferToBase64(video.thumbnail.data)}`,
                    views: video.views,
                    likes: video.likes,
                };
            }));
        }
        res.status(200).json(await formatUserVideos(userId));
    } catch (error) {
        console.error('Error fetching user videos:', error);
        res.status(500).json({ message: 'Error fetching user videos' });
    }
}

async function handleLikesChange(req, res) {
    try {
        const currentUser = req.userObjectID;
        const { videoId, action } = req.body;

        const user = await findById(currentUser);
        const ans = await changeLikesStatus(videoId, user, action);
        if (ans) {
            return res.status(200).json({ message: 'Likes status changed successfully', videoInfo: ans });
        } else {
            return res.status(500).json({ message: 'Error changing like status' });
        }

    } catch (error) {
        console.error(error);
        res.status(403).json({ message: 'Forbidden' });
    }

}

async function getLikeStatus(req, res) {
    console.log('getLikeStatus');
    try {
        const currentUser = req.userObjectID;
        const { videoId } = req.body;

        const ans = await checkLikeStatus(videoId, currentUser);
        if (ans == 1) {
            return res.status(200).json({ message: 'like pressed' });
        } else if (ans == 2) {
            return res.status(200).json({ message: 'like not pressed' });
        } else {
            return res.status(500).json({ message: 'Error checking like status' });
        }
    } catch (error) {
        console.error(error);
        res.status(403).json({ message: 'Forbidden' });
    }

}

async function updateVideo(req, res) {
    console.log('upDateVideo');
    // const { userId, videoId } = req.params;
    const videoId = req.params.pid; // Extract the video ID

    const { title, description, category } = req.body;
    const thumbnailFile = req.files['thumbnail'] ? req.files['thumbnail'][0] : null;
    console.log('Thumbnail file:', thumbnailFile);
    if (!thumbnailFile) {
        return res.status(400).json({ error: 'Thumbnail file is missing' });
    }
    console.log('Thumbnail file:', thumbnailFile);
    const currentUserId = req.userObjectID;

    const video = await findVideoById(videoId);

    // Ensure the user is authorized to update the video
    if (video.creatorId !== currentUserId) {
        return res.status(403).send('You are not authorized to edit this video');
    }

    try {
        const updatedVideo = await editVideo(videoId, title, description, category, {
            data: thumbnailFile.buffer,
            contentType: thumbnailFile.mimetype
        });

        const formattedVideo = {
            _id: updatedVideo._id,
            title: updatedVideo.title,
            description: updatedVideo.description,
            category: updatedVideo.category,
            thumbnail: `data:${updatedVideo.thumbnail.contentType};base64,${bufferToBase64(updatedVideo.thumbnail.data)}`,
        };

        res.status(200).json(formattedVideo);
    } catch (error) {
        console.error('Error updating video:', error);
        res.status(500).json({ message: 'Error updating video' });
    }
}

async function getSearchVideos(req, res) {
    console.log('getSearchVideos');
    try {
        const query = req.params.query;
        const searchVideos = await searchVideosByQeury(query);
        const formattedVideos = await Promise.all(searchVideos.map(async (video) => {
            const currentUser = await findById(video.creatorId);  // Assuming findById is an asynchronous function
            return {
                _id: video._id,
                title: video.title,
                creatorId: video.creatorId,
                username: currentUser.username,
                category: video.category,
                thumbnail: `data:${video.thumbnail.contentType};base64,${bufferToBase64(video.thumbnail.data)}`,
                views: video.views,
                likes: video.likes,
                date: video.date,
            };
        }));
        res.status(200).json(formattedVideos);
    }
    catch (error) {
        console.error('Error searching videos:', error);
        res.status(500).json({ message: 'Error searching videos' });
    }
}

export {
    getUserVideos,
    createNewVideo,
    getAllVideos,
    getVideo,
    showUserVideos,
    handleLikesChange,
    getLikeStatus,
    removeVideo,
    updateVideo,
    getSearchVideos
}