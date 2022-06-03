package com.inventory.controllers;

import com.inventory.dtos.requests.CustomerRequestDto;
import com.inventory.dtos.responses.CustomerResponseDto;
import com.inventory.exceptions.InventoryException;
import com.inventory.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody CustomerRequestDto customerRequestDto) {
        try {
            CustomerResponseDto responseDto = customerService.createUser(customerRequestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (InventoryException exception) {
            return ResponseEntity.badRequest().body(exception);
        }
    }
}