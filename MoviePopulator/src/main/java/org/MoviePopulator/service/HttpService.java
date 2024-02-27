package org.MoviePopulator.service;

import okhttp3.*;
import okhttp3.MultipartBody;
import okio.BufferedSink;
import okio.Okio;
import org.MoviePopulator.Models.Movie;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.MoviePopulator.Models.MovieCombo;
import org.apache.commons.io.FileUtils;

public class HttpService {

    private static String movieImageUrl = "https://image.tmdb.org/t/p/original/";
    private static String apiKey = "Bearer {YOUR API KEY HERE}";
    OkHttpClient client = new OkHttpClient();
    public Movie[] getMovieObj() throws IOException {

        return new Movie[10];
    }

    public List<Integer> getTop500MoviesById() throws IOException {
        List<Integer> movieIds = new ArrayList();

        for (int i = 1; i < 30; i++)
        {
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&region=US&sort_by=revenue.desc&watch_region=US&with_original_language=en&page=" + i)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", apiKey)
                    .build();
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseString);
            JsonNode resultsNode = jsonNode.get("results");
            for (JsonNode Movie : resultsNode) {
                int movieId = Movie.get("id").asInt();
                movieIds.add(movieId);
            }

        }

        return movieIds;
    }

    public MovieCombo getMovieFromId(int id) throws IOException {
        MovieCombo retMovie = new MovieCombo();
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + id + "?language=en-US" + "&append_to_response=credits,release_dates")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", apiKey)
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseString);

        JsonNode genres = jsonNode.get("genres");
        List<String>movieGenres = new ArrayList<>();
        for (JsonNode Genre : genres) {
            movieGenres.add(Genre.get("name").asText());
        }

        retMovie.setGenre(movieGenres);

        JsonNode studios = jsonNode.get("production_companies");
        List<String>movieStudios = new ArrayList<>();
        for (JsonNode Studio : studios) {
            movieStudios.add(Studio.get("name").asText());
        }

        retMovie.setStudio(movieStudios);

        int movieLength = jsonNode.get("runtime").asInt();

        retMovie.setLength(movieLength);

        retMovie.setTmdbRating(BigDecimal.valueOf(jsonNode.get("vote_average").asDouble()));

        retMovie.setPopfilmsRating(BigDecimal.ZERO);

        String releaseYear = "";

        if (jsonNode.get("release_date").asText().indexOf('-') != -1){
            releaseYear = (jsonNode.get("release_date").asText().substring(0, jsonNode.get("release_date").asText().indexOf('-')));
        }
        try{
            retMovie.setReleaseYear(Integer.parseInt(releaseYear));
        }
        catch (Exception e)
        {
            return null;
        }

        retMovie.setSummary(jsonNode.get("overview").asText());

        retMovie.setTitle(jsonNode.get("title").asText());

        String movieRating = "";
        JsonNode release_dates = jsonNode.get("release_dates");
        JsonNode results = release_dates.get("results");

        for (JsonNode Results : results) {
            if (Results.get("iso_3166_1").asText().equals("US")){
                results = Results.get("release_dates");
                for (JsonNode cert : results){
                    if (!cert.get("certification").asText().equals(""))
                    {
                        movieRating += cert.get("certification").asText();
                        break;
                    }
                }
            }
        }

        retMovie.setRating(movieRating);

        JsonNode creds = jsonNode.get("credits");

        JsonNode cast = creds.get("crew");

        List<String> director = new ArrayList<>();
        for (JsonNode member : cast)
        {
            if (member.get("job") != null)
            {
                if (member.get("job").asText().equals("Director")){
                    director.add(member.get("name").asText());
                }
            }
        }

        retMovie.setDirector(director);

        List<String> writer = new ArrayList<>();
        for (JsonNode member : cast)
        {
            if (member.get("job") != null)
            {
                if (member.get("job").asText().equals("Story") || member.get("job").asText().equals("Writer") || member.get("job").asText().equals("Novel") || member.get("job").asText().equals("Writing") || member.get("department").asText().equals("Writing")){
                    if (!writer.contains(member.get("name").asText()))
                    {
                        writer.add(member.get("name").asText());
                    }
                }
            }
        }

        retMovie.setWriter(writer);

        retMovie.setPosterImage_url("https://image.tmdb.org/t/p/original" + jsonNode.get("poster_path").asText());
        retMovie.setBgImage_url("https://image.tmdb.org/t/p/original" + jsonNode.get("backdrop_path").asText());
        retMovie.setTitle(jsonNode.get("title").asText());

        return retMovie;
    }


    public void postToSpring(String poster_image_url, String bg_image_url, Movie movie) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File bgFile = File.createTempFile("tempbg", ".jpg");
        File posterFile = File.createTempFile("tempposter", ".jpg");
        FileUtils.copyURLToFile(new URL(poster_image_url), posterFile);
        FileUtils.copyURLToFile(new URL(bg_image_url), bgFile);



        RequestBody requestBody = new MultipartBody.Builder()
                .setType((MultipartBody.FORM))
                .addFormDataPart("poster_image", "tempfile.jpg",RequestBody.create(MultipartBody.FORM, posterFile))
                .addFormDataPart("bg_image", "tempfile.jpg",RequestBody.create(MultipartBody.FORM, bgFile))
                .addFormDataPart("movie_data", objectMapper.writeValueAsString(movie))
                .build();

        Request request = new Request.Builder()
                .url("http://localhost:8080/api/movies/addMovie")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        response.close();
    }

}
