package com.popfilms.popfilmsapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCheckDto {

    private Integer rating;
    private String title;
    private String body;
    private Long epoch;
    private Long movieId;
    private Long reviewId;
    private boolean isUserRecognized;
    private boolean doesReviewExist;
    public static ReviewCheckDto getEmptyUserReviewCheck(boolean userRecognized){
        ReviewCheckDto reviewCheckDto = new ReviewCheckDto();
        reviewCheckDto.isUserRecognized = userRecognized;
        reviewCheckDto.doesReviewExist = false;
        return reviewCheckDto;
    }

}
