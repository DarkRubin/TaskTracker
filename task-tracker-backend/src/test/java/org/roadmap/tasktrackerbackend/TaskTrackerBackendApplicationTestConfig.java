package org.roadmap.tasktrackerbackend;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TaskTrackerBackendApplicationTestConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> testPostgresqlContainer() {
        PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
                .withExposedPorts(5432);
        postgres.start();
        return postgres;
    }

}
