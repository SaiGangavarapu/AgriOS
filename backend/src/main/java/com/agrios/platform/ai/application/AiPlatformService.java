package com.agrios.platform.ai.application;

import com.agrios.platform.ai.api.AiDtos;
import com.agrios.platform.ai.domain.*;
import com.agrios.platform.common.exception.*;
import com.agrios.platform.farmer.domain.FarmerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AiPlatformService {
    private final AiProviderRepository providers;
    private final AiModelProfileRepository models;
    private final KnowledgeSourceRepository sources;
    private final KnowledgeDocumentRepository documents;
    private final KnowledgeChunkRepository chunks;
    private final AssistantSessionRepository sessions;
    private final AssistantMessageRepository messages;
    private final ToolDefinitionRepository tools;
    private final ToolExecutionRepository executions;
    private final FarmerRepository farmers;
    private final ObjectMapper mapper;

    public AiPlatformService(
            AiProviderRepository providers,
            AiModelProfileRepository models,
            KnowledgeSourceRepository sources,
            KnowledgeDocumentRepository documents,
            KnowledgeChunkRepository chunks,
            AssistantSessionRepository sessions,
            AssistantMessageRepository messages,
            ToolDefinitionRepository tools,
            ToolExecutionRepository executions,
            FarmerRepository farmers,
            ObjectMapper mapper) {
        this.providers = providers;
        this.models = models;
        this.sources = sources;
        this.documents = documents;
        this.chunks = chunks;
        this.sessions = sessions;
        this.messages = messages;
        this.tools = tools;
        this.executions = executions;
        this.farmers = farmers;
        this.mapper = mapper;
    }

    @Transactional
    public UUID createProvider(UUID tenantId, AiDtos.ProviderRequest request) {
        return providers.save(AiProviderEntity.create(
                tenantId, request.providerCode(), request.providerName(),
                request.providerType(), request.endpointReference(),
                request.credentialReference())).id();
    }

    @Transactional
    public UUID createModel(UUID tenantId, AiDtos.ModelRequest request) {
        AiProviderEntity provider = providers
                .findByIdAndTenantId(request.providerId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "AI_PROVIDER_NOT_FOUND", "AI provider not found."));
        if (!"ACTIVE".equals(provider.status())) {
            throw new BusinessException(
                    "AI_PROVIDER_NOT_ACTIVE",
                    "AI provider is not active.", 422);
        }
        return models.save(AiModelProfileEntity.create(
                tenantId, provider.id(), request.modelCode(),
                request.modelName(), request.modelRole(),
                request.maxInputTokens(), request.maxOutputTokens(),
                request.temperature(), request.defaultForRole(),
                json(request.configuration() == null
                        ? Map.of() : request.configuration()))).id();
    }

    @Transactional
    public UUID createKnowledgeSource(
            UUID tenantId, AiDtos.KnowledgeSourceRequest request) {
        return sources.save(KnowledgeSourceEntity.create(
                tenantId, request.sourceCode(), request.sourceName(),
                request.sourceType(), request.ownershipScope(),
                request.sourceReference(), request.languageCode())).id();
    }

    @Transactional
    public UUID createKnowledgeDocument(
            UUID tenantId, AiDtos.KnowledgeDocumentRequest request) {
        sources.findByIdAndTenantId(request.knowledgeSourceId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "KNOWLEDGE_SOURCE_NOT_FOUND",
                        "Knowledge source not found."));
        return documents.save(KnowledgeDocumentEntity.create(
                tenantId, request.knowledgeSourceId(),
                request.externalDocumentId(), request.title(),
                request.contentHash(), request.mimeType(),
                json(request.metadata() == null ? Map.of() : request.metadata()),
                request.farmerId(), request.farmId(),
                request.fieldId(), request.cropCycleId())).id();
    }

    @Transactional
    public UUID addKnowledgeChunk(
            UUID tenantId, UUID documentId,
            AiDtos.KnowledgeChunkRequest request) {
        documents.findByIdAndTenantId(documentId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "KNOWLEDGE_DOCUMENT_NOT_FOUND",
                        "Knowledge document not found."));
        return chunks.save(KnowledgeChunkEntity.create(
                tenantId, documentId, request.chunkIndex(),
                request.chunkText(), request.tokenCount(),
                json(request.metadata() == null ? Map.of() : request.metadata()),
                request.embeddingModelCode(),
                request.embeddingVector() == null
                        ? null : json(request.embeddingVector()))).id();
    }

    @Transactional
    public AiDtos.AssistantSessionResponse createSession(
            UUID tenantId, AiDtos.AssistantSessionRequest request) {
        if (request.farmerId() != null) {
            farmers.findByIdAndTenantId(request.farmerId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "FARMER_NOT_FOUND", "Farmer not found."));
        }
        AssistantSessionEntity value = AssistantSessionEntity.create(
                tenantId, request.farmerId(), request.assistantType(),
                request.title(), request.languageCode());
        return AiDtos.AssistantSessionResponse.from(sessions.save(value));
    }

    @Transactional
    public AiDtos.AssistantMessageResponse addUserMessage(
            UUID tenantId, UUID sessionId,
            AiDtos.AssistantMessageRequest request) {
        requireSession(tenantId, sessionId);
        AssistantMessageEntity value = AssistantMessageEntity.create(
                sessionId, "USER", request.messageText(),
                request.modelCode(),
                json(request.metadata() == null ? Map.of() : request.metadata()));
        return AiDtos.AssistantMessageResponse.from(messages.save(value));
    }

    @Transactional(readOnly = true)
    public List<AiDtos.AssistantMessageResponse> conversation(
            UUID tenantId, UUID sessionId) {
        requireSession(tenantId, sessionId);
        return messages.findByAssistantSessionIdOrderByCreatedAt(sessionId)
                .stream().map(AiDtos.AssistantMessageResponse::from).toList();
    }

    @Transactional
    public UUID createTool(UUID tenantId, AiDtos.ToolDefinitionRequest request) {
        return tools.save(ToolDefinitionEntity.create(
                tenantId, request.toolCode(), request.toolName(),
                request.domainModule(), request.description(),
                json(request.inputSchema() == null ? Map.of() : request.inputSchema()),
                json(request.outputSchema() == null ? Map.of() : request.outputSchema()),
                request.riskLevel(), request.requiresConfirmation())).id();
    }

    @Transactional(readOnly = true)
    public List<String> listToolCodes(UUID tenantId) {
        return tools.findByTenantIdAndEnabledTrueOrderByToolCode(tenantId)
                .stream().map(ToolDefinitionEntity::toolCode).toList();
    }

    @Transactional
    public UUID requestToolExecution(
            UUID tenantId, AiDtos.ToolExecutionRequest request) {
        if (executions.existsByTenantIdAndIdempotencyKey(
                tenantId, request.idempotencyKey())) {
            throw new ConflictException(
                    "TOOL_EXECUTION_DUPLICATE",
                    "Tool execution idempotency key already exists.");
        }

        ToolDefinitionEntity tool = tools
                .findByIdAndTenantId(request.toolDefinitionId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TOOL_DEFINITION_NOT_FOUND",
                        "Tool definition not found."));
        if (!tool.enabled()) {
            throw new BusinessException(
                    "TOOL_DISABLED", "Tool is disabled.", 422);
        }

        return executions.save(ToolExecutionEntity.create(
                tenantId, request.assistantSessionId(),
                request.assistantMessageId(), tool.id(),
                request.idempotencyKey(),
                json(request.inputPayload() == null
                        ? Map.of() : request.inputPayload()),
                tool.requiresConfirmation())).id();
    }

    @Transactional
    public void approveToolExecution(UUID tenantId, UUID executionId) {
        executions.findByIdAndTenantId(executionId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TOOL_EXECUTION_NOT_FOUND",
                        "Tool execution not found."))
                .approve();
    }

    private AssistantSessionEntity requireSession(
            UUID tenantId, UUID sessionId) {
        return sessions.findByIdAndTenantId(sessionId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ASSISTANT_SESSION_NOT_FOUND",
                        "Assistant session not found."));
    }

    private String json(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException(
                    "JSON_SERIALIZATION_FAILED",
                    "Unable to serialize AI platform data.", 500);
        }
    }
}
