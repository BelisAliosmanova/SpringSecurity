package com.springSecurity.springSecurity.services;

import com.springSecurity.springSecurity.exceptions.common.ApiException;

public interface ExceptionService {

    void log(ApiException runtimeException);

    void log(RuntimeException runtimeException, int statusCode);
}
