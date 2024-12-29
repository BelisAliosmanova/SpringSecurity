package com.springSecurity.springSecurity.exceptions.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate internal server errors.
 * Extends ApiException and sets the appropriate message and HTTP status code.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
@Getter
@Setter
public class InternalServerErrorException extends ApiException {
    private RuntimeException innerRuntimeException;

    public InternalServerErrorException(RuntimeException runtimeException) {
        super("Вътрешна грешка на сървъра!", HttpStatus.INTERNAL_SERVER_ERROR);
        setInnerRuntimeException(runtimeException);
    }

    public InternalServerErrorException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
        setInnerRuntimeException(new RuntimeException(message));
    }
}
