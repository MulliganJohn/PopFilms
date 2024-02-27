import React, { useState, useEffect } from 'react'
import './reviewcontainer.css'
import Review from '../review/Review'
import ReviewForm from '../reviewform/ReviewForm'
import { useParams } from 'react-router-dom';
import EditForm from '../editform/EditForm';
import { getApiUrl } from '../../Utils/config';

const ReviewContainer = ({userDetails}) => {
  const [isReviewFormVisble, setIsReviewFormVisible] = useState(false);
  const [isEditFormVisble, setIsEditFormVisible] = useState(false);
  const [reviews, setReviews] = useState([]);
  const { MovieId } = useParams();
  const [reviewStatus, setReviewStatus] = useState("");
  const [reviewCheckDto, setReviewCheckDto] = useState();

  const showReviewForm = () => {
    setIsReviewFormVisible(true);
  };

  const showEditForm = () => {
    setIsEditFormVisible(true);
  };

  const hideReviewForm = () => {
    setIsReviewFormVisible(false);
  };

  const hideEditForm = () => {
    setIsEditFormVisible(false);
  };

  useEffect(() => {
    if (isReviewFormVisble) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = 'visible';
    }

    return () => {
      document.body.style.overflow = 'visible';
    };
  }, [isReviewFormVisble]);

  async function fetchReviews(){
    const response = await fetch(`${getApiUrl()}/api/reviews/getMovieReviews/${MovieId}`, {
        method: 'GET',
        credentials: 'include'
      });
    if (response.ok){
        setReviews(await response.json());
    }
  }
  
  async function checkForPreviousReview(){
    const response = await fetch(`${getApiUrl()}/api/reviews/checkUserReview/${MovieId}`, {
        method: 'GET',
        credentials: 'include'
      });
    if (response.ok){
        const ReviewCheckDto = await response.json();
        setReviewCheckDto(ReviewCheckDto);
        if (ReviewCheckDto.userRecognized === true){
            if (ReviewCheckDto.doesReviewExist === true){
              setReviewStatus("edit");
            }
            else{
              setReviewStatus("default");
            }
        }
        else{
            setReviewStatus("login");
        }
    }
  }

  useEffect(() => {
    fetchReviews();
    checkForPreviousReview();
  }, [MovieId])

  return (
    <div className='popfilms__reviewcontainer'>
      <div className='review_header'>
        <h1 className='review_header-title'>Reviews</h1>
        {reviewStatus === "default" ?  (<button id='reviewButton' type='button' onClick={showReviewForm}>
              Leave a Review
              </button>)
        : reviewStatus === "edit" ? (<button id='reviewButton' type='button' onClick={showEditForm}>
              Edit Review
              </button>)
        : reviewStatus === "login" ? (<button id='reviewButton' type='button' onClick={showReviewForm}>
              <a href="../../login" style={{ textDecoration: 'none', color: 'inherit' }}>
                  Login To Review!
              </a>
              </button>)    
          : null  
        }
      </div>
      {reviewStatus === "default" ? <ReviewForm active={isReviewFormVisble} hide={hideReviewForm} refetch={fetchReviews} recheckReview={checkForPreviousReview} setReviewStatus={setReviewStatus} /> : null}
      {reviewStatus === "edit" ? <EditForm active={isEditFormVisble} hide={hideEditForm} reviewCheckDto={reviewCheckDto} refetch={fetchReviews} recheckReview={checkForPreviousReview} setReviewStatus={setReviewStatus}/> : null}
      <div className={`popfilms__review-wrapper ${reviews.length > 0 ? 'reviews-present' : ''}`}>
        {/* <Review reviewbody={"Was eating it and thought it tasted a little bit funny. Found out Jesse put on his super secret blue salt seasoning. TBH had me tweaking like a mug. Fun little crack out at the theaters yuh. Was eating it and thought it tasted a little bit funny. Found out Jesse put on his super secret blue salt seasoning. TBH had me tweaking like a mug. Fun little crack out at the theaters yuh. Was eating it and thought it tasted a little bit funny. Found out Jesse put on his super secret blue salt seasoning. TBH had me tweaking like a mug. Fun little crack out at the theaters yuh. Was eating it and thought it tasted a little bit funny. Found out Jesse put on his super secret blue salt seasoning. TBH had me tweaking like a mug. Fun little crack out at the theaters yuh. Was eating it and thought it tasted a little bit funny. Found out Jesse put on his super secret blue salt seasoning. TBH had me tweaking like a mug. Fun little crack out at the theaters yuh."}/> */}
        { reviews.length > 0 ? reviews.map((review, index) => (
          <Review key={index} review={review} canUserModerate={userDetails.canModerate} refetch={fetchReviews} recheckReview={checkForPreviousReview} setReviewStatus={setReviewStatus}/>
        )) : <h2>There arent any reviews yet!</h2>}
      </div>
    </div>
  )
}

export default ReviewContainer