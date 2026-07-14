package com.agrios.platform.common.web;

import com.agrios.platform.common.exception.BusinessException;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TenantContextResolver {
    public static final String HEADER = "X-Tenant-Id";

    public TenantContext resolve(HttpServletRequest request) {
        String raw = request.getHeader(HEADER);
        if (!StringUtils.hasText(raw)) {
            throw new BusinessException("TENANT_HEADER_REQUIRED",
                    "X-Tenant-Id is required.", 400);
        }
        try {
            return new TenantContext(UUID.fromString(raw));
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("TENANT_HEADER_INVALID",
                    "X-Tenant-Id must be a UUID.", 400);
        }
    }
}
