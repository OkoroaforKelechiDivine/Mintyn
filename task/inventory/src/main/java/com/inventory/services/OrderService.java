package com.inventory.services;

import com.inventory.dtos.requests.OrderRequestDto;
import com.inventory.dtos.requests.OrderUpdateDto;
import com.inventory.dtos.responses.OrderResponseDto;
import com.inventory.exceptions.InventoryException;

public interface OrderService {
    OrderResponseDto orderItem(OrderRequestDto orderRequestDto) throws InventoryException;
    OrderResponseDto viewOrder(Long userId) throws InventoryException;
    OrderResponseDto updateOrderItem(OrderUpdateDto orderUpdateDto) throws InventoryException;
    void sendMessage(String details);
}
