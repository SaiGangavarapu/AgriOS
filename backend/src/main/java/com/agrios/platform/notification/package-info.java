@org.springframework.modulith.ApplicationModule(
    displayName = "Farmer Notification",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "farmer::domain",
        "advisory::domain"
    }
)
package com.agrios.platform.notification;
