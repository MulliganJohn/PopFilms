import {React , useState, useRef, useEffect} from 'react';
import { useNavigate } from 'react-router-dom';
import './navbar.css';
import logo from '../../Assets/logo.png';
import NavbarUser from '../navbaruser/NavbarUser';
import SearchResults from '../searchresults/SearchResults';
import { getApiUrl } from '../../Utils/config';

const Navbar = ({setDetails}) => {
  const navigate = useNavigate();
  const [query, setQuery] = useState("");
  const [searchHighlighted, setSearchHighlighted] = useState(false);
  const searchRef = useRef(null);
  const showSearch  = query.length > 0 && searchHighlighted;

  const gotoURL = (url) => {
      navigate(url);
  };

  const handleFocus = () => {
    setSearchHighlighted(true);
  }

  const handleBlur = () => {
      setSearchHighlighted(false);
  }
  
  const handleQueryChange = (event) => {
    setQuery(event.target.value);
  }

  const [searchResults, setSearchResults] = useState([]);
  const [searching, setSearching] = useState(false);

  useEffect(() => {
      setSearching(true);
      if(searchHighlighted){
        const timerId = setTimeout(async () => {
          await callApiSearch(query);
          setSearching(false);
        }, 500);
    
        // Cleanup function to clear the existing timeout
        return () => clearTimeout(timerId);
      }
    }, [query]);

    const resetSearch = () => {
      setSearchHighlighted(false);
      setQuery("");
      setSearchResults([]);
      setSearching(false);
      document.activeElement.blur();
    }

    const callApiSearch = async (query) => {
      try {
        const response = await fetch(`${getApiUrl()}/api/movies/search?query=${query}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        });
  
        if (response.ok) {
          setSearchResults(await response.json());
        } else {
            setSearchResults([]);
        }
      } catch (error) {
        setSearchResults([]);
        console.error('Error during registration:', error);
      }
    }


  return (
    <div className='popfilms__navbar'>
      <div className='popfilms__navbar-left'>
          <img src={logo} alt='logo' onClick={gotoURL.bind(this, '/')}/>
          <input type="movie" placeholder="Find a Movie" value={query} onChange={handleQueryChange} ref={searchRef} onFocus={handleFocus} onBlur={handleBlur} />
      </div>
      <NavbarUser setUserDetails={setDetails}/>
      {showSearch && <SearchResults anchorWidthRef={searchRef} searching={searching} searchResults={searchResults} resetSearch={resetSearch}/>}
    </div>
  )
}

export default Navbar