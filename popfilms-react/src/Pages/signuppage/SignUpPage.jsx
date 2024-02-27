import React from 'react'
import './signuppage.css'
import SignUpForm from '../../components/signupform/SignUpForm'
import logo from '../../Assets/logo.png'
import { useNavigate } from 'react-router-dom';

const SignUpPage = () => {
    const navigate = useNavigate();
    const gotoURL = (url) => {
        navigate(url);
    };
  return (
    <div className='signuppage'>
        <header className='signuppage-header'>
            <img src={logo} alt='logo' className='signuppage-logo' onClick={gotoURL.bind(this, '/')}/>
        </header>
        <main className='signuppage-main'>
            <SignUpForm/>
        </main>
    </div>
  )
}

export default SignUpPage