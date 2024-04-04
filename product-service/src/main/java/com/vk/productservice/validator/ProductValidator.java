package com.vk.productservice.validator;

import com.vk.productservice.exception.ProductDescriptionInvalidException;
import com.vk.productservice.exception.ProductNameInvalidException;
import com.vk.productservice.exception.ProductPriceInvalidException;
import com.vk.productservice.dto.ProductRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class ProductValidator {
    public void validateProductRequest(ProductRequest productRequest) {
        Optional<String> nameValidationResult = validateName(productRequest.getName());
        if (nameValidationResult.isPresent()) {
            throw new ProductNameInvalidException(nameValidationResult.get());
        }

        Optional<String> descriptionValidationResult = validateDescription(productRequest.getDescription());
        if (descriptionValidationResult.isPresent()) {
            throw new ProductDescriptionInvalidException(descriptionValidationResult.get());
        }

        Optional<String> priceValidationResult = validatePrice(productRequest.getPrice());
        if (priceValidationResult.isPresent()) {
            throw new ProductPriceInvalidException(priceValidationResult.get());
        }
    }

    private Optional<String> validateName(String name) {
        return Optional.ofNullable(name)
                .filter(n -> !n.isEmpty())
                .map(n -> n.length() < 3 ? "Product name must be at least 3 characters long." : null);
    }

    private Optional<String> validateDescription(String description) {
        return Optional.ofNullable(description)
                .map(d -> d.length() > 255 ? "Product description cannot exceed 255 characters." : null);
    }

    private Optional<String> validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.of("Product Price must be greater than zero.");
        }
        return Optional.empty();
    }

}
