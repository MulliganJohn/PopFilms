import React from 'react'
import './reviewwithmoviecontainer.css'
import ReviewWithMovie from '../reviewwithmovie/ReviewWithMovie'

const ReviewWithMovieContainer = ({reviews, userDetails, setReviews}) => {

  const removeReview = (id) => {
    setReviews((prevReviews) => prevReviews.filter((review) => review.reviewId !== id));
  }


  return (
    <div className='popfilms__reviewwithmoviecontainer'>
      <div className={`popfilms__reviewwithmoviecontainer-wrapper ${reviews.length > 0 ? 'movie-reviews-present' : ''}`}>
        {/* <Review reviewbody={"Was eating it and thought it tasted a little bit funny. Found out Jesse put on his super secret blue salt seasoning. TBH had me tweaking like a mug. Fun little crack out at the theaters yuh. Was eating it and thought it tasted a little bit funny. Found out Jesse put on his super secret blue salt seasoning. TBH had me tweaking like a mug. Fun little crack out at the theaters yuh. Was eating it and thought it tasted a little bit funny. Found out Jesse put on his super secret blue salt seasoning. TBH had me tweaking like a mug. Fun little crack out at the theaters yuh. Was eating it and thought it tasted a little bit funny. Found out Jesse put on his super secret blue salt seasoning. TBH had me tweaking like a mug. Fun little crack out at the theaters yuh. Was eating it and thought it tasted a little bit funny. Found out Jesse put on his super secret blue salt seasoning. TBH had me tweaking like a mug. Fun little crack out at the theaters yuh."}/> */}
        { reviews.length > 0 ? reviews.map((review, index) => (
          <ReviewWithMovie key={index} reviewwithmovie={review} canUserModerate={userDetails.canModerate} removeReview={removeReview}/>
        )) : <h2>This user hasn't left any reviews!</h2>}
      </div>
    </div>
  )
}

export default ReviewWithMovieContainer