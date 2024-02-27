package com.popfilms.popfilmsapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewResponseDto {

    private Integer rating;
    private String title;
    private String body;
    private Long epoch;
    private Long movieId;
    private String movieTitle;
    private Long reviewId;
}
