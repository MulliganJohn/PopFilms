import React, {useEffect, useState} from 'react';
import Movieheader from '../../components/movieheader/Movieheader'
import Reviewcontainer from '../../components/reviewcontainer/Reviewcontainer'
import { useParams } from 'react-router-dom';
import Vibrant from 'node-vibrant';
import './moviepage.css'
import darkenColor from '../../Utils/colorUtils';
import chroma from 'chroma-js';
import Navbar from '../../components/navbar/Navbar';
import { getImageUrl } from '../../Utils/config';

const MoviePage = () => {
  let { MovieId } = useParams();
  const [colors, setColors] = useState(null);
  const [showCanvas, setShowCanvas] = useState(false);
  const [accentcolor1, setaccentcolor1] = useState(`rgb(29, 30, 31)`);
  const [accentcolor2, setaccentcolor2] = useState(`rgb(29, 30, 31)`);
  const [userDetails, setUserDetails] = useState({canModerate: false});
  const [loadGradient, setLoadGradient] = useState(false);
  const [bgLoaded, setBgLoaded] = useState(false);
  const [posterLoaded, setPosterLoaded] = useState(false);

  useEffect(() => {
    window.scrollTo(0, 0)
  }, [])

  useEffect(() => {
    const imageUrl = `${getImageUrl()}/images/${String (MovieId)}.jpg`;
    Vibrant.from(imageUrl).maxColorCount(35).quality(1).getPalette((err, palette) => {
      if (err) {
        return;
      }
      const darkenedColors = [chroma(darkenColor(palette.Vibrant.getRgb(), 60)).set('hsl.s', .35).rgb(), chroma(darkenColor(palette.Muted.getRgb(), 87)).set('hsl.s', .15).rgb()]

      setColors(darkenedColors);
      setShowCanvas(true);
    });
  }, [MovieId]);

  useEffect(() => {
    if (bgLoaded && posterLoaded){
      setLoadGradient(true);
    }
  }, [bgLoaded, posterLoaded])

  useEffect(() => {
    if (colors && loadGradient){
      transitionGradients(accentcolor1, `rgb(${colors[0][0]}, ${colors[0][1]}, ${colors[0][2]})`, accentcolor2, `rgb(${colors[1][0]}, ${colors[1][1]}, ${colors[1][2]})`)
    }
  }, [colors, loadGradient])

  const transitionGradients = async (oldAccent1, newAccent1, oldAccent2, newAccent2) => {
      transformAccentColor1(parseRGB(oldAccent1), parseRGB(newAccent1), 50);
      transformAccentColor2(parseRGB(oldAccent2), parseRGB(newAccent2), 50);
  }

  async function transformAccentColor1(rgbStart, rgbEnd, duration, step = 1) {
    const calculateNextValue = (current, target) => {
      if (current < target) {
        return Math.min(current + step, target);
      } else if (current > target) {
        return Math.max(current - step, target);
      }
      return current;
    };
  
    const transform = async () => {
      while (
        rgbStart.r !== rgbEnd.r ||
        rgbStart.g !== rgbEnd.g ||
        rgbStart.b !== rgbEnd.b
      ) {
        rgbStart.r = calculateNextValue(rgbStart.r, rgbEnd.r);
        rgbStart.g = calculateNextValue(rgbStart.g, rgbEnd.g);
        rgbStart.b = calculateNextValue(rgbStart.b, rgbEnd.b);
  
        setaccentcolor1(`rgb(${rgbStart.r}, ${rgbStart.g}, ${rgbStart.b})`);
  
        await sleep(duration);
      }
    };
    await transform();
  }

  async function transformAccentColor2(rgbStart, rgbEnd, duration, step = 1) {
    const calculateNextValue = (current, target) => {
      if (current < target) {
        return Math.min(current + step, target);
      } else if (current > target) {
        return Math.max(current - step, target);
      }
      return current;
    };
  
    const transform = async () => {
      while (
        rgbStart.r !== rgbEnd.r ||
        rgbStart.g !== rgbEnd.g ||
        rgbStart.b !== rgbEnd.b
      ) {
        rgbStart.r = calculateNextValue(rgbStart.r, rgbEnd.r);
        rgbStart.g = calculateNextValue(rgbStart.g, rgbEnd.g);
        rgbStart.b = calculateNextValue(rgbStart.b, rgbEnd.b);
  
        setaccentcolor2(`rgb(${rgbStart.r}, ${rgbStart.g}, ${rgbStart.b})`);
  
        await sleep(duration);
      }
    };
    await transform();
  }


  const backgroundStyle = {
    background: `linear-gradient(135deg, ${accentcolor1} 0%, ${accentcolor2} 100%)`
  };
  
  return (
    <div className="moviepage">
      {loadGradient && <canvas className='blur' style={backgroundStyle}/>}
      <Navbar setDetails={setUserDetails}/>
      <main className='moviepage-content'>
        <img
          src={`${getImageUrl()}/backgrounds/${String (MovieId)}.jpg`}
          alt="Background Art"
          className="background-image"
          onLoad={setBgLoaded}
        />
        <div className='moviepage_body'>
          <Movieheader setLoadGradient={setLoadGradient} setPosterLoaded={setPosterLoaded}/>
          <Reviewcontainer style={{ zIndex: 1 }} userDetails={userDetails}/>
        </div>
      </main>
    </div>
  )
}

export default MoviePage


function parseRGB(rawColor) {
  const values = rawColor.replace(/rgb\(|\)/g, '').split(',').map(value => parseInt(value.trim(), 10));

  const rgbObject = {
    r: values[0],
    g: values[1],
    b: values[2],
  };

  return rgbObject;
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}