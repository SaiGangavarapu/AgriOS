package com.agrios.platform;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AgriOsApplicationTests {
    @Test
    void applicationEntryPointExists() {
        assertThat(AgriOsApplication.class).isNotNull();
    }
}
