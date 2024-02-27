import React from 'react'
import './reviewratingheader.css'
import StarRating from '../starrating/StarRating'

const ReviewRatingHeader = ({onStarRating, savedrating}) => {


  return (
    <div className='popfilms_reviewratingheader'>
        <h1>Leave A Review</h1>
        <p>What would you rate this movie?</p>
        <StarRating onStarRating={onStarRating} savedrating={savedrating}/>
    </div>
  )
}

export default ReviewRatingHeader