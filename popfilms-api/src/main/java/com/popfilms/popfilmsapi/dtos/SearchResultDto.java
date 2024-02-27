package com.popfilms.popfilmsapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDto {
    private Long id;
    private String title;
    private int releaseYear;
    private int length;
}
