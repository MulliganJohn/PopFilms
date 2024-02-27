import React, {useEffect, useState} from 'react'
import './navbaruser.css'
import { useNavigate } from 'react-router-dom';
import userPNG from '../../Assets/default-user.jpg'
import arrow from "../../Assets/uparrow.png"
import { getApiUrl } from '../../Utils/config';

const NavbarUser = ({setUserDetails}) => {
    const [loggedIn, setLoggedIn] = useState(false);
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [userId, setUserId] = useState("");
    const [loading, setLoading] = useState(true);
    const [dropDownVisible, setDropDownVisible] = useState(false);
    const navigate = useNavigate();

    const gotoURL = (url) => {
        navigate(url);
    };

    async function signIn(){
        try{
            const response = await fetch(`${getApiUrl()}/api/users/signin`, {
                method: 'POST',
                credentials: 'include'
              });
            const responseJson = await response.json();
            setUserDetails(responseJson);
            if (responseJson.userRecognized){
                setUsername(responseJson.username);
                setEmail(responseJson.email);
                setUserId(responseJson.userId);
                setLoggedIn(true);
                setLoading(false);
            }
            else{
                setLoading(false);
            }
        } catch (error){
            console.log(error);
        }
    } 

    useEffect(() =>{
        signIn();
    },[])

    const toggleDropDown = () => {
        setDropDownVisible(!dropDownVisible);
    }

    const handleLogout = async () => {
        try{
            const response = await fetch(`${getApiUrl()}/api/users/signout`, {
                method: 'POST',
                credentials: 'include'
              });
            if (response.ok){
                setLoggedIn(false);
            }
        } catch (error){
            console.log(error);
        }
    }

    const handleViewProfile = async () => {
        gotoURL(`/users/${userId}`);
    }


    if (!loggedIn && !loading){
        return (
            <div className='popfilms__navbaruser_loggedout'>
                <button id='signinbtn' type='button' onClick={gotoURL.bind(this, '/login')}>Sign In</button>
                <button id='signupbtn' type='button'onClick={gotoURL.bind(this, '/signup')}>Sign Up</button>
            </div>
          )
    }
    else if (loggedIn){
        return (
            <div className='popfilms__navbaruser'>
                <div className='popfilms__navbaruser_loggedin' onClick={toggleDropDown}>
                    <img id='userPNG' src={userPNG} alt='userPNG'/>
                    <p>{username}</p>
                    <img id='arrowDropDown' src={arrow} alt='arrow'/>
                </div>
                {dropDownVisible ? <div className='popfilms__navbaruser_dropdown'>
                    <ul id='navbaruser_dropdown_list'>
                        <li>Hello, {username}</li>
                        <li onClick={handleViewProfile}>View Profile</li>
                        <li></li>
                        <li onClick={handleLogout}>Logout</li>
                    </ul>
                </div> : null}
            </div>
        )
    }
}

export default NavbarUser