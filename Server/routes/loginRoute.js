import express from 'express';
import { isLoggedIn, login } from '../controllers/loginController.js';

const router = express.Router();

router.post('/api/tokens', login);//TO-DO - on reload, the user should still be logged in

export default router;