import React, { useState, useEffect, useRef } from 'react'
import { useParams } from 'react-router-dom';
import './reviewform.css'
import ReviewRatingHeader from '../reviewratingheader/ReviewRatingHeader';
import { getApiUrl } from '../../Utils/config';

const ReviewForm = ({active, hide, refetch, recheckReview, setReviewStatus}) => {
    const blankReview = {
      movieId: null,
      title: '',
      body: '',
      rating: 0,
    };
    const [formDrag, setFormDrag] = useState(false);
    const { MovieId } = useParams();
    const [review, setReview] = useState(blankReview);
    const [isInitialRender, setInitialRender] = useState(true);
    const isFormValid = review.title.length > 5 && review.title.length <= 500 && review.body.length > 20 && review.body.length <= 4096 && review.rating > 0;
    let titleRef = useRef(null);
    let bodyRef = useRef(null);
    const [highlightedInput, setHighlightedInput] = useState(null);
    const [titleHasBeenHighlighted, setTitleHasBeenHighlighted] = useState(false);
    const [bodyHasBeenHighlighted, setBodyHasBeenHighlighted] = useState(false);
    const [isSubmittingReview, setIsSubmittingReview] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [showError, setShowError] = useState(false);

    function loadSavedReview(){
      const savedReview = JSON.parse(localStorage.getItem('savedReview'));
      if (savedReview) {
          if (savedReview.movieId !== MovieId){
              localStorage.removeItem('savedReview');
              setReview((prevReview) => ({
                  ...prevReview,
                  movieId: MovieId,
                }));
          }
          else{
              setReview(savedReview);
          }
      }
      else{
          setReview((prevReview) => ({
              ...prevReview,
              movieId: MovieId,
            }));
      }
    }

    useEffect(() => {
      loadSavedReview();
    }, []);

    const handleFocusChange = () => {
      const activeElement = document.activeElement;
      if (activeElement === titleRef.current) {
        setHighlightedInput('title');
        setTitleHasBeenHighlighted(true);
      } else if (activeElement === bodyRef.current) {
        setHighlightedInput('body');
        setBodyHasBeenHighlighted(true);
      } else {
        setHighlightedInput(null);
      }
    };

    useEffect(() => {
      document.addEventListener('focusin', handleFocusChange);

      return () => {
        document.removeEventListener('focusin', handleFocusChange);
      };
    }, []);

    useEffect(() => {
        if (!isInitialRender) {
          localStorage.setItem('savedReview', JSON.stringify(review));
        } else {
            setInitialRender(false);
        }
    }, [review]);


    const handleOuterDivClick = (event) => {
        if (event.target.classList.contains('popfilms__reviewform') && !formDrag) {
            hide();
        }
        return;
    };

    const handleMouseDown = (event) => {
        if (!event.target.classList.contains('popfilms__reviewform')) {
            setFormDrag(true);
            return;
        }
        setFormDrag(false);
    }

    const handleTitleChange = (event) => {
        setReview((prevReview) => ({
            ...prevReview,
            title: event.target.value,
          })); 
    };

    const handleBodyChange = (event) => {
        setReview((prevReview) => ({
            ...prevReview,
            body: event.target.value,
          })); 
    }

    const handleRatingChange = (Rating) => {
        setReview((prevReview) => ({
          ...prevReview,
          rating: Rating,
        }));
    }

    const handleReviewSubmit = async () => {
      try {
        setIsSubmittingReview(true);
        const response = await fetch(`${getApiUrl()}/api/reviews/addreview`, {
          method: 'POST',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(review),
        });

        if (response.ok){
          setIsSubmittingReview(false);
          localStorage.removeItem('savedReview');
          setReviewStatus(null);
          recheckReview();
          hide();
          refetch();
        }
        else{
          setIsSubmittingReview(false);
          setErrorMessage("Error Leaving Review!");
          setShowError(true);
        }
  
      } catch (error) {
        setIsSubmittingReview(false);
        setErrorMessage("Error Leaving Review!");
        setShowError(true);
      }
    }

  return (active) ? (
    <div className='popfilms__reviewform' onClick={handleOuterDivClick} onMouseDown={handleMouseDown}>
        <div className='popfilms__reviewform-form'>
            <ReviewRatingHeader onStarRating={handleRatingChange} savedrating={review.rating} id='reviewFormRating'/>
            { (getRatingMessage(review.rating) !== null) ? <p className='signupform-error-text'>{getRatingMessage(review.rating)}</p>: null}
            <input placeholder='Review Title' value={review.title} onChange={handleTitleChange} ref={titleRef} id='reviewFormTitle'></input>
            {(highlightedInput === 'title' && getTitleMessage(review.title) !== null) || (titleHasBeenHighlighted && getTitleMessage(review.title) !== null) ? <p className='signupform-error-text'>{getTitleMessage(review.title)}</p>: null}
            <textarea placeholder='Review Body' value={review.body} onChange={handleBodyChange} ref={bodyRef} id='reviewFormBody'></textarea>
            {(highlightedInput === 'body' && getBodyMessage(review.body) !== null) || (bodyHasBeenHighlighted && getBodyMessage(review.body) !== null) ? <p className='signupform-error-text'>{getBodyMessage(review.body)}</p>: null}
            <button 
              onClick={handleReviewSubmit}
              disabled={isSubmittingReview || !isFormValid} 
              id='reviewFormButton'>
              {isSubmittingReview ? 'Leaving Review...' : 'Leave Review'}
            </button>
            {showError ? <p className='signupform-error-text'>{errorMessage}</p> : null}
        </div>
    </div>
  ) : "";
}

export default ReviewForm


function getTitleMessage(title){
  if (title.length <= 5){
      return "Title length must be greater than 5 characters!"
  }
  if (title.length > 500){
    return "Title length is more than 500 characters!"
  }
  return null;
}

function getBodyMessage(body){
  if (body.length <= 20){
      return "Body length must be greater than 20 characters!"
  }
  if (body.length > 4096){
    return "Body length is more than 4096 characters!"
  }
  return null;
}

function getRatingMessage(rating){
  if (rating < 1){
    return "Click on a star to add your rating!"
  }
  return null;
}