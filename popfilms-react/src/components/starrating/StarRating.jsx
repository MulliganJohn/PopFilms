import React, { useState, useEffect } from 'react';
import './starrating.css'
import emptystar from '../../Assets/starempty.png'
import filledstar from '../../Assets/starfilled.png'

const StarRating = ({onStarRating, savedrating}) => {
  const [rating, setRating] = useState(0);
  const [hoveredRating, setHoveredRating] = useState(0);

  const handleStarHover = (star) => {
    setHoveredRating(star);
  };

  const handleStarClick = (star) => {
    setRating(star);
    onStarRating(star);
  };

  const handleStarLeave = () => {
    setHoveredRating(0);
  };

  useEffect(() => {
    setRating(savedrating)
    }, [savedrating]);

  return (
    <div className='popfilms__starrating'>
      {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((star) => (
        <img
          key={star}
          src={star <= (hoveredRating || rating) ? filledstar : emptystar}
          alt={`Star ${star}`}
          onMouseEnter={() => handleStarHover(star)}
          onClick={() => handleStarClick(star)}
          onMouseLeave={handleStarLeave}
          style={{ width: '25px', height: '25px', cursor: 'pointer', padding: '2.5px' }}
        />
      ))}
    </div>
  );
};

export default StarRating;