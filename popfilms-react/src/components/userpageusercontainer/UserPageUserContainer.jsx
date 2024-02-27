import React from 'react'
import './userpageusercontainer.css'
import userPNG from '../../Assets/default-user.jpg'
import convertEpochToDate from '../../Utils/dateUtils';

const UserPageUserContainer = ({UserHeader}) => {
  return (
    <div className='usercontainer'>
              <img
                src={userPNG}
                alt="userFiller"
                className="user-img"
              />
              <div className='user-page-info'>
                <h1>{UserHeader.username}</h1>
                <p>Join Date: {convertEpochToDate(UserHeader.joinEpoch)}</p>
                <p>Total Reviews: {UserHeader.totalReviews}</p>
              </div>
    </div>
  )
}

export default UserPageUserContainer