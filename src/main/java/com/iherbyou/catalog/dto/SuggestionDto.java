package com.iherbyou.catalog.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class SuggestionDto {

    private Long id;
    private String name;
    private String type;    // "category" or "product"

}