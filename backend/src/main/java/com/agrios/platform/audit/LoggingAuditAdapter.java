package com.agrios.platform.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoggingAuditAdapter implements AuditPort {
    private static final Logger log = LoggerFactory.getLogger(LoggingAuditAdapter.class);

    @Override
    public void record(AuditEvent event) {
        log.info("audit action={} targetType={} targetId={} actorId={} correlationId={}",
                event.action(), event.targetType(), event.targetId(), event.actorId(), event.correlationId());
    }
}
