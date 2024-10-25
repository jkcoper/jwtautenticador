package com.example.key;

import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import java.nio.file.Paths;


import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
/* @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") */

@RestController
@RequestMapping("/config")
@CrossOrigin(origins = "*")
public class ConfigController {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostMapping("/updateKeys")
    public ResponseEntity<Void> updateKeys(@RequestBody KeysDto keysDto) {
        try {
            // Almacenar las claves privadas y públicas en variables de instancia
            this.privateKey = keysDto.getPrivateKey(); // Obtener PrivateKey
            this.publicKey = keysDto.getPublicKey(); // Obtener PublicKey

            // Imprimir las claves en consola (para depuración)
            System.out.println("Clave privada recibida: " + Base64.getEncoder().encodeToString(this.privateKey.getEncoded()));
            System.out.println("Clave pública recibida: " + Base64.getEncoder().encodeToString(this.publicKey.getEncoded()));

            // Guardar las claves en archivos locales
            Files.write(Paths.get("private_key.pem"), Base64.getEncoder().encode(this.privateKey.getEncoded()));
            Files.write(Paths.get("public_key.pem"), Base64.getEncoder().encode(this.publicKey.getEncoded()));

            System.out.println("Clave privada guardada en private_key.pem");
            System.out.println("Clave pública guardada en public_key.pem");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/publicKey")
public ResponseEntity<Map<String, String>> getPublicKey() {
    // Verificar si la clave pública no se ha establecido
    if (publicKey == null) {
        System.out.println("Devolviendo error interno al llamar a la api publicKey");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    // Preparar la respuesta con la clave pública en formato Base64
    Map<String, String> response = new HashMap<>();
    System.out.println("Clave pública entregada al microservicio");
    response.put("publicKey", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
    
    return ResponseEntity.ok(response);
}

}
