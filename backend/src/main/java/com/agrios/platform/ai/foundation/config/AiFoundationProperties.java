package com.agrios.platform.ai.foundation.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("agrios.ai")
public record AiFoundationProperties(int maxInputCharacters, int maxOutputCharacters,
                                     int historyLimit, Duration timeout, int transientRetries) {
    public AiFoundationProperties {
        if (maxInputCharacters <= 0) maxInputCharacters = 12000;
        if (maxOutputCharacters <= 0) maxOutputCharacters = 16000;
        if (historyLimit <= 0) historyLimit = 20;
        if (timeout == null) timeout = Duration.ofSeconds(60);
        if (transientRetries < 0) transientRetries = 1;
    }
}
