package com.popfilms.popfilmsapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
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
}
