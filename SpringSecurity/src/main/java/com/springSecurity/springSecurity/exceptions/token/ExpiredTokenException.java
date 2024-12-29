package com.springSecurity.springSecurity.exceptions.token;

import com.springSecurity.springSecurity.exceptions.common.UnauthorizedException;

public class ExpiredTokenException extends UnauthorizedException {
    public ExpiredTokenException() {
        super("Токенът е изтекъл!");
    }
}
