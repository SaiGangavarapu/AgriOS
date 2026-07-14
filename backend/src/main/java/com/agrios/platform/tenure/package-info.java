@org.springframework.modulith.ApplicationModule(
    displayName = "Tenure",
    allowedDependencies = {
        "shared",
        "common::web",
        "common::exception",
        "farmer::domain",
        "farm::domain"
    }
)
package com.agrios.platform.tenure;
