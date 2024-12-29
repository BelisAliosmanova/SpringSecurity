package com.springSecurity.springSecurity.services.impl.security;

import com.springSecurity.springSecurity.enums.TokenType;
import com.springSecurity.springSecurity.models.dto.auth.AuthenticationResponse;
import com.springSecurity.springSecurity.models.dto.auth.PublicUserDTO;
import com.springSecurity.springSecurity.models.entity.Token;
import com.springSecurity.springSecurity.models.entity.User;
import com.springSecurity.springSecurity.models.entity.VerificationToken;
import com.springSecurity.springSecurity.repositories.TokenRepository;
import com.springSecurity.springSecurity.repositories.VerificationTokenRepository;
import com.springSecurity.springSecurity.services.JwtService;
import com.springSecurity.springSecurity.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation responsible for handling authentication tokens.
 */
@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Override
    public Token findByToken(String jwt) {
        return tokenRepository.findByToken(jwt).orElse(null);
    }

    @Override
    public List<Token> findByUser(User user) {
        return tokenRepository.findAllByUser(user);
    }

    @Override
    public void saveToken(User user, String jwtToken, TokenType tokenType) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(tokenType)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    @Override
    public void revokeToken(Token token) {
        tokenRepository.delete(token);
    }

    @Override
    public void revokeAllUserTokens(User user) {
        tokenRepository.deleteAll(tokenRepository.findAllByUser(user));
    }

    @Override
    @Transactional
    public void logoutToken(String jwt) {
        Token storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);

        if (storedToken == null) {
            return;
        }

        revokeAllUserTokens(storedToken.getUser());
        SecurityContextHolder.clearContext();
    }

    @Override
    public AuthenticationResponse generateAuthResponse(User user) {
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveToken(user, jwtToken, TokenType.ACCESS);
        saveToken(user, refreshToken, TokenType.REFRESH);

        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(modelMapper.map(user, PublicUserDTO.class))
                .build();
    }

    @Override
    @Transactional
    public void createVerificationToken(User user, String token) {
        clearVerificationTokensByUser(user);
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    @Override
    public void clearVerificationTokensByUser(User user) {
        verificationTokenRepository.deleteAllByUser(user);
    }

}
