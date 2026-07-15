@org.springframework.modulith.ApplicationModule(
    displayName = "AI Decision Intelligence, RAG, Tool Calling, and Farmer Assistant",
    allowedDependencies = {
        "common::web",
        "common::exception",
        "farmer::domain",
        "farm::domain",
        "cropcycle::domain"
    }
)
package com.agrios.platform.ai;
