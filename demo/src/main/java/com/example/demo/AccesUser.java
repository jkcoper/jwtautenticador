package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
/* import org.springframework.core.io.ClassPathResource; */
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
/* import org.springframework.core.io.Resource; */
import io.jsonwebtoken.Claims;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;


import java.util.Base64;
import java.security.KeyFactory;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost", allowCredentials = "true")
public class AccesUser {

    private JwtUtil jwtUtil;
    public AccesUser() {
        this.jwtUtil = new JwtUtil(); // Inicia el utilitario de JWT para manejar tokens de autenticación.
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
    System.out.println("Credenciales de acceso del front: ");
    System.out.println("Usuario: " + authRequest.getUsername());
    System.out.println("Contraseña: " + authRequest.getPassword());

    try {
         // Cargar usuarios desde el archivo JSON para verificar credenciales dinámicamente.
        System.out.println("Iniciando llamada a loadUsersFromJSON()");
        List<User> users = loadUsersFromJSON();
        System.out.println("Usuarios cargados correctamente");

        // Verificar las credenciales
        for (User user : users) {
            if (user.getUsername().equals(authRequest.getUsername()) && user.getPassword().equals(authRequest.getPassword())) {
                // Generar nuevas claves RSA
                KeyPair keyPair = generateKeyPair();

                // Firmar el JWT con la clave privada RSA
                String token = jwtUtil.generateToken(user.getIduser(), user.getUsername(), keyPair.getPrivate());

                // *** Imprimir el token JWT generado en consola ***
                System.out.println("Token JWT generado: " + token);

                // Guarda las claves RSA en el servidor de configuración, para su uso en validación y emisión de tokens.
                updateKeysInConfig(keyPair.getPrivate(), keyPair.getPublic());

                // Configura una cookie HTTP con el token JWT para que el cliente almacene su sesión.
                ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                        .httpOnly(true) // true Esta cookie no será accesible desde JavaScript
                        .secure(false) // true Solo se enviará a través de HTTPS
                        .path("/")
                        .maxAge(60 * 60 * 24)
                        .build();

                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body("Autenticado con éxito");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");

    } catch (Exception e) {
        e.printStackTrace();  // <-- Esto mostrará el error si ocurre alguno
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
    }
    }

    private KeyPair generateKeyPair() {
        // Genera un par de claves RSA de 2048 bits para asegurar la sesión.
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Error generating key pair", e);
        }
    }

    private void updateKeysInConfig(PrivateKey privateKey, PublicKey publicKey) {
        // Envía el par de claves al servidor de configuración para asegurar su disponibilidad para la validación.
        RestTemplate restTemplate = new RestTemplate();
        KeysDto keysDto = new KeysDto(privateKey, publicKey);
        
        // Cambiar la URL según corresponda para el endpoint de tu servidor de configuración
        ResponseEntity<Void> response = restTemplate.postForEntity("http://config-server:8080/config/updateKeys", keysDto, Void.class);
        
        // Manejo de errores o verificaciones de respuesta si es necesario
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Error al enviar las claves al servidor de configuración");
        }
    }

    @GetMapping("/home")
    public ResponseEntity<Map<String, String>> getUsernameFromToken(@CookieValue("jwt") String token) {
         // Extrae y valida el nombre de usuario desde el token JWT usando la clave pública.
        Claims claims = jwtUtil.extractClaims(token, retrievePublicKey()); // Usar la clave pública recuperada
        String username = claims.getSubject();

         // Personaliza el mensaje de bienvenida con el nombre de usuario extraído.
        Map<String, String> response = new HashMap<>();
        response.put("message", "Bienvenido");
        response.put("username", username);

        return ResponseEntity.ok(response);
    }

    private PublicKey retrievePublicKey() {
         // Recupera la clave pública desde el servidor de configuración para la validación de tokens.
        try {
            RestTemplate restTemplate = new RestTemplate();
           ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
    "http://config-server:8080/config/publicKey", 
    HttpMethod.GET, 
    null, 
    new ParameterizedTypeReference<Map<String, String>>() {}
);

Map<String, String> response = responseEntity.getBody();

            
            // Imprimir la respuesta JSON
            System.out.println("Respuesta de la clave pública en JSON: " + response);
    
            String publicKeyBase64 = response.get("publicKey");
    
             // Decodifica la clave pública en formato Base64 para usarla en la validación del JWT.
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving public key", e);
        }
    }
    


    private List<User> loadUsersFromJSON() {
        // Carga la lista de usuarios desde un archivo JSON almacenado en el contenedor para realizar la autenticación.
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Activamos el proceso de carga del usuario del json");
        try {
            File file = new File("/app/user.json");
            System.out.println("Extrayendo el archivo json de la ruta respectiva");
            System.out.println("Extraemos el archivo json de la ruta respectiva: " + file.getAbsolutePath());
            UserList userList = objectMapper.readValue(file, UserList.class);
            /* Resource resource = new ClassPathResource("user.json");
            UserList userList = objectMapper.readValue(resource.getFile(), UserList.class); */
            

            System.out.println("Contenido del archivo JSON:");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userList));
            return userList.getUsers();
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }


}
