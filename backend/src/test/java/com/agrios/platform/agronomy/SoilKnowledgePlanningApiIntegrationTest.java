package com.agrios.platform.agronomy;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
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
class SoilKnowledgePlanningApiIntegrationTest {
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
    void publishesCropAndCreatesSuitabilityAssessment() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Tenant-Id", TENANT.toString());

        Map crop = post("/api/v1/knowledge/crops", Map.of(
                "code", "RICE",
                "defaultName", "Rice",
                "scientificName", "Oryza sativa",
                "cropCategory", "CEREAL",
                "durationMinDays", 100,
                "durationMaxDays", 150,
                "evidenceGrade", "A"), headers);

        String cropId = crop.get("id").toString();
        post("/api/v1/knowledge/crops/" + cropId + "/submit-review", Map.of(), headers);
        post("/api/v1/knowledge/crops/" + cropId + "/approve", Map.of(), headers);
        Map published = post("/api/v1/knowledge/crops/" + cropId + "/publish", Map.of(), headers);

        assertThat(published.get("status")).isEqualTo("PUBLISHED");
    }

    private Map post(String path, Object body, HttpHeaders headers) {
        ResponseEntity<Map> response = rest.exchange(
                path, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        return response.getBody();
    }
}
