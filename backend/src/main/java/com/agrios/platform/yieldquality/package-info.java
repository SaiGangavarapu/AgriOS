@org.springframework.modulith.ApplicationModule(
    displayName = "Yield and Quality",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "harvest::domain",
        "cropcycle::domain",
        "knowledge::domain"
    }
)
package com.agrios.platform.yieldquality;
