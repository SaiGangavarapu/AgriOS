package com.agrios.platform.cropcycle;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "agrios.security.enabled=false")
class CropCycleSeedOperationTaskIntegrationTest {
    private static final UUID TENANT =
            UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgis/postgis:16-3.4")
                    .asCompatibleSubstituteFor("postgres"))
                    .withDatabaseName("agrios")
                    .withUsername("agrios")
                    .withPassword("agrios");

    @Autowired TestRestTemplate rest;

    @Test
    void migrationAndApplicationContextSupportNewModules() {
        ResponseEntity<String> response = rest.getForEntity(
                "/actuator/health", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("UP");
    }
}
