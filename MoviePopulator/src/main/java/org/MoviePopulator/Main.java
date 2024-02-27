package org.MoviePopulator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.MoviePopulator.Models.Movie;
import org.MoviePopulator.Models.MovieCombo;
import org.MoviePopulator.service.HttpService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        HttpService httpService = new HttpService();
        ObjectMapper mapper = new ObjectMapper();
        List<Integer> movieIds = httpService.getTop500MoviesById();

        List<MovieCombo> movies = new ArrayList();

        for (int id : movieIds){
            MovieCombo tempMovie = httpService.getMovieFromId(id);
            if (tempMovie != null && tempMovie.isValid())
            {
                movies.add(tempMovie);
            }
        }


//        This block is for testing output, keep it commented out
//        List<Movie> movies2 = new ArrayList();
//        for (MovieCombo combo : movies){
//            System.out.println(combo.getPosterImage_url() + " : " + combo.getBgImage_url());
//            Movie movie = new Movie(combo.getTitle(), combo.getReleaseYear(), combo.getLength(), combo.getRating(), combo.getGenre(), combo.getPopfilmsRating(), combo.getTmdbRating(), combo.getSummary(), combo.getDirector(), combo.getWriter(), combo.getStudio());
//            movies2.add(movie);
//        }
//        System.out.println(mapper.writeValueAsString(movies2));


        for (MovieCombo combo : movies){
            Movie movie = new Movie(combo.getTitle(), combo.getReleaseYear(), combo.getLength(), combo.getRating(), combo.getGenre(), combo.getPopfilmsRating(), combo.getTmdbRating(), combo.getSummary(), combo.getDirector(), combo.getWriter(), combo.getStudio());
            httpService.postToSpring(combo.getPosterImage_url(), combo.getBgImage_url(), movie);
        }

    }
}