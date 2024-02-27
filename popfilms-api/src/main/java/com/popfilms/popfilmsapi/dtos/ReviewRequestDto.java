package com.popfilms.popfilmsapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDto {

    private String title;

    private String body;

    private Integer rating;

    private Long movieId;

}
