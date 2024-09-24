import styles from './MenuCategory.module.css'

function MenuCategory({icon, categoryName, darkMode, onClickHandler }) {
    return (
        <button className={`list-group-item list-group-item-action w-100 ${ styles.darkMode}`} onClick={onClickHandler}>
            <div className='d-flex align-items-center'>
                {icon}
                <h5 className={`${styles.categoryName}`}>{categoryName}</h5>
            </div>
        </button>
    );
}

export default MenuCategory;