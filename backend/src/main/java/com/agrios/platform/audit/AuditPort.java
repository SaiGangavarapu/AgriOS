package com.agrios.platform.audit;

public interface AuditPort {
    void record(AuditEvent event);
}
