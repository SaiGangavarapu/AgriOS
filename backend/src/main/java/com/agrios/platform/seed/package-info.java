@org.springframework.modulith.ApplicationModule(
    displayName = "Seed",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "cropcycle::domain",
        "knowledge::domain"
    }
)
package com.agrios.platform.seed;
