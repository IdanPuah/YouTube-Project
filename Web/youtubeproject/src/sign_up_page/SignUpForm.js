import React, { useState } from 'react';

import styles from './SignUpForm.module.css'; // Import css modules stylesheet as styles
import { useNavigate } from 'react-router-dom';

function SignUpForm({ darkMode, setCurrentLoggedInUser }) {
    const navigate = useNavigate();
    const [passwordValidity, setPasswordValidity] = useState(false);
    const [passwordErrorMsg, setPasswordErrorMsg] = useState('');

    const [verifyPasswordValidity, setVerifyPasswordValidity] = useState(false);
    const [verifyPasswordErrorMsg, setVerifyPasswordErrorMsg] = useState('');


    const handleSubmit = async (e) => {
        setPasswordValidity(false);
        setVerifyPasswordValidity(false);
        setPasswordErrorMsg('');
        setVerifyPasswordErrorMsg('');

        e.preventDefault();
        const formData = new FormData(e.target);

        const password = formData.get('password');
        const vPassword = formData.get('verifyPassword');
        if (!verifyPassword(password, vPassword)) {
            return
        }

        try {
            const response = await fetch('http://localhost:3300/api/users', {
                method: 'POST',
                body: formData,
            });

            if (response.ok) {
                const data = await response.json();
                console.log(data);
                const token = data.token;
                const user = data.user;
                console.log(user)
                setCurrentLoggedInUser(user);
                localStorage.setItem('token', token);
                navigate("/home");
            } else {
                const errorData = await response.json();
                console.error('Error creating user:', errorData);

                switch (response.status) {
                    case 400:
                        if (errorData.message === 'Email already taken') {
                            setPasswordErrorMsg('Email already taken');
                        } else if (errorData.message === 'Username already taken') {
                            setPasswordErrorMsg('Username already taken');
                        } else if (errorData.message === 'Password must be at least 8 characters long and contain at least one number') {
                            setPasswordErrorMsg('Password must be at least 8 characters long and contain at least one number');
                        }
                        break;
                    case 500:
                        setPasswordErrorMsg('Error creating user');
                        break;
                    default:
                        setPasswordErrorMsg('An unknown error occurred');
                }
            }
        } catch (error) {
            console.error('Network error:', error);
            setPasswordErrorMsg('Network error, please try again later');
        }

    };

    const verifyPassword = (password, verifyPassword) => {
        if (password !== verifyPassword) {
            //console.log("passwords do not match");
            setVerifyPasswordErrorMsg('• Passwords do not match');
            setVerifyPasswordValidity(false);
            return false;
        } else {
            //console.log("passwords match");
            setVerifyPasswordErrorMsg('');
            setVerifyPasswordValidity(true);
            return true;
        }
    }

    /*
    const checkValidPassword = (password) => {
        let errorMessages = [];
        let isValid = true;

        if (password.length < 6) {
            errorMessages.push('• Password should be at least 6 characters long');
            isValid = false;
        }
        if (!/\d/.test(password)) {
            errorMessages.push('• Password should contain at least one number');
            isValid = false;
        }
        if (!/[A-Z]/.test(password) || !/[a-z]/.test(password)) {
            errorMessages.push('• Password should include both upper and lower case letters');
            isValid = false;
        }
        if (!/[^A-Za-z0-9]/.test(password)) {
            errorMessages.push('• Password should include at least one special character');
            isValid = false;
        }

        if (isValid) {
            setPasswordValidity(true);
            setPasswordErrorMsg('');
        } else {
            setPasswordValidity(false);
            setPasswordErrorMsg(errorMessages.join('\n'));
        }
        return isValid;
    }
        */
    return (
        <div className={`d-flex justify-content-center align-items-center vh-100 ${darkMode ? styles.pageDarkMode : ''}`}>
            <div className={`${styles.SignUpForm} ${darkMode ? styles.formDarkMode : ''}`}>
                <h2 className={styles.Title}>Create Your Account</h2>
                <label htmlFor="basic-url" className={styles.Label}>Enter your username and password:</label>

                <form className={`needs-validation`} onSubmit={handleSubmit}>
                    <div className="input-group mb-3">
                        <span className={`input-group-text ${darkMode ? styles.iconDarkMode : ''}`} id="basic-addon1">
                            <i className="bi bi-person-fill-add"></i>
                        </span>
                        <input
                            type="text"
                            name='username'
                            className={`form-control ${darkMode ? styles.inputDarkMode : ''}`}
                            placeholder="Display Name"
                            aria-label="Display Name"
                            aria-describedby="basic-addon1"
                            required
                        />
                    </div>

                    <div className="input-group mb-3">
                        <span className={`input-group-text ${darkMode ? styles.iconDarkMode : ''}`} id="basic-addon1">
                            <i className="bi bi-envelope-at-fill"></i>
                        </span>
                        <input
                            type="email"
                            name='email'
                            className={`form-control ${darkMode ? styles.inputDarkMode : ''}`}
                            placeholder="Email"
                            aria-label="Email"
                            aria-describedby="basic-addon1"
                            autoComplete="email"
                            required
                        />
                    </div>

                    <div className="input-group mb-3">
                        <span className={`input-group-text ${darkMode ? styles.iconDarkMode : ''}`} id="basic-addon1">
                            <i className="bi bi-shield-lock"></i>
                        </span>
                        <input
                            type="password"
                            id='password'
                            name='password'
                            className={`form-control ${darkMode ? styles.inputDarkMode : ''}`}
                            placeholder="Password"
                            aria-label="Password"
                            aria-describedby="basic-addon1"
                            autoComplete="new-password"
                            required
                        />
                        {(!passwordValidity) &&
                            <div className="col-12 text-danger">
                                {passwordErrorMsg.split('\n').map((msg, index) => (
                                    <div key={index}>{msg}</div>
                                ))}
                            </div>
                        }
                    </div>


                    <div className="input-group mb-3">
                        <span className={`input-group-text ${darkMode ? styles.iconDarkMode : ''}`} id="basic-addon1">
                            <i className="bi bi-shield-lock-fill"></i>
                        </span>
                        <input
                            type="password"
                            id='verifyPassword'
                            name='verifyPassword'
                            className={`form-control ${darkMode ? styles.inputDarkMode : ''}`}
                            placeholder="Verify Password"
                            aria-label="Verify Password"
                            aria-describedby="basic-addon1"
                            autoComplete="new-password"
                            required
                        />
                        {(!verifyPasswordValidity) &&
                            <div className="col-12 text-danger">
                                {verifyPasswordErrorMsg}
                            </div>
                        }
                    </div>


                    <label htmlFor="basic-url" className={styles.Label}>Upload User Photo:</label>
                    <div className={`input-group mb-3 `}>
                        <label className={`input-group-text ${darkMode ? styles.iconDarkMode : ''}`} htmlFor="inputGroupFile01">
                            <i className="bi bi-camera"></i>
                        </label>
                        <input
                            accept='.jpg, .jpeg, .png'
                            type="file"
                            name='photo' ///////////////////////////////////////
                            className={`form-control ${darkMode ? styles.inputDarkMode : ''}`}
                            id="inputGroupFile01"
                        //onChange={handlePhotoChange}
                        />
                    </div>


                    <div className="text-center mt-4">
                        <button type="submit" className="btn btn-success">
                            Sign Up <i className="bi bi-person-add"></i>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default SignUpForm;
