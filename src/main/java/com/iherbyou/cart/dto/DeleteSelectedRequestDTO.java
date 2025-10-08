package com.iherbyou.cart.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class DeleteSelectedRequestDTO {
    private List<Long> cartProductIds;
}