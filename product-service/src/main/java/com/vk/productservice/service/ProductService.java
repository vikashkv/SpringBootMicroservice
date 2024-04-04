package com.vk.productservice.service;

import com.mongodb.MongoException;
import com.vk.productservice.exception.ProductDataAccessException;
import com.vk.productservice.model.Product;
import com.vk.productservice.dto.ProductRequest;
import com.vk.productservice.dto.ProductResponse;
import com.vk.productservice.repository.ProductRepository;
import com.vk.productservice.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    public Product createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        try {
            return productRepository.save(product);
        } catch (MongoException e) {
            throw new ProductDataAccessException("Failed to save product", e);
        }
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
