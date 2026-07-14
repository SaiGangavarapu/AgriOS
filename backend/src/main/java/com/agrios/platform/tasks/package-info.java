@org.springframework.modulith.ApplicationModule(
    displayName = "Tasks and Calendar",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "cropcycle::domain",
        "operations::domain"
    }
)
package com.agrios.platform.tasks;
