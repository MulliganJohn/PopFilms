const getBaseURL = () => {
    const protocol = window.location.protocol;
    const hostname = window.location.hostname;

    return `${protocol}//${hostname}`;
};


const URLConfig = {
    development: {
      apiUrl: 'http://localhost:8080',
      imageUrl: 'http://localhost:8081'
    },
    production: {
      apiUrl: `YOUR_URL/IP_HERE`,
      imageUrl: `YOUR_URL/IP_HERE`,
    },
};

const getApiUrl = () => {
    if (process.env.NODE_ENV === 'development') {
        return URLConfig.development.apiUrl;
      } else {
        return URLConfig.production.apiUrl;
      }
}

const getImageUrl = () => {
    if (process.env.NODE_ENV === 'development') {
        return URLConfig.development.imageUrl;
      } else {
        return URLConfig.production.imageUrl;
      }
}
  
export { getApiUrl, getImageUrl };
