package com.popfilms.popfilmsapi.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "movies")
@AllArgsConstructor
@NoArgsConstructor
public class MovieEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "release_year")
    private int releaseYear;

    @Column(name = "length")
    private int length;

    @Column(name = "rating")
    private String rating;

    @Column(name = "popfilms_rating", precision = 5, scale = 3)
    private BigDecimal popfilmsRating;

    @Column(name = "tmdb_rating", precision = 5, scale = 3)
    private BigDecimal tmdbRating;

    @Column(name = "summary", length = 1500)
    private String summary;

    @Column(name = "total_ratings")
    private Integer totalRatings;

    @Column(name = "rating_sum")
    private Integer ratingSum;

    @OneToMany(mappedBy = "movie")
    @EqualsAndHashCode.Exclude
    private Set<ReviewEntity> reviews = new HashSet<>();

    //public void addRating(Integer rating){
        //totalRatings++;
        //ratingSum += rating;
        //popfilmsRating = BigDecimal.valueOf(ratingSum).divide(BigDecimal.valueOf(totalRatings), 1, RoundingMode.HALF_UP);
    //}

    //public void removeRating(Integer rating){
        //totalRatings--;
        //ratingSum -= rating;
        //if (totalRatings > 0){
            //popfilmsRating = BigDecimal.valueOf(ratingSum).divide(BigDecimal.valueOf(totalRatings), 1, RoundingMode.HALF_UP);
        //}
        //else {
            //popfilmsRating = BigDecimal.ZERO;
        //}
    //}



    @ManyToMany
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<GenreEntity> genres = new HashSet<>();

    @ManyToMany
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "movie_writers",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "writer_id")
    )
    private Set<WriterEntity> writers = new HashSet<>();

    @ManyToMany
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "movie_directors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    private Set<DirectorEntity> directors = new HashSet<>();

    @ManyToMany
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "movie_studios",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "studio_id")
    )
    private Set<StudioEntity> studios = new HashSet<>();

    public void addGenre(GenreEntity genre) {
        genres.add(genre);
        genre.getMovies().add(this);
    }

    public void addWriter(WriterEntity writer) {
        writers.add(writer);
        writer.getMovies().add(this);
    }

    public void addDirector(DirectorEntity director) {
        directors.add(director);
        director.getMovies().add(this);
    }

    public void addStudio(StudioEntity studio) {
        studios.add(studio);
        studio.getMovies().add(this);
    }

    public MovieEntity(String title, int releaseYear, int length, String rating, BigDecimal popfilmsRating, BigDecimal tmdbRating, String summary){
        this.title = title;
        this.releaseYear = releaseYear;
        this.length = length;
        this.rating = rating;
        this.popfilmsRating = popfilmsRating;
        this.summary = summary;
        this.tmdbRating = tmdbRating;
    }
}
