import mongoose from 'mongoose';

const videoSchema = new mongoose.Schema({
    title: {
        type: String,
        required: true,
        unique: true
    },
    creatorId: {
        type: String
    },
    description: {
        type: String,
        required: true
    },
    uploadsDate: {
        type: Date
    },
    category: {
        type: String,
        required: true
    },
    videoSrc: {
        data: Buffer,
        contentType: String
    },
    thumbnail: {
        data: Buffer,
        contentType: String
    },
    views: {
        type: Number,
        default: 0
    },
    comments: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Comment'
    }],
    likes: {
        type: Number,
        default: 0
    },
    likedBy: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    }],
    date: {
        type: Date,
        default: Date.now
    }
});

const Videos = mongoose.model('Videos', videoSchema);
export default Videos;

