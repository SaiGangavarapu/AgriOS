package com.agrios.platform.ai.foundation.application;

import com.agrios.platform.ai.api.AiDtos;
import com.agrios.platform.ai.domain.*;
import com.agrios.platform.ai.foundation.config.AiFoundationProperties;
import com.agrios.platform.ai.foundation.guardrail.BasicAiGuardrails;
import com.agrios.platform.ai.foundation.prompt.PromptResolver;
import com.agrios.platform.ai.foundation.provider.AssistantChatProvider;
import com.agrios.platform.common.exception.*;
import java.time.Duration;
import java.util.*;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssistantOrchestrationService {
    private static final Logger log=LoggerFactory.getLogger(AssistantOrchestrationService.class);
    private final AssistantSessionRepository sessions; private final AssistantMessageRepository messages;
    private final ProviderRouter router; private final PromptResolver prompts; private final BasicAiGuardrails guardrails;
    private final AiFoundationProperties properties;
    public AssistantOrchestrationService(AssistantSessionRepository sessions, AssistantMessageRepository messages,
            ProviderRouter router, PromptResolver prompts, BasicAiGuardrails guardrails, AiFoundationProperties properties){
        this.sessions=sessions;this.messages=messages;this.router=router;this.prompts=prompts;this.guardrails=guardrails;this.properties=properties;
    }
    @Transactional
    public AiDtos.AssistantExchangeResponse submit(UUID tenantId, UUID sessionId, AiDtos.AssistantSubmitRequest request, String correlationId){
        var session=sessions.findByIdAndTenantId(sessionId,tenantId).orElseThrow(()->new ResourceNotFoundException("ASSISTANT_SESSION_NOT_FOUND","Assistant session not found."));
        if(!"ACTIVE".equals(session.status())) throw new BusinessException("ASSISTANT_SESSION_NOT_ACTIVE","Assistant session is not active.",422);
        guardrails.validateInput(request.messageText());
        var selection=router.resolve(tenantId,request.modelCode());
        var resolved=prompts.resolve(tenantId,session.assistantType(),session.languageCode(),request.messageText());
        var user=messages.save(AssistantMessageEntity.create(sessionId,"USER",request.messageText(),selection.modelCode(),"{}"));
        var history=messages.findByAssistantSessionIdOrderByCreatedAtDesc(sessionId,org.springframework.data.domain.PageRequest.of(0,properties.historyLimit())).stream()
                .sorted(Comparator.comparing(AssistantMessageEntity::createdAt))
                .filter(m->!m.id().equals(user.id()))
                .map(m->new AssistantChatProvider.HistoryMessage(m.messageRole(),m.messageText())).toList();
        long started=System.nanoTime();
        AssistantChatProvider.ChatProviderResponse response;
        try {
            response=selection.adapter().chat(new AssistantChatProvider.ChatProviderRequest(selection.modelCode(),resolved.systemPrompt(),resolved.userPrompt(),history,selection.maxOutputTokens()));
        } catch (RuntimeException ex) {
            log.warn("AI provider call failed provider={} model={} correlationId={}",selection.providerCode(),selection.modelCode(),correlationId,ex);
            throw new BusinessException("AI_PROVIDER_CALL_FAILED","AI provider call failed. Please retry later.",503);
        }
        long latency=Duration.ofNanos(System.nanoTime()-started).toMillis();
        String text=guardrails.validateOutput(response.text());
        var assistant=messages.save(AssistantMessageEntity.createAssistant(sessionId,text,selection.modelCode(),response.promptTokens(),response.completionTokens(),latency,response.finishReason(),Map.of("providerCode",selection.providerCode(),"promptTemplateCode",resolved.templateCode(),"correlationId",correlationId)));
        log.info("AI response completed provider={} model={} latencyMs={} correlationId={}",selection.providerCode(),selection.modelCode(),latency,correlationId);
        return new AiDtos.AssistantExchangeResponse(user.id(),assistant.id(),text,selection.providerCode(),selection.modelCode(),latency,response.promptTokens(),response.completionTokens(),response.finishReason(),correlationId,session.languageCode());
    }
}
