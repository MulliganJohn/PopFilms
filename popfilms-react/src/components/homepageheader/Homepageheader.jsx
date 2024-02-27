import React from 'react'
import './homepageheader.css'
import HomePageHeaderBg from '../../Assets/HomepageHeaderBg.png'

const Homepageheader = () => {
  return (
    <div className='popfilms__homepage-header'>
        <div className='popfilms__homepage-header-text'>
            <h1>Welcome to the site!</h1>
            <p>Look up and find details and information about popular US movies.</p>
        </div>
        <img src={HomePageHeaderBg} alt="bgImage" className="Bg-image-right"/>
    </div>
  )
}

export default Homepageheader