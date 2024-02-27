import React, {useState} from 'react'
import Moviecontainer from '../../components/moviecontainer/Moviecontainer';
import Homepageheader from '../../components/homepageheader/Homepageheader';
import './homepage.css'
import Navbar from '../../components/navbar/Navbar';


const Homepage = () => {
  const [userDetails, setUserDetails] = useState({canModerate: false});
  return (
    <div className="homepage">
      <Navbar setDetails={setUserDetails}/>
      <Homepageheader/>
      <div className='homepage_body'>
        <Moviecontainer title={'Top Rated Movies'} endpoint={'api/movies/getTop30Ids'} id="1"/>
        <Moviecontainer title={'Pick a Random Movie'} endpoint={'api/movies/get30RandomIds'} id="2"/>
      </div>
    </div>
  )
}

export default Homepage