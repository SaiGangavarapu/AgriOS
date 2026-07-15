@org.springframework.modulith.ApplicationModule(
    displayName = "Market Intelligence, Sales Planning, and Marketplace Integration",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "farmer::domain",
        "cropcycle::domain",
        "knowledge::domain",
        "traceability::domain",
        "finance::domain"
    }
)
package com.agrios.platform.market;
