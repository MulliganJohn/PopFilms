import React, {useState, useRef, useEffect} from 'react'
import { useNavigate } from 'react-router-dom';
import './signupform.css'
import { getApiUrl } from '../../Utils/config';

const SignUpForm = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isLoggingIn, setIsLoggingIn] = useState(false);
    const usernameRef = useRef(null);
    const emailRef = useRef(null);
    const passwordRef = useRef(null);
    const [highlightedInput, setHighlightedInput] = useState(null);
    const [usernameHasBeenHighlighted, setUsernameHasBeenHighlighted] = useState(false);
    const [emailHasBeenHighlighted, setEmailHasBeenHighlighted] = useState(false);
    const [passwordHasBeenHighlighted, setPasswordHasBeenHighlighted] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [showError, setShowError] = useState(false);
    const navigate = useNavigate();

    const gotoURL = (url) => {
      navigate(url);
    };

    useEffect(() => {
        const handleFocusChange = () => {
          const activeElement = document.activeElement;
          if (activeElement === usernameRef.current) {
            setHighlightedInput('username');
            setUsernameHasBeenHighlighted(true);
          } else if (activeElement === emailRef.current) {
            setHighlightedInput('email');
            setEmailHasBeenHighlighted(true);
          } else if (activeElement === passwordRef.current) {
            setHighlightedInput('password');
            setPasswordHasBeenHighlighted(true);
          } else {
            setHighlightedInput(null);
          }
        };
    
        document.addEventListener('focusin', handleFocusChange);
        document.addEventListener('focusout', handleFocusChange);
    
        return () => {
          document.removeEventListener('focusin', handleFocusChange);
          document.removeEventListener('focusout', handleFocusChange);
        };
      }, []);

    const isFormValid = isValidUsername(username) && isValidEmail(email) && password.length >= 8 && password.length < 50 && isValidPassword(password)

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
    };
    
      const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    };

    const handleKeyDown = (event) => {
        if (event.keyCode === 13 && isFormValid) {
          register();
        }
    };

    const register = async () => {
        try {
            setIsLoggingIn(true);
            const response = await fetch(`${getApiUrl()}/api/auth/register`, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(getRegistrationDto(username, email, password)),
            });
      
            if (response.ok) {
                login();
            } else {
                setIsLoggingIn(false);
                console.error('Registration failed');
                setErrorMessage(await response.text());
                setShowError(true);
            }
          } catch (error) {
            setIsLoggingIn(false);
            console.error('Error during registration:', error);
          }
    }

    const login = async () => {
        try {
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
                gotoURL("../login");
            }
          } catch (error) {
            setIsLoggingIn(false);
            console.error('Error during registration:', error);
            gotoURL("../login")
          }
    }


  return (
    <div className='signupform'>
    <div className='signupform-wrapper'>
        <header className='signupform-header'>
            <h1>Create Account</h1>
        </header>
        <div className='signupform-form'>
            <div className='signupform-input-wrapper'>
                <input ref={usernameRef}  autoComplete="off" type="text" id="username" name="username" value={username} onChange={handleUsernameChange} placeholder="Username" required/>
                {(highlightedInput === 'username' && getUserNameMessage(username) !== null) || (usernameHasBeenHighlighted && getUserNameMessage(username) !== null) ? <p className='signupform-error-text'>{getUserNameMessage(username)}</p>: null}
                <input ref={emailRef} autoComplete="off" type="text" id="email" name="email" value={email} onChange={handleEmailChange} placeholder="Email" required/>
                {(emailHasBeenHighlighted && getEmailMessage(email) !== null && highlightedInput !== 'email') ? <p className='signupform-error-text'>{getEmailMessage(email)}</p>: null}
                <input ref={passwordRef} type="password" id="password" name="password" value={password} onChange={handlePasswordChange} onKeyDown={handleKeyDown} placeholder="Password" required/>
                {(highlightedInput === 'password' && getPasswordMessage(password) !== null) || (passwordHasBeenHighlighted && getPasswordMessage(password) !== null) ? <p className='signupform-error-text'>{getPasswordMessage(password)}</p>: null}
            </div>
            <div className='signupform-error'>
                {showError ? <p className='signupform-error-text'>{errorMessage}</p> : null}
            </div>
            <div className='signupform-submit-wrapper'>
                <button
                    disabled={isLoggingIn || !isFormValid} 
                    onClick={register}>
                    {isLoggingIn ? 'Creating Account...' : 'Create Account'}
                </button>
                <p>Already have an account? <a href="../login" style={{color: '#15A9FC', textDecoration: 'none'}}>Login</a></p>
            </div>
        </div>
    </div>
</div>
  )

  function isValidPassword(password) {
    let containsDigit = false;
    let containsUpperCase = false;
    let containsLowerCase = false;

    for (let i = 0; i < password.length; i++) {
        let c = password.charAt(i);

        if (!containsUpperCase && c === c.toUpperCase() && c !== c.toLowerCase()) {
            containsUpperCase = true;
        }

        if (!containsDigit && /\d/.test(c)) {
            containsDigit = true;
        }

        if (!containsLowerCase && c === c.toLowerCase() && c !== c.toUpperCase()) {
            containsLowerCase = true;
        }

        if (containsLowerCase && containsDigit && containsUpperCase) {
            return true;
        }
    }

    return false;
}

function isValidEmail(email) {
    if (!email) {
        return false;
    }

    if (email.length < 6 || email.length > 320) {
        return false;
    }

    const atIndex = email.indexOf('@');
    if (atIndex === -1 || atIndex === 0 || atIndex === email.length - 1) {
        return false;
    }

    const lastDotIndex = email.lastIndexOf('.');
    if (lastDotIndex === -1 || lastDotIndex < atIndex + 2 || lastDotIndex === email.length - 1) {
        return false;
    }

    return true;
}

function isValidUsername(username) {
    const MIN_LENGTH = 3;
    const MAX_LENGTH = 20;
  
    if (!username) {
      return false;
    }
  
    const length = username.length;
    if (length < MIN_LENGTH || length > MAX_LENGTH) {
      return false;
    }
  
    const regex = /^[a-zA-Z0-9_]+$/;
    if (!regex.test(username)) {
      return false;
    }
  
    return true;
  }

function getUserNameMessage(username){
    const MIN_LENGTH = 3;
    const MAX_LENGTH = 20;
  
    const length = username.length;

    if (length === 0 && highlightedInput !== "username"){
        return "Please enter your username!";
    }

    if (length > MAX_LENGTH) {
      return "Username must be less than 20 characters!";
    }

    if (length < MIN_LENGTH){
        return "Username must be at least 3 characters!"
    }
  
    const regex = /^[a-zA-Z0-9_]+$/;
    if (!regex.test(username)) {
      return "Username must contain only alphanumeric characters!"
    }
  
    return null;
}

function getPasswordMessage(password){
    let containsDigit = false;
    let containsUpperCase = false;
    let containsLowerCase = false;

    if (password.length === 0 && highlightedInput !== "password"){
        return "Please enter your password!";
    }

    for (let i = 0; i < password.length; i++) {
        let c = password.charAt(i);

        if (!containsUpperCase && c === c.toUpperCase() && c !== c.toLowerCase()) {
            containsUpperCase = true;
        }

        if (!containsDigit && /\d/.test(c)) {
            containsDigit = true;
        }

        if (!containsLowerCase && c === c.toLowerCase() && c !== c.toUpperCase()) {
            containsLowerCase = true;
        }

        if (containsLowerCase && containsDigit && containsUpperCase && password.length > 8) {
            return null;
        }
    }

    if (!containsDigit){
        return "Password must contain at least one digit!";
    }

    if (!containsUpperCase){
        return "Password must contain at least one uppercase letter!";
    }

    if (!containsLowerCase){
        return "Password must contain at least one lowercase letter!"
    }
    
    if (password.length < 8){
        return "Password must be at least 8 characters long!"
    }

    return null;
}

function getEmailMessage(email){
    if (email.length === 0 && highlightedInput !== "email"){
        return "Please enter your email address!"
    }

    if (email.length < 6) {
        return "Email address must be at least 6 characters!"
    }

    const atIndex = email.indexOf('@');
    if (atIndex === -1 || atIndex === 0 || atIndex === email.length - 1) {
        return "Email address is invalid. Please enter a valid email address!"
    }

    const lastDotIndex = email.lastIndexOf('.');
    if (lastDotIndex === -1 || lastDotIndex < atIndex + 2 || lastDotIndex === email.length - 1) {
        return "Email address is invalid. Please enter a valid email address"
    }
    return null;
}

function getRegistrationDto(username, email, password){
    return {
        username: username,
        emailAddress: email,
        password: password,
    }
}

function getLoginDto(username, password){
    return {
        username: username,
        password: password
    }
}

}

export default SignUpForm