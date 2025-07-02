package com.instagram.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class InstagramAuthApplicationTests {

    @Test
    void contextLoads() {
        // Test básico para verificar que el contexto de Spring se carga correctamente
    }
}