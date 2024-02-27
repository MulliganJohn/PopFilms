package org.MoviePopulator.Models;

import okhttp3.MultipartBody;

import java.math.BigDecimal;
import java.util.List;

public class MovieCombo {
    private String title;

    private int releaseYear;

    private int length;

    private String rating;

    private List<String> genre;

    private BigDecimal popfilmsRating;
    private BigDecimal tmdbRating;

    private String summary;

    private List<String> director;

    private String bgImage_url;

    private List<String> writer;

    private List<String> studio;

    private String posterImage_url;

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

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
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

    public List<String> getDirector() {
        return director;
    }

    public void setDirector(List<String> director) {
        this.director = director;
    }

    public String getBgImage_url() {
        return bgImage_url;
    }

    public void setBgImage_url(String bgImage_url) {
        this.bgImage_url = bgImage_url;
    }

    public List<String> getWriter() {
        return writer;
    }

    public void setWriter(List<String> writer) {
        this.writer = writer;
    }

    public List<String> getStudio() {
        return studio;
    }

    public void setStudio(List<String> studio) {
        this.studio = studio;
    }

    public String getPosterImage_url() {
        return posterImage_url;
    }

    public void setPosterImage_url(String posterImage_url) {
        this.posterImage_url = posterImage_url;
    }

    public MovieCombo() {
    }

    public MovieCombo(String title, int releaseYear, int length, String rating, List<String> genre, BigDecimal popfilmsRating, BigDecimal tmdbRating, String summary, List<String> director, String bgImage_url, List<String> writer, List<String> studio, String posterImage_url) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.length = length;
        this.rating = rating;
        this.genre = genre;
        this.popfilmsRating = popfilmsRating;
        this.summary = summary;
        this.director = director;
        this.bgImage_url = bgImage_url;
        this.writer = writer;
        this.studio = studio;
        this.posterImage_url = posterImage_url;
        this.tmdbRating = tmdbRating;
    }

    public boolean isValid()
    {
        if (writer.isEmpty() || studio.isEmpty() || director.isEmpty() || rating.equals("") || genre.isEmpty() || length == 0){
            return false;
        }
        return true;

    }

}
