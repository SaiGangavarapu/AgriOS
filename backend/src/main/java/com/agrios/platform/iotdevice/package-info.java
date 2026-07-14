@org.springframework.modulith.ApplicationModule(
    displayName = "IoT Device",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "farm::domain",
        "irrigation::domain",
        "weather::domain"
    }
)
package com.agrios.platform.iotdevice;
