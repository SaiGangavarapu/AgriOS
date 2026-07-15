@org.springframework.modulith.ApplicationModule(
    displayName = "Traceability and Storage",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "harvest::domain",
        "yieldquality::domain",
        "cropcycle::domain"
    }
)
package com.agrios.platform.traceability;
