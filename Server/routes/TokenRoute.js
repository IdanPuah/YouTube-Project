import express from 'express';
//import { checkToken } from '../controllers/tokenController.js';
import { returnUserInfoByToken } from '../controllers/usersController.js';
import { authenticateToken } from '../controllers/tokenController.js';
const router = express.Router();

router.post('/checkToken', authenticateToken, returnUserInfoByToken);

export default router; 