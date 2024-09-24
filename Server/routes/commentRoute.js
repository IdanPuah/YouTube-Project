import express from 'express';
import { authenticateToken } from '../controllers/tokenController.js';
import { addComment, getLatestComments, deleteComment , editComment} from '../controllers/commentController.js';


const router = express.Router();

router.post('/addComment', authenticateToken ,  addComment);
router.post('/getLatestComments', getLatestComments);
router.delete('/deleteComment', authenticateToken , deleteComment);
router.post('/editComment', authenticateToken , editComment);//TO-DO

export default router;