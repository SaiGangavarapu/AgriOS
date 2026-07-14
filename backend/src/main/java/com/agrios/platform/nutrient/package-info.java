@org.springframework.modulith.ApplicationModule(
    displayName = "Nutrient Management",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "cropcycle::domain",
        "soilwater::domain",
        "knowledge::domain"
    }
)
package com.agrios.platform.nutrient;
