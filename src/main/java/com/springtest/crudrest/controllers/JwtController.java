package com.springtest.crudrest.controllers;

import com.springtest.crudrest.dto.AuthenticationDto;
import com.springtest.crudrest.security.AuthProviderImpl;
import com.springtest.crudrest.security.JwtUtil;
import com.springtest.crudrest.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jwt")
public class JwtController {
    private final AuthProviderImpl authProviderImpl;
    private final JwtUtil jwtUtil;

    public JwtController(AuthProviderImpl authProviderImpl, JwtUtil jwtUtil) {
        this.authProviderImpl = authProviderImpl;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public String generateJwt (@RequestBody AuthenticationDto authenticationDto) {
        authProviderImpl.checkAuthenticationElseThrow(authenticationDto);
        return jwtUtil.generateToken(authenticationDto.getUsername());
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(AuthenticationException ex) {
        ErrorResponse response = new ErrorResponse(
                "Invalid username or password",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
