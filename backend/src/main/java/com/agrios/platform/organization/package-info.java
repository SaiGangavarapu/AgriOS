@org.springframework.modulith.ApplicationModule(
    displayName = "Farmer Organization, Shared Resources, and Collective Marketing",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "farmer::domain",
        "knowledge::domain",
        "traceability::domain",
        "market::domain",
        "finance::domain"
    }
)
package com.agrios.platform.organization;
