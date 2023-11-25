package com.springtest.crudrest.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.springtest.crudrest.security.JwtUtil;
import com.springtest.crudrest.services.LibrarianDetailService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final LibrarianDetailService librariansService;


    public JwtFilter(JwtUtil jwtUtil, LibrarianDetailService librariansService) {
        this.jwtUtil = jwtUtil;
        this.librariansService = librariansService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if (StringUtils.isBlank(jwt)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT is blank in Bearer Header");
            } else {
                try {
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    UserDetails user = librariansService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (JWTVerificationException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
