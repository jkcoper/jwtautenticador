package com.example.demo;

import io.jsonwebtoken.Claims;
/* import io.jsonwebtoken.JwtBuilder; */
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
/* import io.jsonwebtoken.security.Keys; */
import java.security.Key;

/* import java.util.HashMap;
import java.util.Map; */
import java.util.Date;


/* import javax.crypto.SecretKey; */

public class JwtUtil {
    /* private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); */

    // Método para generar el token
    public String generateToken(String idUser, String username, Key privateKey) {
        return Jwts.builder()
                .setSubject(username)
                .claim("id_user", idUser)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1 día
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Claims extractClaims(String token, Key publicKey) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
