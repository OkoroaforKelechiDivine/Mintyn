package com.inventory.controllers;

import com.inventory.dtos.requests.OrderRequestDto;
import com.inventory.dtos.requests.OrderUpdateDto;
import com.inventory.dtos.responses.OrderResponseDto;
import com.inventory.exceptions.InventoryException;
import com.inventory.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("order")
    public ResponseEntity<?> orderItem(@RequestBody OrderRequestDto orderRequestDto){
        OrderResponseDto orderResponseDto;
        try{
            orderResponseDto = orderService.orderItem(orderRequestDto);
        } catch (InventoryException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    @PatchMapping("/update-order")
    public ResponseEntity<?> updateOrderItems(@RequestBody OrderUpdateDto updateDto){
        try{
            OrderResponseDto responseDto = orderService.updateOrderItem(updateDto);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (InventoryException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping( "/publish")
    public void sendMessageToKafka(@RequestParam ("message") String message) {
        this.orderService.sendMessage(message);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getOrderById(@PathVariable("userId") Long id){
        try{
            OrderResponseDto responseDto = orderService.viewOrder(id);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (InventoryException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
