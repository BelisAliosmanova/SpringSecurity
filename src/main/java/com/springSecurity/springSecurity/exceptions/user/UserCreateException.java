package com.springSecurity.springSecurity.exceptions.user;

import com.springSecurity.springSecurity.exceptions.common.BadRequestException;

/**
 * Exception thrown when there is an issue creating a user, either due to invalid data or duplicate user details.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class UserCreateException extends BadRequestException {

    /**
     * Constructs a UserCreateException with a message indicating either a duplicate email or invalid user data.
     */
    public UserCreateException(boolean isUnique) {
        super(
                isUnique
                        ? "Вече съществува потребител със същия имейл!"
                        : "Невалидни потребителски данни!"
        );
    }
}
