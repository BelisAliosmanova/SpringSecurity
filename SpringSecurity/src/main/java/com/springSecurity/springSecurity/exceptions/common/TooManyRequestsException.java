package com.springSecurity.springSecurity.exceptions.common;

import org.springframework.http.HttpStatus;

public class TooManyRequestsException extends ApiException {
    public TooManyRequestsException() {
        super("Твърде много заявки. Моля, опитайте отново по-късно.", HttpStatus.TOO_MANY_REQUESTS);
    }
}
