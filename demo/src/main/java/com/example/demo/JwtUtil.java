package com.example.demo;

import io.jsonwebtoken.Claims;
/* import io.jsonwebtoken.JwtBuilder; */
import io.jsonwebtoken.Jwts;
/* import io.jsonwebtoken.SignatureAlgorithm; */
/* import io.jsonwebtoken.security.Keys; */

import java.util.HashMap;
import java.util.Map;
import java.util.Date;


import javax.crypto.SecretKey;

public class JwtUtil {
    /* private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); */

    // Método para generar el token
    public String generateToken(String idUser, String username, SecretKey secretKey) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id_user", idUser); // Agregar el id_user al payload
        claims.put("username", username); // Agregar el username al payload
        return Jwts.builder()
        .setClaims(claims) // Establecer el payload
        .setSubject(username)
        .setIssuedAt(new Date()) // Establecer la fecha de emisión
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Tiempo de expiración (1 hora)
        .signWith(secretKey) // Usa la clave secreta dinámica
        .compact();
    }

    // Método para extraer las claims del token
    public Claims extractClaims(String token, SecretKey secretKey) {
        return Jwts.parserBuilder()  // Use parserBuilder instead of parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
