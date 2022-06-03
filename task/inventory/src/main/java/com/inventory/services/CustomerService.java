package com.inventory.services;

import com.inventory.dtos.requests.CustomerRequestDto;
import com.inventory.dtos.responses.CustomerResponseDto;
import com.inventory.exceptions.InventoryException;

public interface CustomerService {
    CustomerResponseDto createUser(CustomerRequestDto customerRequestDto) throws InventoryException;
}
