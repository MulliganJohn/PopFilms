import './App.css';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Homepage from './Pages/homepage/Homepage';
import MoviePage from './Pages/moviepage/MoviePage';
import Loginpage from './Pages/loginpage/Loginpage';
import SignUpPage from './Pages/signuppage/SignUpPage';
import UserPage from './Pages/userpage/UserPage';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/movies/:MovieId" element={<MoviePage />} />
        <Route path="/login" element ={<Loginpage/>}/>
        <Route path="/signup" element={<SignUpPage/>}/>
        <Route path="/users/:UserId" element={<UserPage />} />
      </Routes>
    </Router>
  )
}

export default App;
