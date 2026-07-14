package com.agrios.platform.configuration;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PlatformInfoController {
    @GetMapping("/api/v1/platform/info")
    Map<String, Object> info() {
        return Map.of(
                "name", "AgriOS",
                "version", "0.1.0-SNAPSHOT",
                "architecture", "Spring Modulith modular monolith",
                "timestamp", Instant.now().toString());
    }
}
