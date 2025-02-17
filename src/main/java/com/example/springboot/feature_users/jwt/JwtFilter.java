package com.example.springboot.feature_users.jwt;

import com.example.springboot.feature_users.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired private JwtUtils jwtUtils;

    @Autowired private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println(
                "JwtFilter - Request: " + request.getMethod() + " " + request.getRequestURI());

        // Bypass JWT filter for authentication endpoints
        String path = request.getServletPath();
        if (path.equals("/users/login") || path.equals("/users/register") || path.startsWith("/actuator")) {
            System.out.println("Skipping JWT filter for " + path);
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authorizationHeader);

        // Validate Authorization header
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("No JWT token found in request, blocking access.");
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: No JWT token found.");
            return;
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtils.extractUsername(token);
        List<String> roles = jwtUtils.extractRoles(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtils.validateToken(token, userDetails.getUsername())) {
                List<SimpleGrantedAuthority> authorities =
                        roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("User authenticated: " + username);
                System.out.println("User roles: " + roles);
            } else {
                System.out.println("Invalid JWT token, blocking access.");
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid JWT.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}