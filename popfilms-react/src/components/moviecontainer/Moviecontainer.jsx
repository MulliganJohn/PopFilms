import React, {useRef, useState, useEffect} from 'react';
import './moviecontainer.css';
import scrollbuttonright from '../../Assets/RightScroll.png';
import scrollbuttonleft from '../../Assets/LeftScroll.png';
import { useNavigate } from 'react-router-dom';
import { getImageUrl, getApiUrl } from '../../Utils/config';

function Moviecontainer({title, endpoint}) {
  const [movieIds, setMovieIds] = useState([]);
  const navigate = useNavigate();
  const movieContainerRef = useRef(null);


  const gotoMovie = (url) => {
    navigate('/movies/' + url);
  };

  useEffect(() => {
    fetch(`${getApiUrl()}/${String(endpoint)}`)
      .then(response => response.json())
      .then(data => setMovieIds(data))
      .catch(error => console.error(error));
  }, [endpoint]);


  const handleScrollClick = (dir) => {
    if (dir === 'right' && movieContainerRef.current){
      movieContainerRef.current.scrollBy({
        left: 1000, // adjust as needed
        behavior: 'smooth'
      });
    }
    else if (movieContainerRef.current) {
      movieContainerRef.current.scrollBy({
        left: -1000, // adjust as needed
        behavior: 'smooth'
      });
    }
  };
  
  return (
    <div className='popfilms__movielist'>
      <p>{title}</p>
      <div className= 'popfilms__movielist-moviecontainer'>
        <input className='scroll-button-left' type='button' style={{ backgroundImage: `url(${scrollbuttonleft})` }} onClick={handleScrollClick.bind(this, 'left')}/>
        <div className='popfilms__movielist-movies' ref={movieContainerRef}>
            {movieIds.map(movieId => (
              <img key = {movieId} alt={movieId} src={`${getImageUrl()}/thumbnails/${String (movieId)}.jpg`} onClick={gotoMovie.bind(this, movieId)} className='MovieContainerMovies'/>
            ))}
        </div>
        <input className='scroll-button-right' type='button' style={{ backgroundImage: `url(${scrollbuttonright})` }} onClick={handleScrollClick.bind(this, 'right')}/>
      </div>
    </div>
  );
}
export default Moviecontainer;