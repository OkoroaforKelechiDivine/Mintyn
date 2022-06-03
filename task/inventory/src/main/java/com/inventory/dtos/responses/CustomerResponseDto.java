package com.inventory.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CustomerResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
}
