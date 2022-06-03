package com.inventory.dtos.responses;

import lombok.Builder;
import lombok.Data;
import com.inventory.data.models.Item;

import java.util.List;

@Data
@Builder
public class OrderResponseDto {
    private List<Item> itemList;
    private double totalPrice;
}
