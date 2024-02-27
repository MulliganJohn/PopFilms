import React, { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom';
import Navbar from '../../components/navbar/Navbar';
import './userpage.css'
import UserPageUserContainer from '../../components/userpageusercontainer/UserPageUserContainer';
import UserPageTabs from '../../components/userpagetabs/UserPageTabs';
import ReviewWithMovieContainer from '../../components/reviewwithmoviecontainer/ReviewWithMovieContainer';
import { getApiUrl } from '../../Utils/config';

const UserPage = () => {
  let { UserId } = useParams();
  const [reviews, setReviews] = useState();
  const [userHeader, setUserHeader] = useState({});
  const [userDetails, setUserDetails] = useState({canModerate: false});

  async function fetchUserPage(){
    const response = await fetch(`${getApiUrl()}/api/users/getuserpage?id=${UserId}`, {
        method: 'GET',
        credentials: 'include'
      });
    if (response.ok){
        const userPageDto = await response.json();
        setReviews(userPageDto.reviews);
        setUserHeader(userPageDto.userHeader)
    }
} 

  useEffect(() => {
    fetchUserPage();
  }, [])



  return (
    <div className='userpage'>
      <Navbar setDetails={setUserDetails}/>
      <main className='userpage-content'>
        <div className='userpage-body'>
            <UserPageUserContainer UserHeader={userHeader}/>
            <UserPageTabs/>
            {reviews && <ReviewWithMovieContainer reviews={reviews} userDetails={userDetails} setReviews={setReviews}/>}
        </div>
      </main>
    </div>
  )
}

export default UserPage