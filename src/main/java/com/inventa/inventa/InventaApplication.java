package com.inventa.inventa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventaApplication {

    public static void main(String[] args) {
        // Capturar las variables de entorno que debería inyectar Railway
        System.out.println("==== VARIABLES DE ENTORNO DETECTADAS ====");
        System.out.println("SPRING_DATASOURCE_URL=" + System.getenv("SPRING_DATASOURCE_URL"));
        System.out.println("SPRING_DATASOURCE_USERNAME=" + System.getenv("SPRING_DATASOURCE_USERNAME"));
        System.out.println("SPRING_DATASOURCE_PASSWORD=" + System.getenv("SPRING_DATASOURCE_PASSWORD"));
        System.out.println("========================================");

        SpringApplication.run(InventaApplication.class, args);
    }
}
