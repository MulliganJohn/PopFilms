import React, {useState} from 'react'
import logo from '../../Assets/logo-small.png';
import './review.css'
import userpic from '../../Assets/default-user.jpg'
import convertEpochToDate from '../../Utils/dateUtils';
import { getApiUrl } from '../../Utils/config';
import { useNavigate } from 'react-router-dom';

const Review = ({review, canUserModerate, refetch, recheckReview, setReviewStatus}) => {
  const [isSubmittingReview, setIsSubmittingReview] = useState(false);
  const navigate = useNavigate();
  const gotoURL = (url) => {
    navigate(url);
  };

  const handleReviewDelete = async () => {
    try {
        setIsSubmittingReview(true);
        const response = await fetch(`${getApiUrl()}/api/reviews/removereview?id=${review.reviewId}`, {
            method: 'DELETE',
            credentials: 'include'
        });

        if (response.ok){
            setIsSubmittingReview(false);
            setReviewStatus(null);
            recheckReview();
            refetch();
        }
        else{
            setIsSubmittingReview(false);
        }

    } catch (error) {
        setIsSubmittingReview(false);
    }
  }


  return (
    <div className='popfilms__review'>
      <div className='popfilms__review-left'>
        <img id='userpic' src={userpic} alt="userpic" onClick={gotoURL.bind(this, `../../users/${review.user.id}`)}/>
        <div className='popfilms__review-left-info'>
          <h1> <a  href={`../../users/${review.user.id}`} style={{color: 'white', textDecoration: 'none'}}>{review.user.username}</a></h1>
          <p>Total Reviews: {review.user.totalReviews}</p>
          <p>{convertEpochToDate(review.epoch)}</p>
        </div>
      </div>
      <div className='popfilms__review-right'>
        <div className='rating'>
          <img id='reviewlogo' src={logo} alt="logo"/>
          <p>{review.rating}</p>
        </div>
        <h1>{review.title}</h1>
        <p className='reviewbody'>{review.body}</p>
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

export default Review