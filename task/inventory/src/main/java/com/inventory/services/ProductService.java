package com.inventory.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.inventory.data.models.Product;
import com.inventory.dtos.requests.ProductDto;
import com.inventory.exceptions.InventoryException;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product findProductById(Long id) throws InventoryException;
    Product createProduct(ProductDto productDto) throws InventoryException;
    Product updateProduct(Long productId, JsonPatch patch) throws JsonPatchException, JsonProcessingException, InventoryException;



}
