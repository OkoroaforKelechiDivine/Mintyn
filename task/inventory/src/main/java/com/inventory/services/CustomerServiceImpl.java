package com.inventory.services;

import com.inventory.data.models.Customer;
import com.inventory.data.repositories.CustomerRepository;
import com.inventory.dtos.requests.CustomerRequestDto;
import com.inventory.dtos.responses.CustomerResponseDto;
import com.inventory.exceptions.InventoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public CustomerResponseDto createUser
            (CustomerRequestDto userRequestDto) throws InventoryException {
        //Check if user exists
        Optional<Customer> optionalAppUser =
                customerRepository.findCustomerByPhoneNumber(userRequestDto.getPhoneNumber());
        if ( optionalAppUser.isPresent() )
            throw new InventoryException("This user is present");
        //Create an app user object
        Customer customer = new Customer();
        customer.setFirstName(userRequestDto.getFirstName());
        customer.setLastName(userRequestDto.getLastName());
        customer.setPhoneNumber(userRequestDto.getPhoneNumber());
        //Save object
        customerRepository.save(customer);

        //return response
        return buildResponse(customer);
    }

    private CustomerResponseDto buildResponse(Customer customer){
        return CustomerResponseDto.builder()
                .email(customer.getPhoneNumber())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName()).build();
    }
}
