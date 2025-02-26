package com.example.cleaning_service.security.util;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class KeyProvider {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public KeyProvider(
            @Value("${security.jwt.private-key}") Resource privateKeyResource,
            @Value("${security.jwt.public-key}") Resource publicKeyResource) {
        try {
            this.privateKey = loadPrivateKey(privateKeyResource);
            this.publicKey = loadPublicKey(publicKeyResource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load RSA keys", e);
        }
    }

    private RSAPrivateKey loadPrivateKey(Resource resource) throws Exception {
        byte[] keyBytes = readPemFile(resource);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    private RSAPublicKey loadPublicKey(Resource resource) throws Exception {
        byte[] keyBytes = readPemFile(resource);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    private byte[] readPemFile(Resource resource) throws Exception {
        try (PemReader pemReader = new PemReader(new InputStreamReader(resource.getInputStream()))) {
            PemObject pemObject = pemReader.readPemObject();
            return pemObject.getContent();
        }
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }
}