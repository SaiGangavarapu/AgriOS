package com.agrios.platform.farmer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "agrios.security.enabled=false")
class FarmerFarmApiIntegrationTest {
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

    @Autowired
    TestRestTemplate rest;

    @Test
    void registersFarmerAndFarm() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Tenant-Id", TENANT.toString());

        Map<String, Object> farmerBody = Map.of(
                "fullName", "Test Farmer",
                "preferredLanguage", "te",
                "mobileE164", "+919999999999",
                "villageName", "Test Village",
                "sourceChannel", "SELF");

        ResponseEntity<Map> farmer = rest.exchange(
                "/api/v1/farmers", HttpMethod.POST,
                new HttpEntity<>(farmerBody, headers), Map.class);

        assertThat(farmer.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String farmerId = farmer.getBody().get("id").toString();

        Map<String, Object> farmBody = Map.of(
                "name", "Test Farm",
                "primaryOperatorFarmerId", farmerId,
                "farmType", "INDIVIDUAL_FAMILY_FARM",
                "villageName", "Test Village");

        ResponseEntity<Map> farm = rest.exchange(
                "/api/v1/farms", HttpMethod.POST,
                new HttpEntity<>(farmBody, headers), Map.class);

        assertThat(farm.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(farm.getBody().get("primaryOperatorFarmerId").toString())
                .isEqualTo(farmerId);
    }
}
