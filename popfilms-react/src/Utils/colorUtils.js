function darkenColor(rgbColor, brightness) {
    const maxValue = Math.max(rgbColor[0], rgbColor[1], rgbColor[2]);
    const scaleFactor = brightness / maxValue;
  
    const darkenedColor = rgbColor.map(component => Math.round(component * scaleFactor));
  
    return darkenedColor;
  }
  
  export default darkenColor;