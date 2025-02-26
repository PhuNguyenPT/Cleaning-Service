package com.example.cleaning_service.security.util;

import com.example.cleaning_service.security.users.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
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
     * @throws JOSEException If signing fails.
     */
    public String generateToken(User subject, long expirationMillis) throws JOSEException {
        JWSSigner signer = new RSASSASigner(keyProvider.getPrivateKey());

        // Extract role and permissions
        String role = subject.getRole().getName().name(); // Convert Enum to String
        List<String> permissions = subject.getPermissions().stream()
                .map(permission -> permission.getName().name()) // Convert Enum to String
                .collect(Collectors.toList());

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject.getUsername())  // Store username
                .claim("role", role)          // Store user role
                .claim("permissions", permissions)  // Store user permissions
                .issueTime(now)  // Issue time
                .expirationTime(expiryDate) // Expiration time
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token.
     * @return The extracted username.
     * @throws ParseException If parsing fails.
     */
    public String extractUsername(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getSubject();
    }

    /**
     * Validates the JWT token against UserDetails using RS256.
     *
     * @param token The JWT token.
     * @param userDetails The user details to validate against.
     * @return true if the token is valid, false otherwise.
     * @throws JOSEException If verification fails.
     * @throws ParseException If parsing fails.
     */
    public boolean validateToken(String token, UserDetails userDetails) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new RSASSAVerifier(keyProvider.getPublicKey());

        // Validate signature
        if (!signedJWT.verify(verifier)) {
            return false;
        }

        // Extract claims
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        String usernameFromToken = claims.getSubject();
        String roleFromToken = extractRole(token);
        Set<String> permissionsFromToken = extractPermissions(token);

        // Validate expiration time
        if (new Date().after(claims.getExpirationTime())) {
            return false;
        }

        // Validate username
        if (!userDetails.getUsername().equals(usernameFromToken)) {
            return false;
        }

        // Validate role and permissions
        if (userDetails instanceof User user) {
            Set<String> userPermissions = user.getPermissions().stream()
                    .map(permission -> permission.getName().name())
                    .collect(Collectors.toSet());

            return roleFromToken.equals(user.getRole().getName().name())
                    && userPermissions.containsAll(permissionsFromToken);
        }

        return false;
    }

    /**
     * Extracts the role from a JWT token.
     */
    public String extractRole(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getStringClaim("role");
    }

    /**
     * Extracts permissions from a JWT token.
     */
    public Set<String> extractPermissions(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return new HashSet<>(signedJWT.getJWTClaimsSet().getStringListClaim("permissions"));
    }

    /**
     * Extracts expiration time from a JWT token.
     */
    public Long extractExpirationTime(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getExpirationTime().getTime();
    }

}