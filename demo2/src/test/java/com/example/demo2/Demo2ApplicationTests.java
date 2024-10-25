package com.example.demo2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ProductoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerProductosSinToken_deberiaRetornarUnauthorized() throws Exception {
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }


}
