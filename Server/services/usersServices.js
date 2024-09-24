import Users from '../models/usersModel.js';
import Videos from '../models/videosModel.js';
import mongoose from 'mongoose';
import { ObjectId } from 'mongodb';
const findByUsername = async (username) => {
    return await Users.findOne({ username });
}

const findByUsernameBool = async (username) => {
    const user = await Users.findOne({ username })
    if (user){
        return true
    }
    return false;
}

const findByEmail = async (email) => {
    const user = await Users.findOne({ email })
    if (user){
        console.log("user found")
        return user
    }
    return null;
}

const findByEmailBool = async (email) => {
    const user = await Users.findOne({ email })
    if (user){
        return true
    }
    return false;
}


const findById = async (id) => {
    const user = await Users.findOne({ _id: id });
    if (!user) {
        throw new Error('User not found');
    }
    return user;
}

const getInfo = async (id) => {
    const user = await Users.findById(id);
    const { password: userPassword, ...userWithoutPassword } = user.toObject();
    return userWithoutPassword;
}

const authenticate = async (username, password) => {
    const user = await findByUsername(username); // find user by username
    if (user && user.password === password) { // check if user exists and password is correct
        return user;
    }
    return null;
}

const addNewUser = async (username, email, password, photo) => {
    // create new user
    const newUser = new Users({
        username: username,
        email: email,
        password: password,
        photo: photo,
        uploads: "0", 
        subscription: "0"
    });

    const savedUser = await newUser.save(); // save user in database

    return savedUser; // return saved user
}

// const deleteUser = async (id) => {
//     try {
//         const user = await Users.findById(id); // Call findById on the User model
//         if (user) {
//             await Users.deleteOne({ _id: id }); // Use filter object with deleteOne
//             return true;
//         }
//         return false; // Return false if user is not found
//     } catch (error) {
//         console.error('Error deleting user:', error);
//         return false; // Return false in case of error
//     }
// };

const deleteUser = async (userId) => {
    try {
        const user = await Users.findById(userId); // Find the user by ID
        if (!user) {
            return false; // Return false if user not found
        }

        // Delete all videos associated with this user
        await Videos.deleteMany({ creatorId: userId });

        // Delete the user after deleting the videos
        await Users.deleteOne({ _id: userId });

        return true; // Return true if successful
    } catch (error) {
        console.error('Error deleting user and their videos:', error);
        return false; // Return false in case of error
    }
};


const updateUsername = async (id, newUsername) => {
    try {
        
        const user = await Users. findByIdAndUpdate(id, { username: newUsername }, { new: true } )// This option returns the updated document);
        return user ? true : false; // Return true if user was found and updated, otherwise false
    } catch (error) {
        console.error('Error updating username:', error);
        return false; // Return false in case of an error
    }
};

const updatePassword = async (id, newPassword) => {
    try {
        const user = await Users.findByIdAndUpdate(
            id, 
            { password: newPassword }, 
            { new: true } // This option returns the updated document
        );
        return user ? true : false; // Return true if user was found and updated, otherwise false
    } catch (error) {
        console.error('Error updating username:', error);
        return false; // Return false in case of an error
    }
};

const updateUser = async (id, updateData) => {
    try {
        const user = await Users.findByIdAndUpdate(id, updateData, { new: true });
        return user ? true : false; // Return true if user was found and updated, otherwise false
    } catch (error) {
        console.error('Error updating user:', error);
        return false; // Return false in case of an error
    }
};


const updateEmail = async (id, newEmail) => {
    try {
        // Find user and update the email
        const user = await Users.findByIdAndUpdate(
            id, 
            { email: newEmail }, 
            { new: true } // This option returns the updated document
        );

        return user ? true : false; // Return true if user was found and updated, otherwise false
    } catch (error) {
        console.error('Error updating email:', error); // Updated error message
        return false; // Return false in case of an error
    }
};


const validatePassword = async (password) => {
    // Password must be at least 8 characters long
    if (password.length < 8) {
        return false;
    }
    // Password must contain at least one uppercase letter
    if (!/[A-Z]/.test(password)) {
        return false;
    }
    // Password must contain at least one lowercase letter
    if (!/[a-z]/.test(password)) {
        return false;
    }
    // Password must contain at least one number
    if (!/[0-9]/.test(password)) {
        return false;
    }
    return true;
}

export {
    findByUsername,
    findById,
    authenticate,
    addNewUser,
    deleteUser,
    updateUsername,
    updatePassword,
    findByEmail,
    getInfo,
    validatePassword,
    findByEmailBool,
    findByUsernameBool,
    updateEmail,
    updateUser

}

