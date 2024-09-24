import express from 'express';
import { isLoggedIn, login } from '../controllers/loginController.js';
import { decodeToken } from '../controllers/tokenController.js';

const router = express.Router();

router.get('/',isLoggedIn, (req, res) => {
    res.send('You are logged in');
});

router.get('/token', decodeToken)

export default router;