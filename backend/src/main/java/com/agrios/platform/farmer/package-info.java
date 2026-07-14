@org.springframework.modulith.ApplicationModule(
    displayName = "Farmer Registry",
    allowedDependencies = {
        "shared",
        "common::api",
        "common::web",
        "common::exception",
        "audit",
        "integration"
    }
)
package com.agrios.platform.farmer;
