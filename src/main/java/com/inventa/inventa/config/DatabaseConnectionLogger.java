package com.inventa.inventa.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseConnectionLogger implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionLogger.class);

    private final DataSource dataSource;

    public DatabaseConnectionLogger(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("==========================================================");
        logger.info("VERIFICANDO CONEXIÓN A LA BASE DE DATOS...");
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                logger.info("CONEXIÓN A LA BASE DE DATOS ESTABLECIDA CORRECTAMENTE.");
            } else {
                logger.error("LA CONEXIÓN A LA BASE DE DATOS NO ES VÁLIDA.");
            }
        } catch (Exception e) {
            logger.error("ERROR AL CONECTARSE A LA BASE DE DATOS: ", e);
        }
        logger.info("==========================================================");
    }
}
