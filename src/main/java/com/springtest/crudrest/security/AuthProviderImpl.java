package com.springtest.crudrest.security;

import com.springtest.crudrest.dto.AuthenticationDto;
import com.springtest.crudrest.services.LibrarianDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthProviderImpl implements AuthenticationProvider {
    private final LibrarianDetailService librarianDetailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthProviderImpl(LibrarianDetailService librarianDetailService, PasswordEncoder passwordEncoder) {
        this.librarianDetailService = librarianDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        UserDetails librarianDetails = librarianDetailService.loadUserByUsername(username);

        String enteredPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(enteredPassword, librarianDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }
        return new UsernamePasswordAuthenticationToken(librarianDetails, librarianDetails.getPassword(), Collections.emptyList());
    }

    public void checkAuthenticationElseThrow(AuthenticationDto authenticationDto) throws AuthenticationException {
        UserDetails librarianDetails = librarianDetailService.loadUserByUsername(authenticationDto.getUsername());
        if (!passwordEncoder.matches(authenticationDto.getPassword(), librarianDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    public LibrarianDetailService getService() {return librarianDetailService;}
}
