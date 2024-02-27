package com.popfilms.popfilmsapi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popfilms.popfilmsapi.Entity.MovieEntity;
import com.popfilms.popfilmsapi.Service.MovieService;
import com.popfilms.popfilmsapi.dtos.ReviewRequestDto;
import com.popfilms.popfilmsapi.dtos.SearchResultDto;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.popfilms.popfilmsapi.dtos.MovieDto;
import com.popfilms.popfilmsapi.Entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"})
    @PostMapping("/api/movies/addMovie")
    public ResponseEntity<String> addMovie(@RequestParam("poster_image") MultipartFile poster_image, @RequestParam("bg_image") MultipartFile bg_image, @RequestParam("movie_data") String movieJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MovieDto movieDto = objectMapper.readValue(movieJson, MovieDto.class);
            MovieEntity savedMovie = movieService.addMovie(movieDto, poster_image, bg_image);
            return new ResponseEntity<>(savedMovie.toString(), HttpStatus.OK);
        }
        catch (Exception e) {
            // create some way to log error here
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
        }
    }

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"})
    @GetMapping("/api/movies/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        try {
            MovieEntity foundMovie = movieService.getMovieById(id);
            List<String> genreNamesList = foundMovie.getGenres().stream()
                    .map(GenreEntity::getName)
                    .collect(Collectors.toList());
            List<String> directorNamesList = foundMovie.getDirectors().stream()
                    .map(DirectorEntity::getName)
                    .collect(Collectors.toList());
            List<String> writerNamesList = foundMovie.getWriters().stream()
                    .map(WriterEntity::getName)
                    .collect(Collectors.toList());
            List<String> studioNamesList = foundMovie.getStudios().stream()
                    .map(StudioEntity::getName)
                    .collect(Collectors.toList());
            MovieDto returnMovie = new MovieDto(foundMovie.getTitle(), foundMovie.getReleaseYear(), foundMovie.getLength(), foundMovie.getRating(), genreNamesList, foundMovie.getPopfilmsRating(), foundMovie.getTmdbRating(), foundMovie.getSummary(), directorNamesList, writerNamesList, studioNamesList);
            return ResponseEntity.status(HttpStatus.OK).body(returnMovie);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            //create some way to log the error.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"})
    @GetMapping("/api/movies/getTop30Ids")
    public ResponseEntity<List<Long>> getTop30MovieIds() {
        try {
            List<Long> top30Movies = movieService.getTop30MovieIds();
            return ResponseEntity.status(HttpStatus.OK).body(top30Movies);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"})
    @GetMapping("/api/movies/get30RandomIds")
    public ResponseEntity<List<Long>> get30RandomIds() {
        try {
            List<Long> randomIds = movieService.get30RandomIds();
            return ResponseEntity.status(HttpStatus.OK).body(randomIds);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"})
    @GetMapping("/api/movies/search")
    public ResponseEntity<List<SearchResultDto>> getMoviesBySearch(@RequestParam String query){
        try {
            List<SearchResultDto> searchResultDtoList = movieService.getMoviesBySearch(query);
            return ResponseEntity.status(HttpStatus.OK).body(searchResultDtoList);
        }
        catch (Exception e){
            logger.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
