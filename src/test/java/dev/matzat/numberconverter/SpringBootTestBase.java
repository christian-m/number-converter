package dev.matzat.numberconverter;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SpringBootTestBase {

    @ServiceConnection
    private final static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:14");

    static {
        postgresqlContainer.start();
    }

}
