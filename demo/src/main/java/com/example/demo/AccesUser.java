package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.Resource;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")

public class AccesUser {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static SecretKey getSecretKey() {
        return SECRET_KEY;
    }
    private JwtUtil jwtUtil;

    public static class UserList {
        private List<User> users;

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

    public AccesUser() {
        this.jwtUtil = new JwtUtil(); // Crear una nueva instancia de JwtUtil
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        System.out.println("Credenciales de acceso del front: ");
        System.out.println("Usuario: " + authRequest.getUsername());
        System.out.println("Contraseña: " + authRequest.getPassword());

        // Cargar usuarios desde el archivo JSON
        List<User> users = loadUsersFromJSON();
        System.out.println("Usuarios que se encuentran en el JSON:");
        for (User user : users) {
            System.out.println("Usuario: " + user.getUsername() + "  " + "Password: " + user.getPassword());
        }
        
        // Verificar las credenciales
        for (User user : users) {
            if (user.getUsername().equals(authRequest.getUsername()) && 
                user.getPassword().equals(authRequest.getPassword())) { // Comparar contraseñas directamente

                String token = jwtUtil.generateToken(user.getIduser(),user.getUsername(), SECRET_KEY);

                ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true) // Set to true if using HTTPS
                .path("/")
                .maxAge(60 * 60 * 24) // 1 day
                .build();

                System.out.println("Credenciales válidas. Generando token...");
                return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(Map.of("message", "Login successful"));
            }
        }
        
        // Si las credenciales no son válidas
        System.out.println("Credenciales inválidas.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }


    // Método para cargar usuarios desde el archivo JSON
    private List<User> loadUsersFromJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Resource resource = new ClassPathResource("user.json");
            UserList userList = objectMapper.readValue(resource.getFile(), UserList.class);
            return userList.getUsers();
        } catch (IOException e) {
            e.printStackTrace();
            return List.of(); // Retornar una lista vacía si ocurre un error
        }
    }

    
    @GetMapping("/home")
    public ResponseEntity<Map<String, String>> getUsernameFromToken(@CookieValue("jwt") String token) {
        Claims claims = jwtUtil.extractClaims(token, SECRET_KEY);
        String username = claims.getSubject();

        // Crear una respuesta en formato JSON
        Map<String, String> response = new HashMap<>();
        response.put("message","Bienvenido");
        response.put("username", username);

        return ResponseEntity.ok(response);
    }
}
