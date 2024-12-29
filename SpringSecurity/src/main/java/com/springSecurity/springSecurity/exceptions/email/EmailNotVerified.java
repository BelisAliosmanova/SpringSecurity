package com.springSecurity.springSecurity.exceptions.email;

import com.springSecurity.springSecurity.exceptions.common.BadRequestException;

/**
 * Exception thrown to indicate that the user's email has not been verified.
 * Extends BadRequestException and sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class EmailNotVerified extends BadRequestException {
    public EmailNotVerified() {
        super("Не можете да влезете, имейлът, даден по време на регистрацията, не е потвърден!");
    }
}
