import styles from './SearchBar.module.css';
import { useRef } from 'react';

function SearchBar({ doSearch, darkMode }) {

  const searchBox = useRef(null);

  // const search = function() {
  //   doSearch(searchBox.current.value);
  // }

  const search = async function () {
    const query = searchBox.current.value;

    if (!query) {
      console.error('Search query is empty');
      return;
    }

    try {
      const url = `http://localhost:3300/api/videos/${query}`;
      const response = await fetch(url, {
        method: 'GET',
      });
      if (response.ok) {
        const searchVideos = await response.json();
        console.log('search video to client');
        console.log(searchVideos);
        console.log('search video to client');
        doSearch(searchVideos);
        // navigate("/home");
      }
      else {
        console.error('Error fetching videos:', response.statusText);
      }
    } catch (error) {
      console.error('Error fetching videos:', error);
    }

  }

  return (
    <div className={`${styles.searchBar} d-flex align-items-center container-fluid`}>
      <input ref={searchBox} onKeyUp={search} type="text" className={`form-control ${darkMode ? styles.darkMode : ''}`} placeholder="Search"></input>
      <button className="btn btn-outline-secondary " type="button">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className={`${styles.searchIcon} bi bi-search `} viewBox="0 0 16 16">
          <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0" />
        </svg>
      </button>
    </div>
  );
}

export default SearchBar;