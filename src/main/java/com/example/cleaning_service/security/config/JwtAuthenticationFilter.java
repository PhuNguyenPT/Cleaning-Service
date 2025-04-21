package com.example.cleaning_service.security.config;

import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.IJwtService;
import com.example.cleaning_service.security.services.impl.CustomUserDetailsService;
import com.example.cleaning_service.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final IJwtService jwtService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService, IJwtService jwtService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            // 🔹 Check if token exists in Redis (ensure it's valid and not revoked)
            if (!jwtService.existsByToken(token)) {
                logger.warn("Token not found in Redis (possible logout or expiration)");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }

            // 🔴 Check if Token is Blacklisted (Logged out)
            if (jwtService.isTokenBlacklisted(token)) {
                logger.warn("Attempt to use blacklisted token: {}", token);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been blacklisted");
                return;
            }

            username = jwtUtil.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userDetailsService.loadUserByUsername(username);

            if (!jwtUtil.validateToken(token, user)) {
                logger.warn("Invalid JWT token: {}", token);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
            Set<GrantedAuthority> roles = jwtUtil.extractRoles(token).stream()
                    .map(s -> new SimpleGrantedAuthority("ROLE_" + s))
                    .collect(Collectors.toSet());
            Set<GrantedAuthority> authorities = jwtUtil.extractPermissions(token).stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            authorities.addAll(roles);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, authorities);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
