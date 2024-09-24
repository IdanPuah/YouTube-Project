import styles from './SignInPage.module.css';
import { Link, useNavigate } from 'react-router-dom';
import React, { useState } from 'react';
function SignInPage({ darkMode ,setCurrentLoggedInUser }) {
    const [loginError, setLoginError] = useState(false);
    const navigate = useNavigate();


    const handleSubmit = async (e) => {

        e.preventDefault(); // Prevent default form submission
        const formData = new FormData(e.target);
        const email = formData.get('email');
        const password = formData.get('password');

        const response = await fetch('http://localhost:3300/api/tokens', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const data = await response.json();
            const token = data.token;
            const user = data.user._doc;
            setCurrentLoggedInUser(user);
            localStorage.setItem('token', token);
            console.log('User logged in:', user);
            navigate("/home");
        } else {
            setLoginError(true);
        }
    }


    return (
        <>
            <div id="page" className={`${styles.page} container-fluid vh-100 d-flex align-items-center justify-content-center ${darkMode ? styles.pageDarkMode : ''}`}>
                <div id="main-div" className={`${styles.mainDiv} ${darkMode ? styles.mainDivDarkMode : ''}`}>
                    <div className="row">
                        <div className="col-6">
                            <img className={`img-fluid ${styles.logo}`} src="signInRes/youtubeLogo.png" alt="logo"></img>
                            <div id="signInText" className={styles.signInText} >
                                <h2>Sign in</h2>
                                <p>to continue to YouTube</p>
                            </div>
                        </div>
                        <div id="right-section" className={`col-6 ${styles.rightSection}`}>
                            <form onSubmit={handleSubmit}>
                                <div>
                                    <input className={`form-control ${styles.textInput} ${darkMode ? styles.inputDarkMode : ''}`} type="email" placeholder="Email" name='email' required></input>
                                    <input type="password" className={`form-control ${styles.textInput} ${darkMode ? styles.inputDarkMode : ''}`} id="exampleInputPassword1"
                                        placeholder="Password" name='password' autoComplete="on" required></input>
                                </div>
                                {loginError &&
                                    <div className="col-12 text-danger">Email or password is incorrect</div>
                                }
                                <div id="btn-div" className={`d-flex justify-content-end align-items-end ${styles.btnDiv}`}>
                                    <Link to="/SignUp">
                                        <button type="button" className={`btn btn-light ${styles.MyBtn} ${styles.zeroMargin}`} >Create account</button>
                                    </Link>
                                    <button type="submit" className={`btn btn-dark ${styles.MyBtn}`}>
                                        Sign in
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default SignInPage;