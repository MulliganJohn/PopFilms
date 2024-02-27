package com.popfilms.popfilmsapi.dtos;

import com.popfilms.popfilmsapi.Entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageDto {
    private UserHeader userHeader;
    private List<UserReviewResponseDto> reviews;
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class UserHeader {
        private String username;
        private Long joinEpoch;
        private int totalReviews;
    }

    public void setUserHeader(String username, Long joinEpoch, int totalReviews){
        UserHeader userHeader = new UserHeader(username, joinEpoch, totalReviews);
        this.userHeader = userHeader;
    }



}
