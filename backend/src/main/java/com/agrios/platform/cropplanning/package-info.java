@org.springframework.modulith.ApplicationModule(
    displayName = "Crop Planning",
    allowedDependencies = {
        "shared",
        "common::web",
        "common::exception",
        "farm::domain",
        "soilwater::domain",
        "knowledge::domain",
        "tenure"
    }
)
package com.agrios.platform.cropplanning;
