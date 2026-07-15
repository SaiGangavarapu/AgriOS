@org.springframework.modulith.ApplicationModule(
    displayName = "Farmer Finance and Profitability",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "farmer::domain",
        "farm::domain",
        "cropcycle::domain"
    }
)
package com.agrios.platform.finance;
