package com.inventory.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.inventory.data.models.Product;
import com.inventory.data.repositories.ProductRepository;
import com.inventory.dtos.requests.ProductDto;
import com.inventory.exceptions.InventoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductRepository productRepository;
    @Override
    public Product createProduct(ProductDto productDto) throws InventoryException {
        Product product = new Product();
        //product dto is not null
        validateDtoContent(productDto);
        mapProductDtoToProduct(productDto, product);
        return productRepository.save(product);
    }

    private void validateDtoContent(ProductDto productDto) throws InventoryException {
        if ( productDto == null ) {
            throw new IllegalArgumentException("Product information cannot be empty!");
        }
        if ( productRepository.findProductByName(productDto.getName()) != null ) throw new InventoryException("Product already exists");
    }

    private void mapProductDtoToProduct(ProductDto productDto, Product product) {
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setDescription(productDto.getDescription());
    }



    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductById(Long id) throws InventoryException {
        if ( id == null ){
            throw new IllegalArgumentException("Id cannot be null");
        }
        Optional<Product> optionalProduct = productRepository.findById(id);
        if ( optionalProduct.isPresent()) return optionalProduct.get();

        throw new InventoryException("Product with id: "+id+" does not exist");
    }

    @Override
    public Product updateProduct(Long productId, JsonPatch patch) throws  InventoryException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if ( optionalProduct.isEmpty() ) throw new InventoryException("Product with ID "+productId+" does not exist");
        Product targetProduct = optionalProduct.get();

        return getProduct(patch, targetProduct);
    }

    private Product getProduct(JsonPatch patch, Product targetProduct) throws InventoryException {
        try{
            targetProduct = applyPatchToProduct(patch, targetProduct);
            return productRepository.save(targetProduct);

        }catch (JsonPatchException| JsonProcessingException exception){
            throw new InventoryException("Update failed");
        }
    }

    private Product applyPatchToProduct(JsonPatch patch, Product targetProduct) throws JsonProcessingException, JsonPatchException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode patched = patch.apply(mapper.convertValue(targetProduct, JsonNode.class));

        return mapper.treeToValue(patched, Product.class);
    }}
