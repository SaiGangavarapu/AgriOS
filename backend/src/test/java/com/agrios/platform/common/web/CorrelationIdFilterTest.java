package com.agrios.platform.common.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class CorrelationIdFilterTest {
    @Test
    void preservesValidCorrelationId() throws Exception {
        UUID expected = UUID.randomUUID();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(CorrelationIdFilter.HEADER, expected.toString());
        MockHttpServletResponse response = new MockHttpServletResponse();

        new CorrelationIdFilter().doFilter(request, response, (req, res) -> {});

        assertThat(response.getHeader(CorrelationIdFilter.HEADER)).isEqualTo(expected.toString());
    }

    @Test
    void replacesInvalidCorrelationId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(CorrelationIdFilter.HEADER, "invalid");
        MockHttpServletResponse response = new MockHttpServletResponse();

        new CorrelationIdFilter().doFilter(request, response, (req, res) -> {});

        assertThatCodeIsUuid(response.getHeader(CorrelationIdFilter.HEADER));
    }

    private void assertThatCodeIsUuid(String value) {
        assertThat(UUID.fromString(value)).isNotNull();
    }
}
