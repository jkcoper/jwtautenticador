package com.example.demo;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class KeysDto {
    private String privateKey; // Clave privada en formato base64
    private String publicKey; // Clave pública en formato base64

    public KeysDto() {}

    public KeysDto(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        this.publicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
