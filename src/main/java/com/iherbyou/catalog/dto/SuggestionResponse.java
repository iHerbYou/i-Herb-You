package com.iherbyou.catalog.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class SuggestionResponse {

    private List<SuggestionDto> categories;
    private List<SuggestionDto> products;
    private Long redirectCategoryId;

}