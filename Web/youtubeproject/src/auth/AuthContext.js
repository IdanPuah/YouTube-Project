import React, { createContext, useState, useEffect } from 'react';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [currentUser, setCurrentUser] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const authenticateUser = async (token) => {
        try {
            //console.log('Authenticating user...');
            const response = await fetch(`http://localhost:3300/checkToken`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `${token}`,
                },
            });

            if (response.ok) {
                const data = await response.json();
                //console.log('User authenticated:', data);
                data.photo = 'data:image/png;base64,'+data.photo.data;
                setCurrentUser(data);
                setIsLoggedIn(true);
            } else {
                const errorData = await response.json();
                console.error('Error authenticating user:', errorData);
            }
        } catch (error) {
            console.error('Network error:', error);
        }
    };

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            authenticateUser(token);
        }
    }, []);

    return (
        <AuthContext.Provider value={{ currentUser, isLoggedIn, setCurrentUser, setIsLoggedIn }}>
            {children}
        </AuthContext.Provider>
    );
};