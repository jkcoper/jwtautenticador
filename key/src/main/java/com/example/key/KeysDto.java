package com.example.key;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeysDto {
    private String privateKey; // Clave privada en formato Base64
    private String publicKey; // Clave pública en formato Base64

    public KeysDto() {}

    public KeysDto(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        this.publicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public PrivateKey getPrivateKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    public PublicKey getPublicKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public String getPrivateKeyString() {
        return privateKey;
    }

    public void setPrivateKeyString(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKeyString() {
        return publicKey;
    }

    public void setPublicKeyString(String publicKey) {
        this.publicKey = publicKey;
    }
}