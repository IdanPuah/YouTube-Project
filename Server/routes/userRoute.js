import express from 'express';
import multer from 'multer';
import { getUserInfo, deleteUser, updateUser, createNewUser } from '../controllers/usersController.js';
import { getUserVideos, createNewVideo } from '../controllers/videosController.js';
import { authenticateToken } from '../controllers/tokenController.js';


// Configure multer storage
const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

const router = express.Router();
//API section 1
router.get('/api/users/:id', getUserInfo); //DONE
router.delete('/api/users/:id',authenticateToken, deleteUser); //DONE
router.put('/api/users/:id',authenticateToken, updateUser); //DONE


//API section 2
//router.post('/api/users/:id/videos', createNewVideo);

//API section 3
router.post('/api/users', upload.single('photo'), createNewUser);//DONE

export default router;



