package com.inventory.dtos.requests;

import lombok.Builder;
import lombok.Data;
import com.inventory.dtos.QuantityOperation;

@Data
@Builder
public class OrderUpdateDto {
    private Long userId;
    private Long itemId;
    private QuantityOperation quantityOperation;
}
