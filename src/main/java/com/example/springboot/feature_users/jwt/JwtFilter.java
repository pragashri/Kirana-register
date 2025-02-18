package com.example.springboot.feature_users.jwt;

import static com.example.springboot.feature_users.logConstants.LogConstants.*;

import com.example.springboot.feature_users.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired private JwtUtils jwtUtils;

    @Autowired private CustomUserDetailsService userDetailsService;

    /**
     * Intercepts HTTP requests to check the Authorization header for a valid JWT token. If the
     * token is valid, the user is authenticated, and the request proceeds. If the token is missing
     * or invalid, an error response is sent with an Unauthorized status.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain to pass the request along if authentication succeeds.
     * @throws ServletException If the filter encounters an error during processing.
     * @throws IOException If there is an error reading or writing the request/response.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("{}{} {}", JWT_REQUEST, request.getMethod(), request.getRequestURI());

        String path = request.getServletPath();
        if (path.equals("/users/login")
                || path.equals("/users/register")
                || path.startsWith("/actuator")) {
            log.info("{}{}", SKIPPING_JWT_FILTER, path);
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        log.info("{}{}", AUTHORIZATION_HEADER, authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info(JWT_NOT_FOUND);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, JWT_NOT_FOUND);
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
                log.info("{}{}", USER_AUTHENTICATED, username);
                log.info("{}{}", USER_ROLE, roles);
            } else {
                log.info(BLOCK_ACCESS);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_JWT_TOKEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
