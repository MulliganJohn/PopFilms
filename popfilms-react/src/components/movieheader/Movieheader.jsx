import React, {useState, useEffect} from 'react'
import './movieheader.css';
import tmdblogo from '../../Assets/tmdblogo.svg';
import poplogo from '../../Assets/logo-small.png';
import { useParams } from 'react-router-dom';
import { getApiUrl, getImageUrl } from '../../Utils/config';

const Movieheader = ({setPosterLoaded}) => {
  const [movieInfo, setMovieInfo] = useState([]);
  let { MovieId } = useParams();

  useEffect(() => {
    fetch(`${getApiUrl()}/api/movies/${String (MovieId)}`)
      .then(response => response.json())
      .then(data => setMovieInfo(data))
      .catch(error => console.error(error));
  }, [MovieId]);

  useEffect(() => {
  }, [movieInfo]);

  const formatMovieLength = (len) => {
    const hours = Math.floor(len / 60);
    const minutes = len % 60;
  
    return `${hours}h ${minutes}m`;
  };

  return (
    <div className='popfilms__movieheader'>
        <img className='moviePNG' src={`${getImageUrl()}/images/${String (MovieId)}.jpg`} alt="movieimg" onLoad={setPosterLoaded}/>
        <div className='popfilms__movieinfo'>
          <div className='popfilms__movieinfo-topcontainer'>
            <h1>{movieInfo.title}</h1>
            <div className='dtrgrcontainer'>
              <div className='dtr'>
                <p id='date'>{movieInfo.releaseYear}</p>
                <p id='time'>{formatMovieLength(movieInfo.length)}</p>
                <p id='rating'>{movieInfo.rating}</p>
              </div>
              {movieInfo && movieInfo.genres && movieInfo.genres.length > 0 &&<p id='genre'>{movieInfo.genres.join(", ")}</p>}
              <div className='review'>
                <div className='webrating'>
                  <img id='poplogo' src={poplogo} alt="logo"/>
                  <p>{movieInfo.popfilmsRating}</p>
                </div>
                <div className='webrating'>
                  <img id='tmdblogo' src={tmdblogo} alt="logo"/>
                  {movieInfo.tmdbRating && <p>{movieInfo.tmdbRating.toFixed(1)}</p>}
                </div>
              </div>
            </div>
          </div>
          <p id='summary'>{movieInfo.summary}</p>
          <div className='dwscontainer'>
            <div className='dwschild'>
              <p>Directed By</p>
              {movieInfo && movieInfo.directors && movieInfo.directors.length > 0 && <p>{movieInfo.directors.join(", ")}</p>}
            </div>
            <div className='dwschild'>
              <p>Written By</p>
              {movieInfo && movieInfo.writers && movieInfo.writers.length > 0 && <p>{movieInfo.writers.join(", ")}</p>}
            </div>
            <div className='dwschild'>
              <p>Studio</p>
              {movieInfo && movieInfo.studios && movieInfo.studios.length > 0 && <p>{movieInfo.studios.join(", ")}</p>}
            </div>
          </div>
      </div>
    </div>
  )
}

export default Movieheader