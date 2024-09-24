import jwt from 'jsonwebtoken';
import { findByEmail }  from '../services/usersServices.js';
import { getKey } from './tokenController.js';

async function login(req, res) {
    try {
        const { email, password } = req.body;

        const user = await findByEmail(email);
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        const isPasswordValid = password === user.password;
        if (!isPasswordValid) {
            return res.status(401).json({ message: 'Invalid password' });
        }

        const { password: userPassword, ...userWithoutPassword } = user;

        const token = jwt.sign({ id: user._id }, getKey()); 
        res.json({ user: userWithoutPassword, token });
    } catch (error) {
        console.error('Error logging in:', error);
        res.status(500).json({ message: 'Internal server error' });
    }
}

function isLoggedIn(req, res, next) {
    if(req.session.token) {
        next()
    } else {
        res.redirect('/home')
    }
}

export { login, isLoggedIn };