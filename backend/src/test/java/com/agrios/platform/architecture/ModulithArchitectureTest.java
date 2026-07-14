package com.agrios.platform.architecture;

import com.agrios.platform.AgriOsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModulithArchitectureTest {
    @Test
    void verifiesModuleStructure() {
        ApplicationModules.of(AgriOsApplication.class).verify();
    }
}
