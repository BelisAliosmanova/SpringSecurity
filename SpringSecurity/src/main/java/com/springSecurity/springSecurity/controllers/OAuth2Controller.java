package com.springSecurity.springSecurity.controllers;

import com.springSecurity.springSecurity.models.dto.auth.AuthenticationResponse;
import com.springSecurity.springSecurity.services.OAuth2AuthenticationService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
public class OAuth2Controller {
    private final OAuth2AuthenticationService oAuth2AuthenticationService;

    @GetMapping("/url/google")
    @RateLimiter(name = "sensitive_operations_rate_limiter")
    public ResponseEntity<String> auth() {
        return ResponseEntity.ok(oAuth2AuthenticationService.getOAuthGoogleLoginUrl());
    }

    @GetMapping("/authenticate/google")
    @RateLimiter(name = "sensitive_operations_rate_limiter")
    public ResponseEntity<AuthenticationResponse> googleAuthenticate(@RequestParam("code") String code) {
        return ResponseEntity.ok(oAuth2AuthenticationService.processOAuthGoogleLogin(code));
    }
}
