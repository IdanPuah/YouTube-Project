import Videos from '../models/videosModel.js';
import Users from '../models/usersModel.js';

const addNewVideo = async (title, creatorId, description, uploadsDate, category, videoSrc, thumbnail) => {
    const newVideo = new Videos({
        title: title,
        creatorId: creatorId,
        description: description,
        uploadsDate: uploadsDate,
        category: category,
        videoSrc: {
            data: videoSrc.data,
            contentType: videoSrc.contentType
        },
        thumbnail: {
            data: thumbnail.data,
            contentType: thumbnail.contentType
        }
    });
    return await newVideo.save();
}

const displayAllVideos = async () => {
    try {
        const videos = await Videos.find().select('-videoSrc');
        return videos;
    } catch (error) {
        throw new Error('Error fetching videos'); a
    }
}


const displayTwentyVideos = async () => {
    try {
        // Step 1: Fetch the 10 most viewed videos
        const mostViewedVideos = await Videos.find()
            .sort({ views: -1 }) // Sort by views in descending order
            .limit(10) // Limit to the top 10 most viewed videos
            .select('-videoSrc'); // Exclude videoSrc from the result

        // Step 2: Fetch remaining videos if the total number of videos is less than 20
        const mostViewedVideoIds = mostViewedVideos.map(video => video._id);

        // Count all videos excluding the most viewed ones
        const remainingVideosCount = await Videos.countDocuments({ _id: { $nin: mostViewedVideoIds } });

        // Calculate the number of remaining videos needed to make up 20
        const remainingVideosNeeded = Math.max(0, 20 - mostViewedVideos.length);

        // Fetch the remaining videos randomly if available
        let randomVideos = [];
        if (remainingVideosCount > 0) {
            // Calculate random skip value for random videos if enough videos are available
            const randomSkip = Math.max(0, Math.floor(Math.random() * (remainingVideosCount - remainingVideosNeeded)));

            randomVideos = await Videos.find({ _id: { $nin: mostViewedVideoIds } })
                .skip(randomSkip)
                .limit(remainingVideosNeeded)
                .select('-videoSrc');
        }

        // Combine the results
        const combinedVideos = [...mostViewedVideos, ...randomVideos];

        return combinedVideos;
    } catch (error) {
        console.error('Error fetching videos:', error);
        throw new Error('Error fetching videos');
    }
}



const findVideoById = async (videoId) => {
    try {
        return await Videos.findById(videoId);
    } catch (error) {
        throw new Error('Error fetching video');
    }
}

const findVideoByUserId = async (userId) => {
    try {
        return await Videos.find({ creatorId: userId });
    } catch (error) {
        throw new Error('Error fetching video');
    }
}

const incrementViews = async (videoId) => {
    try {
        const video = await Videos.findById(videoId);

        if (!video) {
            throw new Error('Video not found');
        }

        video.views += 1;
        await video.save();

        return video;
    } catch (error) {
        throw new Error(error.message);
    }
};

const deleteCommentFromPost = async (postId, commentId) => {
    try {
        // Find the post by ID and update it
        const video = await Videos.findById(postId);
        if (!video) {
            console.log('Post not found');
            return;
        }

        // Remove the comment from the comments array
        video.comments.pull(commentId);
        await video.save();
        console.log('Comment deleted successfully');
        return true;
    } catch (error) {
        console.error('Error deleting comment:', error);
        return false;
    }
};

const changeLikesStatus = async (videoId, user, action) => {
    try {
        // Find the post by ID and update it
        const video = await Videos.findById(videoId);
        if (!video) {
            console.log('Video not found');
            return false;
        }

        if (action === 'add') {
            video.likedBy.push(user);
            video.likes += 1;
            await video.save();
            console.log('Like added successfully');
            return video;
        } else if (action === 'remove') {
            video.likedBy.pull(user);
            video.likes -= 1;
            await video.save();
            console.log('Like removed successfully');
            return video;
        } else {
            console.log('Invalid action');
            return false;
        }
    } catch (error) {
        console.error('Error deleting comment:', error);
        return false;
    }
};

const checkLikeStatus = async (videoId, userId) => {
    try {
        const video = await Videos.findById(videoId);
        if (!video) {
            console.log('Video not found');
            return false;
        }
        const user = await Users.findById(userId);
        if (!user) {
            console.log('User not found');
            return false;
        }
        const ans = video.likedBy.some(likedById => likedById.toString() === userId);
        if (ans) {
            console.log('Like pressed');
            return 1;
        } else {
            console.log('Like unpressed');
            return 2;
        }
    } catch (error) {
        console.error('Error checking like status:', error);
        return -1;
    }
}
const deleteVideo = async (videoId) => {
    try {
        const video = await findVideoById(videoId);
        if (!video) {
            throw new Error('Video not found');
        }
        await Videos.deleteOne({ _id: videoId });
        return video;
    } catch (error) {
        throw new Error(`Error deleting video: ${error.message}`);
    }
};

const editVideo = async(videoId, title, description, category, thumbnail) => {
    try {
        const video = await Videos.findById(videoId);
        if (!video) {
            throw new Error('Video not found');
        }
        video.title = title;
        video.description = description;
        video.category = category;
        video.thumbnail = {
            data: thumbnail.data,
            contentType: thumbnail.contentType
        };
        await video.save();
        return video;
    } catch (error) {
        throw new Error(`Error editing video: ${error.message}`);

    }

}

const searchVideosByQeury = async (query) => {
    try {
        return await Videos.find({ title: { $regex: query, $options: 'i' } });
    } catch (error) {
        throw new Error('Error fetching videos');
    }
}

export {
    addNewVideo,
    displayAllVideos,
    findVideoById,
    incrementViews,
    findVideoByUserId,
    displayTwentyVideos,
    deleteCommentFromPost,
    changeLikesStatus,
    checkLikeStatus,
    deleteVideo,
    editVideo,
    searchVideosByQeury
}