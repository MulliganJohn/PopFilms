import React from 'react'
import StarRating from '../starrating/StarRating'

const EditRatingHeader = ({onStarRating, savedrating}) => {

    return (
        <div className='popfilms_reviewratingheader'>
            <h1>Edit Review</h1>
            <StarRating onStarRating={onStarRating} savedrating={savedrating}/>
        </div>
      )
}

export default EditRatingHeader