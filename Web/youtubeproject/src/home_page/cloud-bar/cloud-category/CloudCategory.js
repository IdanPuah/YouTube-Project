import styles from './CloudCategory.module.css'

function CloudCategory({ category, clickCategory, darkMode}){

    const handleClick = () => {
        clickCategory(category);
    }

    return(
        <button type="button" className={`${styles.myCattegoryBottun} ${darkMode ? styles.darkMode : ''}`} onClick={handleClick}>{category}</button>   
    );
}

export default CloudCategory;