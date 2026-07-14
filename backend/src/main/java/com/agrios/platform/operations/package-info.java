@org.springframework.modulith.ApplicationModule(
    displayName = "Farm Operations",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "cropcycle::domain"
    }
)
package com.agrios.platform.operations;
