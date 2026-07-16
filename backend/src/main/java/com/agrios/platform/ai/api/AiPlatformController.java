package com.agrios.platform.ai.api;

import com.agrios.platform.ai.application.AiPlatformService;
import com.agrios.platform.ai.foundation.application.AssistantOrchestrationService;
import com.agrios.platform.common.web.TenantContextResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AiPlatformController {
    private final AiPlatformService service;
    private final TenantContextResolver tenants;
    private final AssistantOrchestrationService orchestration;

    public AiPlatformController(
            AiPlatformService service, TenantContextResolver tenants,
            AssistantOrchestrationService orchestration) {
        this.service = service; this.tenants = tenants; this.orchestration = orchestration;
    }

    @PostMapping("/ai/providers")
    Map<String, UUID> provider(
            @Valid @RequestBody AiDtos.ProviderRequest body,
            HttpServletRequest request) {
        return Map.of("providerId", service.createProvider(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/ai/models")
    Map<String, UUID> model(
            @Valid @RequestBody AiDtos.ModelRequest body,
            HttpServletRequest request) {
        return Map.of("modelId", service.createModel(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/ai/knowledge/sources")
    Map<String, UUID> knowledgeSource(
            @Valid @RequestBody AiDtos.KnowledgeSourceRequest body,
            HttpServletRequest request) {
        return Map.of("knowledgeSourceId", service.createKnowledgeSource(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/ai/knowledge/documents")
    Map<String, UUID> knowledgeDocument(
            @Valid @RequestBody AiDtos.KnowledgeDocumentRequest body,
            HttpServletRequest request) {
        return Map.of("knowledgeDocumentId", service.createKnowledgeDocument(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/ai/knowledge/documents/{documentId}/chunks")
    Map<String, UUID> knowledgeChunk(
            @PathVariable UUID documentId,
            @Valid @RequestBody AiDtos.KnowledgeChunkRequest body,
            HttpServletRequest request) {
        return Map.of("knowledgeChunkId", service.addKnowledgeChunk(
                tenants.resolve(request).tenantId(), documentId, body));
    }

    @PostMapping("/ai/assistant/sessions")
    AiDtos.AssistantSessionResponse session(
            @Valid @RequestBody AiDtos.AssistantSessionRequest body,
            HttpServletRequest request) {
        return service.createSession(
                tenants.resolve(request).tenantId(), body);
    }

    @PostMapping("/ai/assistant/sessions/{sessionId}/messages")
    AiDtos.AssistantMessageResponse message(
            @PathVariable UUID sessionId,
            @Valid @RequestBody AiDtos.AssistantMessageRequest body,
            HttpServletRequest request) {
        return service.addUserMessage(
                tenants.resolve(request).tenantId(), sessionId, body);
    }

    @PostMapping("/ai/assistant/sessions/{sessionId}/submit")
    AiDtos.AssistantExchangeResponse submit(
            @PathVariable UUID sessionId,
            @Valid @RequestBody AiDtos.AssistantSubmitRequest body,
            HttpServletRequest request) {
        String correlationId = Optional.ofNullable(request.getHeader("X-Correlation-ID"))
                .filter(v -> !v.isBlank()).orElse(UUID.randomUUID().toString());
        return orchestration.submit(tenants.resolve(request).tenantId(), sessionId, body, correlationId);
    }

    @GetMapping("/ai/assistant/sessions/{sessionId}/messages")
    List<AiDtos.AssistantMessageResponse> conversation(
            @PathVariable UUID sessionId,
            HttpServletRequest request) {
        return service.conversation(
                tenants.resolve(request).tenantId(), sessionId);
    }

    @PostMapping("/ai/tools")
    Map<String, UUID> tool(
            @Valid @RequestBody AiDtos.ToolDefinitionRequest body,
            HttpServletRequest request) {
        return Map.of("toolDefinitionId", service.createTool(
                tenants.resolve(request).tenantId(), body));
    }

    @GetMapping("/ai/tools")
    List<String> tools(HttpServletRequest request) {
        return service.listToolCodes(
                tenants.resolve(request).tenantId());
    }

    @PostMapping("/ai/tool-executions")
    Map<String, UUID> toolExecution(
            @Valid @RequestBody AiDtos.ToolExecutionRequest body,
            HttpServletRequest request) {
        return Map.of("toolExecutionId", service.requestToolExecution(
                tenants.resolve(request).tenantId(), body));
    }

    @PostMapping("/ai/tool-executions/{executionId}/approve")
    void approveToolExecution(
            @PathVariable UUID executionId,
            HttpServletRequest request) {
        service.approveToolExecution(
                tenants.resolve(request).tenantId(), executionId);
    }
}
