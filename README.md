# InventaGT

InventaGT es un sistema completo de gestión de inventario y ventas diseñado para optimizar las operaciones comerciales. Esta aplicación ayuda a administrar productos, proveedores, clientes y ventas, proporcionando una solución completa para el control de inventario.

## Empezando

Estas instrucciones te permitirán obtener una copia del proyecto en funcionamiento en tu máquina local para fines de desarrollo y prueba.

### Prerrequisitos

Lo que necesitas para instalar el software:

*   [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) - Versión 17 o posterior
*   [Gradle](https://gradle.org/install/) - Versión 8.x (Opcional, el wrapper de Gradle está incluido)

### Instalación

1.  **Clona el repositorio**
    ```sh
    git clone https://github.com/RLiteM/InventaGT.git
    cd InventaGT
    ```

2.  **Configura la base de datos**
    *   La aplicación utiliza una base de datos PostgreSQL. Asegúrate de tener PostgreSQL instalado y en funcionamiento.
    *   Crea una base de datos llamada `inventagt`.
    *   Configura la conexión de la base de datos en `src/main/resources/application.properties`:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/inventagt
        spring.datasource.username=tu_usuario
        spring.datasource.password=tu_contraseña
        ```

## Ejecutando la aplicación

Puedes ejecutar la aplicación usando el wrapper de Gradle:

*   **En macOS/Linux:**
    ```sh
    ./gradlew bootRun
    ```

*   **En Windows:**
    ```sh
    .\gradlew.bat bootRun
    ```

La aplicación se iniciará en `http://localhost:8080`.

## Construido con

*   [Spring Boot](https://spring.io/projects/spring-boot) - El framework web utilizado
*   [Gradle](https://gradle.org/) - Gestión de dependencias
*   [PostgreSQL](https://www.postgresql.org/) - Base de datos