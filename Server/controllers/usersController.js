import * as usersServices from '../services/usersServices.js';
import { getKey } from './tokenController.js';
import jwt from 'jsonwebtoken';
import Users from '../models/usersModel.js'; 


import { decodeToken } from './tokenController.js';
// import { addNewUser } from '../services/usersServices.js';

async function getUserInfo(req, res) {
    try {
        const id = req.params.id;
        //const result = await findById(id);
        const user = await usersServices.getInfo(id);
        res.send(user);
    } catch (error) {
        //console.error('Error user:', error);
        return res.status(404).json({ message: 'User not found' });
    }
}

/*
* This function should delete the user from the users database
* it's delete the user that asked to delete the account only if is the same user that is logged in
* and only then delete the user from the users array
*/
function deleteUser(req, res) {
    const idToDelete = req.params.id;
    const currentUserId = req.userObjectID;
    if (idToDelete !== currentUserId) {
        console.log('Error - Unauthorized: user trying to delete another user')
        return res.status(401).json({ message: 'Error - Unauthorized: user trying to delete another user' });
    } else {
        console.log('server - deleteUser');
        usersServices.deleteUser(idToDelete);
        res.status(200).json({ message: 'User deleted successfully' });
    }
}

// function updateUser(req, res) {
//     console.log('updateUser');
//     const idToEdit = req.params.id;
//     const currentUserId = req.userObjectID;
//     //const objectId = mongoose.Types.ObjectId(currentUserId);
//     //const currentUserId = decodeToken(req, res);
//     if (idToEdit !== currentUserId) {
//         console.log('Error - Unauthorized: user trying to edit another user')
//         return res.status(401).json({ message: 'Error - Unauthorized: user trying to edit another user' });
//     } else {
//         console.log('editUser');
//         //UPDATE USERNAME
//         const newUsername = req.body.username;
//         if (!newUsername) {
//             return res.status(400).json({ message: 'Username is required' });
//         }
//         usersServices.updateUsername(currentUserId, req.body.username);

//         const newMail = req.body.email;
//         if (!newMail) {
//             return res.status(400).json({ message: 'Email is required' });
//         }
//         usersServices.updateEmail(currentUserId, req.body.email);

//         const newPassword = req.body.password;
//         if (!newPassword) {
//             return res.status(400).json({ message: 'Password is required' });
//         }
//         usersServices.updatePassword(currentUserId, req.body.password);

//         res.status(200).json({ message: 'Username updated successfully' });
//     }
// }

// User Controller
const updateUser = async (req, res) => {
    console.log('updateUser');
    const idToEdit = req.params.id;
    const currentUserId = req.userObjectID;

    if (idToEdit !== currentUserId) {
        console.log('Error - Unauthorized: user trying to edit another user');
        return res.status(401).json({ message: 'Error - Unauthorized: user trying to edit another user' });
    }

    const { username, email, password } = req.body;
    const updateData = {};

    if (username) {
        const existingUser = await Users.findOne({ username });
        if (existingUser && existingUser._id.toString() !== currentUserId) {
            return res.status(400).json({ message: 'Username is already in use' });
        }
        updateData.username = username;
    }

    if (email) {
        const existingEmail = await Users.findOne({ email });
        if (existingEmail && existingEmail._id.toString() !== currentUserId) {
            return res.status(400).json({ message: 'Email is already in use' });
        }
        updateData.email = email;
    }

    if (password) {
        // Validate password
        if (!(await usersServices.validatePassword(password))) {
            return res.status(400).json({ message: 'Password does not meet the criteria' });
        }
        updateData.password = password;
    }

    if (Object.keys(updateData).length === 0) {
        return res.status(400).json({ message: 'At least one field (username, email, password) is required' });
    }

    try {
        await usersServices.updateUser(currentUserId, updateData);
        res.status(200).json({ message: 'User updated successfully' });
    } catch (error) {
        console.error('Error updating user:', error);
        res.status(500).json({ message: 'Error updating user' });
    }
};



async function createNewUser(req, res) {
    console.log('createNewUser');
    //TODO - this function should create a new user
    try {
        const { username, email, password } = req.body;
        const photo = req.file; // Multer stores the file in req.file

        // Check if email already exists
        const isEmailTaken = await usersServices.findByEmailBool(email);
        if (isEmailTaken) {
            return res.status(400).json({ message: 'Email already taken' });
        }

        // Check if user name already exists
        const isNamelTaken = await usersServices.findByUsernameBool(username);
        if (isNamelTaken) {
            return res.status(400).json({ message: 'Username already taken' });
        }

        // Validate password
        const isPasswordValid = await usersServices.validatePassword(password);
        if (!isPasswordValid) {
            return res.status(400).json({ message: 'Password must be at least 8 characters long and contain at least one number' });
        }


        const newUser = await usersServices.addNewUser(username, email, password, {
            data: photo.buffer,
            contentType: photo.mimetype
        });
        const { password: userPassword, ...userWithoutPassword } = newUser._doc; //remove password from user object`
        const token = jwt.sign({ id: userWithoutPassword._id }, getKey()); //generate token
        res.status(201).json({ user: userWithoutPassword, token }); //return user and token
    } catch (error) {
        res.status(500).json({ error: 'Failed to add new user', details: error });  // Handle synchronous errors

    }
}
async function returnUserInfoByToken(req, res) {
    try {
        const currentUserId = req.userObjectID;
        const user = await usersServices.getInfo(currentUserId);
        res.send(user);
    } catch (error) {
        return res.status(404).json({ message: 'Invalid token' });
    }
}
export {
    getUserInfo,
    deleteUser,
    updateUser,
    createNewUser,
    returnUserInfoByToken
}