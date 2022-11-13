package com.beside.special.config.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.beside.special.config.SpecialJWTConfiguration;
import com.beside.special.exception.AuthorizationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AuthorizationFilter extends OncePerRequestFilter {
    private final SpecialJWTConfiguration specialJWTConfiguration;
    private final ObjectMapper objectMapper;

    public AuthorizationFilter(SpecialJWTConfiguration specialJWTConfiguration, ObjectMapper objectMapper) {
        this.specialJWTConfiguration = specialJWTConfiguration;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {
        try {
            String authorization = request.getHeader("authorization");
            if (authorization == null) {
                throw new AuthorizationException("Invalid token");
            }

            String token = authorization.split(" ")[1];

            if (token == null) {
                throw new AuthorizationException("Invalid token");
            }
            JWT.require(Algorithm.HMAC256(specialJWTConfiguration.getSecret())).build()
                .verify(token);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            objectMapper.writeValue(response.getWriter(), errorDetails);
        }
    }
}
