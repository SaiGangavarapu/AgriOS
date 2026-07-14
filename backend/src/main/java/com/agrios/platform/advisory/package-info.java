@org.springframework.modulith.ApplicationModule(
    displayName = "Advisory and Expert Review",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "farmer::domain",
        "farm::domain",
        "cropcycle::domain",
        "tasks::domain"
    }
)
package com.agrios.platform.advisory;
