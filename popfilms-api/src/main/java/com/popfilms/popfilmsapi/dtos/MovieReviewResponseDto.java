package com.popfilms.popfilmsapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieReviewResponseDto {

    private Integer rating;
    private String title;
    private String body;
    private Long epoch;
    private UserReview user;
    private Long reviewId;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class UserReview {
        private long id;
        private String username;
        private int totalReviews;
    }

    public void setUser(long id, String username, int totalReviews){
        UserReview userReview = new UserReview(id, username, totalReviews);
        this.user = userReview;
    }

}
