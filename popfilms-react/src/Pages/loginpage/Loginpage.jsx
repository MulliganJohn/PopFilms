import React from 'react'
import './loginpage.css'
import Loginform from '../../components/loginform/Loginform'
import { useNavigate } from 'react-router-dom';
import logo from '../../Assets/logo.png'

const Loginpage = () => {
    const navigate = useNavigate();
    const gotoURL = (url) => {
        navigate(url);
      };


  return (
    <div className='loginpage'>
        <header className='loginpage-header'>
            <img src={logo} alt='logo' className='loginpage-logo' onClick={gotoURL.bind(this, '/')}/>
        </header>
        <main className='loginpage-main'>
            <Loginform/>
        </main>
    </div>
  )
}

export default Loginpage