package com.vk.productservice.validator;

import com.vk.productservice.dto.ProductRequest;
import com.vk.productservice.exception.ProductDescriptionInvalidException;
import com.vk.productservice.exception.ProductNameInvalidException;
import com.vk.productservice.exception.ProductPriceInvalidException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductValidatorTest {
    @InjectMocks
    private ProductValidator productValidator;

    @Test
    void testValidateName_Valid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        String name = "Test Product";

        // Get the private method using reflection
        Method method = ProductValidator.class.getDeclaredMethod("validateName", String.class);
        method.setAccessible(true);

        // Act
        var validationResult = method.invoke(productValidator, name);

        // Assert
        assertEquals(validationResult, java.util.Optional.empty());
    }

    @Test
    void testValidateName_Invalid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        String name = "A";

        // Get the private method using reflection
        Method method = ProductValidator.class.getDeclaredMethod("validateName", String.class);
        method.setAccessible(true);

        // Act
        var validationResult = method.invoke(productValidator, name);

        // Assert
        assertEquals(validationResult, java.util.Optional.of("Product name must be at least 3 characters long."));
    }

    @Test
    void testValidateDescription_Valid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

        Method method = ProductValidator.class.getDeclaredMethod("validateDescription", String.class);
        method.setAccessible(true);
        // Act
        var validationResult = method.invoke(productValidator, description);

        // Assert
        assertEquals(validationResult, java.util.Optional.empty());
    }

    @Test
    void testValidateDescription_Invalid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.";

        Method method = ProductValidator.class.getDeclaredMethod("validateDescription", String.class);
        method.setAccessible(true);
        // Act
        var validationResult = method.invoke(productValidator, description);

        // Assert
        assertEquals(validationResult, java.util.Optional.of("Product description cannot exceed 255 characters."));
    }

    @Test
    void testValidatePrice_Valid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        BigDecimal price = BigDecimal.valueOf(10.0);

        Method method = ProductValidator.class.getDeclaredMethod("validatePrice", BigDecimal.class);
        method.setAccessible(true);
        // Act
        var validationResult = method.invoke(productValidator, price);

        // Assert
        assertEquals(validationResult, java.util.Optional.empty());
    }

    @Test
    void testValidatePrice_Invalid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        BigDecimal price = BigDecimal.ZERO;

        Method method = ProductValidator.class.getDeclaredMethod("validatePrice", BigDecimal.class);
        method.setAccessible(true);
        // Act
        var validationResult = method.invoke(productValidator, price);

        // Assert
        assertEquals(validationResult, java.util.Optional.of("Product Price must be greater than zero."));
    }

    @Test
    void testValidateProductRequest_Valid() {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        productRequest.setPrice(BigDecimal.valueOf(10.0));

        // Act & Assert (No exceptions thrown)
        assertDoesNotThrow(() -> productValidator.validateProductRequest(productRequest));
    }

    @Test
    void testValidateProductRequest_InvalidName() {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("A");
        productRequest.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        productRequest.setPrice(BigDecimal.valueOf(10.0));

        // Act & Assert
        assertThrows(ProductNameInvalidException.class, () -> productValidator.validateProductRequest(productRequest));
    }

    @Test
    void testValidateProductRequest_InvalidDescription() {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.");
        productRequest.setPrice(BigDecimal.valueOf(10.0));

        // Act & Assert
        assertThrows(ProductDescriptionInvalidException.class, () -> productValidator.validateProductRequest(productRequest));
    }

    @Test
    void testValidateProductRequest_InvalidPrice() {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        productRequest.setPrice(BigDecimal.ZERO);

        // Act & Assert
        assertThrows(ProductPriceInvalidException.class, () -> productValidator.validateProductRequest(productRequest));
    }
}
