@org.springframework.modulith.ApplicationModule(
    displayName = "Farm and Field",
    allowedDependencies = {
        "shared",
        "common::api",
        "common::web",
        "common::exception",
        "farmer::domain",
        "integration"
    }
)
package com.agrios.platform.farm;
