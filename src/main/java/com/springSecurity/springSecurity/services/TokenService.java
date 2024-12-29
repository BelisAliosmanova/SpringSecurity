package com.springSecurity.springSecurity.services;

import com.springSecurity.springSecurity.enums.TokenType;
import com.springSecurity.springSecurity.models.dto.auth.AuthenticationResponse;
import com.springSecurity.springSecurity.models.entity.Token;
import com.springSecurity.springSecurity.models.entity.User;

import java.util.List;

public interface TokenService {
    Token findByToken(String jwt);

    List<Token> findByUser(User user);

    void saveToken(User user, String jwtToken, TokenType tokenType);

    void revokeToken(Token token);

    void revokeAllUserTokens(User user);

    void logoutToken(String jwt);

    AuthenticationResponse generateAuthResponse(User user);

    void createVerificationToken(User user, String token);

    void clearVerificationTokensByUser(User user);
}
