package com.vk.productservice;

import com.vk.productservice.controller.ProductController;
import com.vk.productservice.dto.ProductRequest;
import com.vk.productservice.exception.InvalidRequestException;
import com.vk.productservice.exception.ProductDescriptionInvalidException;
import com.vk.productservice.exception.ProductNameInvalidException;
import com.vk.productservice.exception.ProductPriceInvalidException;
import com.vk.productservice.model.Product;
import com.vk.productservice.service.ProductService;
import com.vk.productservice.validator.ProductValidator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
class ProductServiceApplicationTests {
    @Mock
    private ProductService productService;

    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private ProductController productController;

    @Test
    void testCreateProduct_Success() throws InvalidRequestException, ProductNameInvalidException,
            ProductDescriptionInvalidException, ProductPriceInvalidException {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setDescription("Test Description");
        productRequest.setPrice(BigDecimal.valueOf(100.0)); // Set whatever value is needed for testing
        when(productService.createProduct(productRequest)).thenReturn(new Product());
        // Act
        ResponseEntity<?> responseEntity = productController.createProduct(productRequest);
        // Assert
        verify(productValidator).validateProductRequest(productRequest);
        verify(productService).createProduct(productRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    void testCreateProduct_InvalidRequestException() throws InvalidRequestException, ProductNameInvalidException,
            ProductDescriptionInvalidException, ProductPriceInvalidException {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Invalid Product");
        // Set other fields as needed for testing

        doThrow(new InvalidRequestException("Invalid request")).when(productValidator).validateProductRequest(productRequest);

        // Act
        ResponseEntity<?> responseEntity = productController.createProduct(productRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid request", responseEntity.getBody());
    }

}
