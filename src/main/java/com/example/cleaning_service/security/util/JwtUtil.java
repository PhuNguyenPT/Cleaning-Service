package com.example.cleaning_service.security.util;

import com.example.cleaning_service.security.entities.user.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    private static final Long EXPIRATION_TIME_IN_MILLIS = 86400000L;
    private static final Long EXPIRATION_TIME_IN_SECONDS = EXPIRATION_TIME_IN_MILLIS / 1000;
    private final KeyProvider keyProvider;
    public JwtUtil(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    /**
     * Generates a JWT token using RS256.
     *
     * @param subject The user identifier (e.g., username).
     * @param expirationMillis The expiration time in milliseconds.
     * @return The generated JWT token.
     */
    public String generateToken(User subject, @Nullable Long expirationMillis) {
        JWSSigner signer = new RSASSASigner(keyProvider.getPrivateKey());

        // Extract role and permissions
        List<String> roles= subject.getRoles().stream()
                .map(role -> role.getName().name())
                .toList(); // Convert Enum to String
        List<String> permissions = subject.getPermissions().stream()
                .map(permission -> permission.getName().name()) // Convert Enum to String
                .toList();

        Date now = new Date(System.currentTimeMillis());
        expirationMillis = (expirationMillis != null)
                ? expirationMillis : getDefaultExpirationTimeInMillis();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject.getUsername())  // Store username
                .claim("roles", roles)          // Store user roles
                .claim("permissions", permissions)  // Store user permissions
                .issueTime(now)  // Issue time
                .expirationTime(expiryDate) // Expiration time
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        return signedJWT.serialize();
    }

    public Long getDefaultExpirationTimeInSeconds() {
        return EXPIRATION_TIME_IN_SECONDS;
    }

    private Long getDefaultExpirationTimeInMillis() {
        return EXPIRATION_TIME_IN_MILLIS;
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token.
     * @return The extracted username.
     */
    public String extractUsername(String token)  {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates the JWT token against UserDetails using RS256.
     *
     * @param token The JWT token.
     * @param user The user details to validate against.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token, User user) {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JWSVerifier verifier = new RSASSAVerifier(keyProvider.getPublicKey());

        // Validate signature
        try {
            if (!signedJWT.verify(verifier)) {
                log.warn("Cannot verify token {}", token);
                return false;
            }
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        // Extract claims
        JWTClaimsSet claims;
        try {
            claims = signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String usernameFromToken = claims.getSubject();
        Set<String> tokenRoles = extractRoles(token);
        Set<String> tokenPermissions = extractPermissions(token);

        // Validate expiration time
        if (new Date().after(claims.getExpirationTime())) {
            log.warn("Token expired: {}", token);
            return false;
        }

        // Validate username
        if (!user.getUsername().equals(usernameFromToken)) {
            log.warn("Token's username unknown: {}", token);
            return false;
        }

        Set<String> userRoles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        // Validate role
        if (!userRoles.containsAll(tokenRoles)) {
            log.warn("Token's role: {} not included in user's role {}", tokenRoles, userRoles);
            return false;
        }

        Set<String> userPermissions = user.getPermissions().stream()
                .map(permission -> permission.getName().name())
                .collect(Collectors.toSet());

        // Validate permissions
        if (!userPermissions.containsAll(tokenPermissions)) {
            log.warn("Token's permissions: {} not equal to user' permissions {}",
                    tokenPermissions, userPermissions);
            return false;
        }

        return true;
    }

    /**
     * Extracts the role from a JWT token.
     */
    public Set<String> extractRoles(String token) {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            return new HashSet<>(signedJWT.getJWTClaimsSet().getStringListClaim("roles"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extracts permissions from a JWT token.
     */
    public Set<String> extractPermissions(String token) {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            return new HashSet<>(signedJWT.getJWTClaimsSet().getStringListClaim("permissions"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extracts expiration time from a JWT token.
     */
    public Long extractExpirationTime(String token)  {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            return signedJWT.getJWTClaimsSet().getExpirationTime().getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}