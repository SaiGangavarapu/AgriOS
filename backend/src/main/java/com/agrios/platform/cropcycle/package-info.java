@org.springframework.modulith.ApplicationModule(
    displayName = "Crop Cycle",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "cropplanning::domain",
        "farm::domain",
        "knowledge::domain",
        "tenure::domain"
    }
)
package com.agrios.platform.cropcycle;
