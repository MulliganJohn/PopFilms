import React, {useState} from 'react'
import { useNavigate } from 'react-router-dom';
import './loginform.css'
import { getApiUrl } from '../../Utils/config';

const Loginform = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isLoggingIn, setIsLoggingIn] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [showError, setShowError] = useState(false);
    const isFormValid = username.length > 0 && password.length > 0;
    const navigate = useNavigate();

    const gotoURL = (url) => {
      navigate(url);
    };

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
    };
    
      const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const handleKeyDown = (event) => {
        if (event.keyCode === 13 && isFormValid) {
          login();
        }
    };

    const login = async () => {
        try {
            setIsLoggingIn(true);
            const response = await fetch(`${getApiUrl()}/api/auth/login`, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(getLoginDto(username, password)),
              credentials: 'include'
            });
      
            if (response.ok) {
                setIsLoggingIn(false);
                gotoURL("../");
            } else {
                setIsLoggingIn(false);
                console.error('Login failed');
                setErrorMessage(await response.text());
                setShowError(true);
            }
          } catch (error) {
            setIsLoggingIn(false);
            console.error('Error during registration:', error);
          }
    }

  return (
    <div className='loginform'>
        <div className='loginform-wrapper'>
            <header className='loginform-header'>
                <h1>Welcome Back</h1>
            </header>
            <div className='loginform-form'>
                <div className='loginform-input-wrapper'>
                    <input autoComplete="off" type="text" id="username" name="username" value={username} onChange={handleUsernameChange} placeholder="Username" required/>
                    <input type="password" id="password" name="password" value={password} onChange={handlePasswordChange} onKeyDown={handleKeyDown} placeholder="Password" required/>
                </div>
                <div className='signupform-error'>
                    {showError ? <p className='signupform-error-text'>{errorMessage}</p> : null}
                </div>
                <div className='loginform-submit-wrapper'>
                    <button
                        disabled={isLoggingIn || !isFormValid} 
                        onClick={login}>
                        {isLoggingIn ? 'Logging In...' : 'Login'}
                    </button>
                    <p>Don't have an account? <a  href="../signup" style={{color: '#15A9FC', textDecoration: 'none'}}>Sign Up</a></p>
                </div>
            </div>
        </div>
    </div>
  )


function getLoginDto(username, password){
    return {
        username: username,
        password: password
    }
}


}

export default Loginform