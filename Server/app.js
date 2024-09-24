import express from 'express';
import mongoose from 'mongoose';
//import customEnv from 'custom-env';
import cors from 'cors';
import path from 'path';

import bodyParser from 'body-parser';
import { fileURLToPath } from 'url';

//import cookieParser from 'cookie-parser';
import session from 'express-session';

import connectDB from './db.js';
import loginRoutes from './routes/loginRoute.js';
import userRoute from './routes/userRoute.js';
import videoRoute from './routes/videoRoute.js';
import testRoute from './routes/testRoute.js';
import commentRoute from './routes/commentRoute.js';
import tokenRoute from './routes/TokenRoute.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);


await connectDB();
const server = express();
server.use(cors());
//server.use(cookieParser()); // Middleware to parse cookies
server.use(session({
    secret: 'YoutyoubeProject2024',
    saveUninitialized: false,
    resave: false
}))



// server.use(bodyParser.json()); // Middleware to parse JSON bodies
// server.use(bodyParser.urlencoded({ extended: true })); // Middleware to parse URL-encoded bodies

server.use(bodyParser.json({ limit: '50mb' })); // Adjust limit as needed
server.use(bodyParser.urlencoded({ extended: true, limit: '50mb' }));

server.use(express.static('public'));
server.use(express.json());
 

server.use('/', loginRoutes);
server.use('/', userRoute);
server.use('/', videoRoute);
server.use('/', commentRoute);
server.use('/', tokenRoute);

server.use('/test', testRoute); // remove this before submitting

server.get('/manifest.json', (req, res) => {
    res.json({ key: 'value' });
  });
server.get('*', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

server.listen(3300)