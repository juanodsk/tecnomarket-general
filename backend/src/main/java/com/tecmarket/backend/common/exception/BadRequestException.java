package com.tecmarket.backend.common.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String messageKey) {
        super(messageKey);
    }
}
