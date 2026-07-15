package com.agrios.platform.ai.api;

import com.agrios.platform.ai.domain.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.*;

public final class AiDtos {
    private AiDtos() {}

    public record ProviderRequest(
            @NotBlank String providerCode,
            @NotBlank String providerName,
            @NotBlank String providerType,
            String endpointReference,
            String credentialReference) {}

    public record ModelRequest(
            @NotNull UUID providerId,
            @NotBlank String modelCode,
            @NotBlank String modelName,
            @NotBlank String modelRole,
            Integer maxInputTokens,
            Integer maxOutputTokens,
            BigDecimal temperature,
            boolean defaultForRole,
            Map<String,Object> configuration) {}

    public record KnowledgeSourceRequest(
            @NotBlank String sourceCode,
            @NotBlank String sourceName,
            @NotBlank String sourceType,
            @NotBlank String ownershipScope,
            String sourceReference,
            @NotBlank String languageCode) {}

    public record KnowledgeDocumentRequest(
            @NotNull UUID knowledgeSourceId,
            String externalDocumentId,
            @NotBlank String title,
            @NotBlank String contentHash,
            String mimeType,
            Map<String,Object> metadata,
            UUID farmerId,
            UUID farmId,
            UUID fieldId,
            UUID cropCycleId) {}

    public record KnowledgeChunkRequest(
            @Min(0) int chunkIndex,
            @NotBlank String chunkText,
            Integer tokenCount,
            Map<String,Object> metadata,
            String embeddingModelCode,
            List<BigDecimal> embeddingVector) {}

    public record AssistantSessionRequest(
            UUID farmerId,
            @NotBlank String assistantType,
            String title,
            @NotBlank String languageCode) {}

    public record AssistantMessageRequest(
            @NotBlank String messageText,
            String modelCode,
            Map<String,Object> metadata) {}

    public record ToolDefinitionRequest(
            @NotBlank String toolCode,
            @NotBlank String toolName,
            @NotBlank String domainModule,
            @NotBlank String description,
            Map<String,Object> inputSchema,
            Map<String,Object> outputSchema,
            @NotBlank String riskLevel,
            boolean requiresConfirmation) {}

    public record ToolExecutionRequest(
            UUID assistantSessionId,
            UUID assistantMessageId,
            @NotNull UUID toolDefinitionId,
            @NotBlank String idempotencyKey,
            Map<String,Object> inputPayload) {}

    public record AssistantSessionResponse(
            UUID id, UUID farmerId, String assistantType,
            String languageCode, String status) {
        public static AssistantSessionResponse from(AssistantSessionEntity value) {
            return new AssistantSessionResponse(
                    value.id(), value.farmerId(), value.assistantType(),
                    value.languageCode(), value.status());
        }
    }

    public record AssistantMessageResponse(
            UUID id, String messageRole, String messageText) {
        public static AssistantMessageResponse from(AssistantMessageEntity value) {
            return new AssistantMessageResponse(
                    value.id(), value.messageRole(), value.messageText());
        }
    }
}
