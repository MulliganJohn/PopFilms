package org.MoviePopulator.Models;

import java.math.BigDecimal;
import java.util.List;

public class Movie {

    private String title;

    private int releaseYear;

    private int length;

    private String rating;

    private List<String> genres;

    private BigDecimal popfilmsRating;

    private BigDecimal tmdbRating;

    private String summary;

    private List<String> directors;

    private List<String> writers;

    private List<String> studios;

    public Movie() {
    }

    public Movie(String title, int releaseYear, int length, String rating, List<String> genres, BigDecimal popfilmsRating, BigDecimal tmdbRating, String summary, List<String> directors, List<String> writers, List<String> studios) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.length = length;
        this.rating = rating;
        this.genres = genres;
        this.popfilmsRating = popfilmsRating;
        this.summary = summary;
        this.directors = directors;
        this.writers = writers;
        this.studios = studios;
        this.tmdbRating = tmdbRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public BigDecimal getPopfilmsRating() {
        return popfilmsRating;
    }

    public void setPopfilmsRating(BigDecimal popfilmsRating) {
        this.popfilmsRating = popfilmsRating;
    }

    public BigDecimal getTmdbRating() {
        return tmdbRating;
    }

    public void setTmdbRating(BigDecimal tmdbRating) {
        this.tmdbRating = tmdbRating;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public List<String> getWriters() {
        return writers;
    }

    public void setWriters(List<String> writers) {
        this.writers = writers;
    }

    public List<String> getStudios() {
        return studios;
    }

    public void setStudios(List<String> studio) {
        this.studios = studios;
    }
}
