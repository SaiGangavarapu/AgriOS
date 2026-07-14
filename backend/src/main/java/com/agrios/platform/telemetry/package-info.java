@org.springframework.modulith.ApplicationModule(
    displayName = "Telemetry",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "iotdevice::domain"
    }
)
package com.agrios.platform.telemetry;
