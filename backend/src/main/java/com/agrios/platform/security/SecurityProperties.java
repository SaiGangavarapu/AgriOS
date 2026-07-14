package com.agrios.platform.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "agrios.security")
public record SecurityProperties(boolean enabled, String jwtSecret, String issuer) {}
