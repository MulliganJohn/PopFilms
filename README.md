# PopFilms

PopFilms is a simple website for rating and exploring various movies. It is built using React, Springboot, Express, and MySql. The purpose of this website is to serve as an example.

You can find a link to the demo here: https://popfilms.xyz

## Front-End: React

### Features
- Uses react-router-dom to create multiple pages with dynamic routing.
- Doesn't use any inbuilt design packages such as bootstrap.
- Uses all custom made components.


## Back-End: Springboot

### Features
- Uses a custom built token authentication system.
- Implements spring security to secure endpoints via a users role and their custom authentication token.
- Securely stores user password data using Bcrypt.
- Connects to a MySQL database and implements ORM with Spring JPA and Hibernate.

## CDN: Emulated with express
### Info
Since this website stores and needs to display a lot of images, I wanted to emulate some sort of CDN rather than storing the images directly in MySQL database. I created a simple server with Express to do that. The Express server is very basic, and doesn't involve any sort of caching like a regular CDN. It just returns an images through HTTP get requests or adds images with HTTP POST requests.

:warning: If you plan to use this implementation, one thing to be aware of is that the post endpoint /addMovieImages needs to be accessible only from the springboot service. I used NGINX to block requests that weren't GET requests.

## Running this locally
### If you wish to run this locally, there are a few things you need to do:
  1. Setup a MySqlDB schema and modify application.properties in the springboot directory (popfilms-api/src/main/resources/application.properties)
  
  2. Run the springboot application to populate the schema with the necessary tables / foreign keys.
  
  3. Manually setup roles ```{id 1 : ROLE_USER, id 2 : ROLE_MOD, id 3 : ROLE_ADMIN}```
  
  4. The handling of updating review ratings is done through database triggers, run the dbtriggers.sql on your database in /MYSQL to set them up.
  
  5. To add movies to the database using the /addMovie endpoint the request is a multipart form with 3 fields: poster_image, bg_image, and movie_data. Realistically, you will need some sort of tool and movie api to import movies. See MoviePopulator below.
  
  6. Once you have done this setup, you should be able to run all three services locally and you should see the website via localhost:3000

## MoviePopulator
### Info
- The MoviePopulator is a tool to assist in adding movies to your movie database. It retrieves movie images / data from the TMDB API and sends this information to the springboot /addMovie endpoint in order to add movies.
### Using
- To use the MoviePopulator, you need to first get a TMDB API key that you can add to line 24 on httpservice.java - paste it where ```{YOUR API KEY HERE}``` is.
- In getTop500MoviesById() there is a for loop ```for (int i = 1; i < 30; i++)``` 30 is the number of pages of movies you will get. You can change this to increase or decrease the movies in your database. 50 ~= 1000 movies.
- You can change the URL in the getTop500MoviesById() method also if you want to sort and filter the types of movies you import. Look at the TMDB API to figure out possible URL query parameters.

## Hosting Information
- This website (demo url: https://popfilms.xyz) is hosted on a free-tier Amazon AWS EC2 instance.
- I run each service (api, image server, front-end) using TMUX with a window for each of the services.
- I setup NGINX as a reverse proxy to route each request based on server name =>
  - api.popfilms.xyz -> localhost:8080 (springboot app)
  - images.popfilms.xyz -> localhost:8081 (image server)
  - popfilms.xyz -> localhost:3000 (react)

## Images

<p align="center">
    <img src="https://github.com/MulliganJohn/PopFilms/blob/main/Website-Images/homepage.png?raw=true" alt="The Popfilms Homepage."/>
</p>

<p align="center">
    Figure 1: This is the homepage for the popfilms website.
</p>

<p align="center">
    <img src="https://github.com/MulliganJohn/PopFilms/blob/main/Website-Images/moviepage.png?raw=true" alt="The Popfilms Homepage."/>
</p>

<p align="center">
    Figure 2: This is the movie page for the popfilms website.
</p>

<p align="center">
    <img src="https://github.com/MulliganJohn/PopFilms/blob/main/Website-Images/moviepage_review.png?raw=true" alt="The Popfilms Homepage."/>
</p>

<p align="center">
    Figure 3: This is moviepage when leaving a review.
</p>

<p align="center">
    <img src="https://github.com/MulliganJohn/PopFilms/blob/main/Website-Images/moviepage_with_review.png?raw=true" alt="The Popfilms Homepage."/>
</p>

<p align="center">
    Figure 4: This is the movie page with a review added.
</p>

<p align="center">
    <img src="https://github.com/MulliganJohn/PopFilms/blob/main/Website-Images/userpage.png?raw=true" alt="The Popfilms Homepage."/>
</p>

<p align="center">
    Figure 5: This is the user page for the popfilms website.
</p>

<p align="center">
    <img src="https://github.com/MulliganJohn/PopFilms/blob/main/Website-Images/loginpage.png?raw=true" alt="The Popfilms Homepage."/>
</p>

<p align="center">
    Figure 6: This is the login page for the popfilms website.
</p>

<p align="center">
    <img src="https://github.com/MulliganJohn/PopFilms/blob/main/Website-Images/registerpage.png?raw=true" alt="The Popfilms Homepage."/>
</p>

<p align="center">
    Figure 7: This is the registration page for the popfilms website.
</p>
