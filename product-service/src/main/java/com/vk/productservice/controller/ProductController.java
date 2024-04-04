package com.vk.productservice.controller;

import com.vk.productservice.exception.*;
import com.vk.productservice.dto.ProductRequest;
import com.vk.productservice.dto.ProductResponse;
import com.vk.productservice.service.ProductService;
import com.vk.productservice.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/product")
@RestController
public class    ProductController {
    private final ProductService productService;
    private final ProductValidator productValidator;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create-product")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            productValidator.validateProductRequest(productRequest);
            return ResponseEntity.ok(productService.createProduct(productRequest));
        } catch (InvalidRequestException | ProductNameInvalidException | ProductDescriptionInvalidException |
                 ProductPriceInvalidException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/get-all-products")
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
}
