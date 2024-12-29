package com.springSecurity.springSecurity.security.filters;

import com.springSecurity.springSecurity.exceptions.user.UserNotFoundException;
import com.springSecurity.springSecurity.models.dto.auth.PublicUserDTO;
import com.springSecurity.springSecurity.repositories.TokenRepository;
import com.springSecurity.springSecurity.services.JwtService;
import com.springSecurity.springSecurity.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * Filter responsible for JWT-based authentication.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Key to retrieve user information from request attribute.
     */
    public static final String USER_KEY = "user";
    public static final String JWT_KEY = "jwt";

    private final JwtService jwtService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getServletPath();

        // Skip authentication for specific paths related to authentication process
        if (path.contains("/api/v1/auth") || path.contains("/api/v1/oauth2")) {
            filterChain.doFilter(request, response);
            return;
        }

        request.setAttribute(USER_KEY, null);
        request.setAttribute(JWT_KEY, null);

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        if (jwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String userEmail;

        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (Exception exception) {
            filterChain.doFilter(request, response);
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;

            try {
                userDetails = userService.findByEmail(userEmail);
            } catch (UserNotFoundException exception) {
                filterChain.doFilter(request, response);
                return;
            }

            // Check if token is valid and not revoked or expired
            boolean isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);

            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {

                // Set user authentication in security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            // Set user details in request attribute
            request.setAttribute(USER_KEY, modelMapper.map(userDetails, PublicUserDTO.class));
            request.setAttribute(JWT_KEY, jwt);
        }

        filterChain.doFilter(request, response);
    }
}
