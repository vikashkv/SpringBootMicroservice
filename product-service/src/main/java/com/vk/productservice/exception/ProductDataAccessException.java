package com.vk.productservice.exception;

public class ProductDataAccessException extends ProductException {

    public ProductDataAccessException(String message) {
        super(message);
    }

    public ProductDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

}
