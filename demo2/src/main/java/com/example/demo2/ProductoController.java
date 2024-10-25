package com.example.demo2;

import org.springframework.core.ParameterizedTypeReference;
/* import org.springframework.core.io.ClassPathResource; */
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.Base64;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import java.security.spec.X509EncodedKeySpec;
import java.io.File;
import java.security.KeyFactory;

@CrossOrigin(origins = "http://localhost", allowCredentials = "true")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private JwtUtil jwtUtil; // Utilitario JWT para la extracción de claims del token.

    @GetMapping
    public ResponseEntity<?> obtenerProductos(HttpServletRequest request) {
        // Recupera la cookie JWT del usuario autenticado.
        System.out.println("Extraer la cookie con el JWT");
        Cookie[] cookies = request.getCookies();
        String jwtToken = null;
        // Buscar la cookie llamada "jwt" entre todas las cookies recibidas.
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken == null) {
            System.out.println("Token JWT no proporcionado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT no proporcionado");
        }

        try {
            // Recupera la clave pública del servidor de configuración y valida el token JWT.
            System.out.println("Recuperando clave pública");
            PublicKey publicKey = retrievePublicKey(); 
            Claims claims = jwtUtil.extractClaims(jwtToken, publicKey);
            String idUsuario = claims.get("id_user", String.class);

            // Filtra productos asociados al usuario a partir de su ID extraído del JWT.
            System.out.println("Filtrando productos del usuario");
            List<Producto> productos = obtenerProductosAsociadosAlUsuario(idUsuario);

            if (productos.isEmpty()) {
                System.out.println("No se encontraron productos para este usuario.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron productos para este usuario.");
            }

            return ResponseEntity.ok(Map.of("products", productos));
        } catch (Exception e) {
            System.out.println("Token JWT no válido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT no válido.");
        }
    }

    private List<Producto> obtenerProductosAsociadosAlUsuario(String idUsuario) {
        // Carga los productos desde un archivo JSON y filtra los que coinciden con el usuario.
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Deserializa el archivo JSON en una lista de productos.

            File file = new File("/app/productos.json");
            System.out.println("Extrayendo el archivo json de la ruta respectiva");
            System.out.println("Extraemos el archivo json de la ruta respectiva: " + file.getAbsolutePath());
            ProductList productList = objectMapper.readValue(file, ProductList.class);


            /* ClassPathResource resource = new ClassPathResource("productos.json");
            ProductList productList = objectMapper.readValue(resource.getFile(), ProductList.class); */

            System.out.println("ID Usuario: " + idUsuario);
            System.out.println("Productos encontrados: " + productList.getProducts());
            
            // Filtra la lista de productos según el id_user que corresponda con el usuario autenticado.
            return productList.getProducts().stream()
                    .filter(product -> product.getId_user().equals(idUsuario))
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // En caso de error, devolver una lista vacía
        }
    }

    private PublicKey retrievePublicKey() {
         // Recupera la clave pública desde el servidor de configuración para validar el token JWT.
        try {
            RestTemplate restTemplate = new RestTemplate();
            // Crear un HttpEntity vacío
            HttpEntity<Void> requestEntity = new HttpEntity<>(null);

            ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
                "http://config-server:8080/config/publicKey", 
                HttpMethod.GET, 
                requestEntity, 
                new ParameterizedTypeReference<Map<String, String>>() {}
            );

            Map<String, String> response = responseEntity.getBody();
            String publicKeyBase64 = response.get("publicKey");
            // Convierte la clave pública de Base64 a PublicKey para usarla en la validación.
            System.out.println("Clave pública recuperada: " + publicKeyBase64);
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving public key", e);
        }
    }
}
