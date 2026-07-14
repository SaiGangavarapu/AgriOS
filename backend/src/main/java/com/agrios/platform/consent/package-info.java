@org.springframework.modulith.ApplicationModule(
    displayName = "Consent",
    allowedDependencies = {
        "shared",
        "common::web",
        "common::exception",
        "farmer::domain"
    }
)
package com.agrios.platform.consent;
