@org.springframework.modulith.ApplicationModule(
    displayName = "Weather Intelligence",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "farm::domain",
        "cropcycle::domain"
    }
)
package com.agrios.platform.weather;
