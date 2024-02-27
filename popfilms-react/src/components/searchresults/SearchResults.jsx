import React, {useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import './searchresults.css'
import { getImageUrl } from '../../Utils/config';

const SearchResults = React.memo(({anchorWidthRef, searching, searchResults, resetSearch}) => {
    const navigate = useNavigate();
    const [componentWidth, setComponentWidth] = useState(400);

    const gotoMovie = (id) => {
        navigate('/movies/' + id);
        resetSearch();
    };

    const handleMouseDown = (event) =>{
        event.preventDefault();
    }

    const handleOuterDivClick = (event) => {
        event.preventDefault();
    }

    useEffect(() => {
        const resizeObserver = new ResizeObserver((entries) => {
          for (const entry of entries) {
            const { width } = entry.contentRect;
            const paddingLeft = parseInt(getComputedStyle(anchorWidthRef.current).paddingLeft, 10);
            setComponentWidth(width + paddingLeft);
          }
        });
    
        if (anchorWidthRef.current) {
          resizeObserver.observe(anchorWidthRef.current);
        }
    
        return () => {
          resizeObserver.disconnect(); // Cleanup when component unmounts
        };
      }, [anchorWidthRef]);

  return (
    <div className='popfilms-searchresults' style={{width: `${componentWidth}px`}}onMouseDown={handleMouseDown} onClick={handleOuterDivClick}>
        {searching ? 
        <div style={{height: '150px', display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
            <div className='popfilms-loader'/>
        </div> :
        searchResults.length > 0 ? 
            searchResults.map(result => (
                <div key={result.id} className='popfilms-searchresults-result' onMouseDown={handleMouseDown} onClick={gotoMovie.bind(this, result.id)}>
                    <img className='searchResultImage' key={result.id} alt={result.id} src={`${getImageUrl()}/thumbnails/${String (result.id)}.jpg`}/>
                    <div className='popfilms-searchresults-result-right'>
                        <p>{result.title}</p>
                        <p style={{color: 'rgba(255, 255, 255, 0.5)'}}>{result.releaseYear} • {formatMovieLength(result.length)}</p>
                    </div>
                </div>
            ))
        :         
        <div style={{height: '150px', display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
            <p>No results</p>
        </div>}
    </div>
  )
})

export default SearchResults


const formatMovieLength = (len) => {
    const hours = Math.floor(len / 60);
    const minutes = len % 60;
  
    return `${hours}h ${minutes}m`;
  };