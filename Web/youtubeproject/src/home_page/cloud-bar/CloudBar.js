import styles from './CloudBar.module.css'
import CloudCategory from './cloud-category/CloudCategory';

function CloudBar({ clickCategory, darkMode }) {
    return (
        <div className={`d-flex flex-row justify-content-around scroll-container ${styles.cloudMargin}`}>
            <div><CloudCategory category={"Sport"} clickCategory={clickCategory} darkMode={darkMode} /></div>
            <div><CloudCategory category={"News"} clickCategory={clickCategory} darkMode={darkMode}/></div>
            <div><CloudCategory category={"Health"} clickCategory={clickCategory} darkMode={darkMode}/></div>
            <div><CloudCategory category={"Nature"} clickCategory={clickCategory} darkMode={darkMode}/></div>
            <div><CloudCategory category={"Travel"} clickCategory={clickCategory} darkMode={darkMode}/></div>
            <div><CloudCategory category={"Science"} clickCategory={clickCategory} darkMode={darkMode}/></div>
        </div >

    );
}
export default CloudBar;