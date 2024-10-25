package com.example.key;

import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.util.Base64;

public class PublicKeyDto {
    private String publicKey; // Clave pública en formato Base64

    public PublicKeyDto(PublicKey publicKey) {
        this.publicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String getPublicKey() {
        return publicKey;
    }

    // Método para convertir de Base64 a PublicKey si es necesario
    public PublicKey getPublicKeyObject() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}
