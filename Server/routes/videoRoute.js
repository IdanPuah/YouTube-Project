import express from 'express';
import multer from 'multer';

import {createNewVideo, getAllVideos, getVideo, showUserVideos, removeVideo, updateVideo, handleLikesChange, getLikeStatus, getSearchVideos} from '../controllers/videosController.js';
import { authenticateToken } from '../controllers/tokenController.js';
import { get } from 'mongoose';


const storage = multer.memoryStorage();
const upload = multer({ storage: storage }).fields([
    { name: 'videoSrc', maxCount: 1 },
    { name: 'thumbnail', maxCount: 1 }
]);

const router = express.Router();

router.get('/api/videos', getAllVideos);//TO-DO - need to return only 20 videos
router.post('/api/users/:id/videos',authenticateToken, upload, createNewVideo);//DONE
router.get('/api/users/:id/videos',showUserVideos);
router.get('/api/users/:id/videos/:pid', getVideo);//DONE


router.post('/api/videos/handleLikesChange', authenticateToken, handleLikesChange);
router.post('/api/videos/getLikeStatus', authenticateToken, getLikeStatus);

router.delete('/api/users/:id/videos/:pid', authenticateToken , removeVideo);
router.put('/api/users/:id/videos/:pid',authenticateToken,upload, updateVideo);

router.get('/api/videos/:query', getSearchVideos);


export default router;