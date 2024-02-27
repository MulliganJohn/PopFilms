import React, {useState} from 'react'
import './reviewwithmovie.css'
import logo from '../../Assets/logo-small.png';
import convertEpochToDate from '../../Utils/dateUtils'
import { getImageUrl } from '../../Utils/config';
import { getApiUrl } from '../../Utils/config';
import { useNavigate } from 'react-router-dom';

const ReviewWithMovie = ({reviewwithmovie, removeReview, canUserModerate}) => {
  const [isSubmittingReview, setIsSubmittingReview] = useState(false);
  const navigate = useNavigate();
  const gotoURL = (url) => {
    navigate(url);
  };

  const handleReviewDelete = async () => {
    try {
        setIsSubmittingReview(true);
        const response = await fetch(`${getApiUrl()}/api/reviews/removereview?id=${reviewwithmovie.reviewId}`, {
            method: 'DELETE',
            credentials: 'include'
        });

        if (response.ok){
            setIsSubmittingReview(false);
            removeReview(reviewwithmovie.reviewId)
        }
        else{
            setIsSubmittingReview(false);
        }

    } catch (error) {
        setIsSubmittingReview(false);
    }
  }

  return (
    <div className='popfilms__reviewwithmovie'>
      <div className='popfilms__reviewwithmovie-left'>
        <img id='movieposter' src={`${getImageUrl()}/thumbnails/${String (reviewwithmovie.movieId)}.jpg`} alt="movieposter" onClick={gotoURL.bind(this, `../../movies/${reviewwithmovie.movieId}`)}/>
      </div>
      <div className='popfilms__reviewwithmovie-right'>
        <h2><a  href={`../../movies/${reviewwithmovie.movieId}`} style={{color: 'white', textDecoration: 'none'}}>{reviewwithmovie.movieTitle}</a></h2>
        <div className='rating'>
          <img id='reviewlogo' src={logo} alt="logo"/>
          <p>{reviewwithmovie.rating}</p>
        </div>
        <div className='titleandepoch'>
            <h1>{reviewwithmovie.title}</h1>
            <p>{convertEpochToDate(reviewwithmovie.epoch)}</p>
        </div>
        <p className='reviewbody'>{reviewwithmovie.body}</p>
      </div>
      {canUserModerate && <button 
          onClick={handleReviewDelete}
          disabled={isSubmittingReview}
          >
          {isSubmittingReview ? 'Deleting Review...' : 'Delete Review'}
      </button>}
    </div>
  )
}

export default ReviewWithMovie