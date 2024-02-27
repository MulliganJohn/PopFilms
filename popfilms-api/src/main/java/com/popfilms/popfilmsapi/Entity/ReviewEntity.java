package com.popfilms.popfilmsapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(name = "review_title", length = 500)
    private String reviewTitle;

    @Column(name = "review_body", length = 4096)
    private String reviewBody;

    @Column(name = "review_rating")
    private Integer reviewRating;

    @Column(name = "review_creation_epoch")
    private Long reviewCreationEpoch;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
