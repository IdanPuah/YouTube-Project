import styles from './SelfDetails.module.css'
import React, { useContext } from 'react';
import { AuthContext } from '../../auth/AuthContext'; // Adjust the path as needed

function SelfDetails({ /*currentUser*/ SignOutHandler }) {
    const { currentUser } = useContext(AuthContext);
    //console.log("SelfDetails");
    //console.log("SelfDetails: ", currentUser);

    const photoPath = (currentUser.photo); // Access the photo property of currentUser
    //console.log(photoPath);
    return (
        <div className={`d-flex align-items-center container-fluid`}>


            <div className="dropdown">
                <img className={styles.profilePicture} type="button" data-bs-toggle="dropdown" aria-expanded="false" src={photoPath} alt="" />
                <ul className={`dropdown-menu`}>
                    <li><button className="dropdown-item" type="button" onClick={SignOutHandler} >Sign out</button></li>
                </ul>
            </div>
        </div>


    );
}
export default SelfDetails;