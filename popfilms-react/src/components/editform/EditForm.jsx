import React, { useState, useEffect, useRef } from 'react'
import { useParams } from 'react-router-dom';
import { getApiUrl } from '../../Utils/config';
import ReviewRatingHeader from '../reviewratingheader/ReviewRatingHeader';
import './editform.css'
import EditRatingHeader from '../editratingheader/EditRatingHeader';

const EditForm = ({hide, active, reviewCheckDto, refetch, recheckReview, setReviewStatus}) => {
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
  
      const resetForm = () => {
        setFormDrag(false);
        resetReview();
        setInitialRender(true);
        setHighlightedInput(null);
        setTitleHasBeenHighlighted(false);
        setBodyHasBeenHighlighted(false);
        setIsSubmittingReview(false);
        setErrorMessage("");
        setShowError(false);
      }
  
      const resetReview = () => {
        setReview((prevReview) => ({
          movieId: MovieId,
          title: '',
          body: '',
          rating: 0,
        }));
      }

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
        if (reviewCheckDto){
            setReview(reviewCheckDto);
        }
      }, [reviewCheckDto]);


    
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
          const response = await fetch(`${getApiUrl()}/api/reviews/editreview?id=${review.reviewId}`, {
            method: 'PUT',
            credentials: 'include',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                title: review.title,
                body: review.body,
                rating: review.rating,
                movieId: review.movieId
            }),
          });
  
          if (response.ok){
            refetch();
            setIsSubmittingReview(false);
            //resetForm();
            hide();
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
                //resetForm();
                hide();
                refetch();
            }
            else{
                setIsSubmittingReview(false);
                setErrorMessage("Error Deleting Review!");
                setShowError(true);
            }
    
        } catch (error) {
            setIsSubmittingReview(false);
            setErrorMessage("Error Deleting Review!");
            setShowError(true);
        }
      }


  
    return (active) ? (
      <div className='popfilms__reviewform' onClick={handleOuterDivClick} onMouseDown={handleMouseDown}>
          <div className='popfilms__reviewform-form'>
              <EditRatingHeader onStarRating={handleRatingChange} savedrating={review.rating} id='reviewFormRating'/>
              { (getRatingMessage(review.rating) !== null) ? <p className='signupform-error-text'>{getRatingMessage(review.rating)}</p>: null}
              <input placeholder='Review Title' value={review.title} onChange={handleTitleChange} ref={titleRef} id='reviewFormTitle'></input>
              {(highlightedInput === 'title' && getTitleMessage(review.title) !== null) || (titleHasBeenHighlighted && getTitleMessage(review.title) !== null) ? <p className='signupform-error-text'>{getTitleMessage(review.title)}</p>: null}
              <textarea placeholder='Review Body' value={review.body} onChange={handleBodyChange} ref={bodyRef} id='reviewFormBody'></textarea>
              {(highlightedInput === 'body' && getBodyMessage(review.body) !== null) || (bodyHasBeenHighlighted && getBodyMessage(review.body) !== null) ? <p className='signupform-error-text'>{getBodyMessage(review.body)}</p>: null}
              <button 
                onClick={handleReviewSubmit}
                disabled={isSubmittingReview || !isFormValid} 
                id='reviewFormButton'>
                {isSubmittingReview ? 'Editing Review...' : 'Edit Review'}
              </button>
              <button 
                onClick={handleReviewDelete}
                disabled={isSubmittingReview} 
                id='editReviewFormDeleteButton'>
                {isSubmittingReview ? 'Deleting Review...' : 'Delete Review'}
              </button>
              {showError ? <p className='signupform-error-text'>{errorMessage}</p> : null}
          </div>
      </div>
    ) : "";
}

export default EditForm

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