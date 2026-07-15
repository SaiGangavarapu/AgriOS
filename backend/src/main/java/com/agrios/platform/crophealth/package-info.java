@org.springframework.modulith.ApplicationModule(
    displayName = "Pest, Disease, Crop Health, and IPM",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "farm::domain",
        "cropcycle::domain",
        "knowledge::domain",
        "advisory::domain",
        "tasks::domain"
    }
)
package com.agrios.platform.crophealth;
