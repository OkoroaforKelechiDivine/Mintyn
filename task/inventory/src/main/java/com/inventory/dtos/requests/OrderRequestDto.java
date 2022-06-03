package com.inventory.dtos.requests;

import lombok.Data;

@Data
public class OrderRequestDto {
    private Long userId;
    private Long productId;
    private int quantity;

}
