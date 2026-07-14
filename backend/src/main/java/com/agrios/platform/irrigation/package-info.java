@org.springframework.modulith.ApplicationModule(
    displayName = "Irrigation",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "cropcycle::domain",
        "farm::domain",
        "soilwater::domain",
        "iotdevice::domain",
        "telemetry::domain"
    }
)
package com.agrios.platform.irrigation;
