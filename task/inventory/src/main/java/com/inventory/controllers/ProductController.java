package com.inventory.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.inventory.data.models.Product;
import com.inventory.dtos.requests.ProductDto;
import com.inventory.exceptions.InventoryException;
import com.inventory.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {


    @Autowired
    ProductService productService;

    @GetMapping()
    public ResponseEntity<?> findAllProducts(){

        List<Product> productList = productService.getAllProducts();

        return ResponseEntity.ok().body(productList);
    }

    @PostMapping("/product")
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto){

        try{
            Product savedProduct = productService.createProduct(productDto);
            return ResponseEntity.ok().body(savedProduct);
        }
        catch (InventoryException |IllegalArgumentException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody JsonPatch patch){
        try{
            Product updatedProduct = productService.updateProduct(id, patch);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        } catch (JsonPatchException | JsonProcessingException | InventoryException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }

    }

}
