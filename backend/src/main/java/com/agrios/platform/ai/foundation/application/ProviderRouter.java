package com.agrios.platform.ai.foundation.application;

import com.agrios.platform.ai.domain.*;
import com.agrios.platform.ai.foundation.provider.AssistantChatProvider;
import com.agrios.platform.common.exception.BusinessException;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ProviderRouter {
    private final AiModelProfileRepository models;
    private final AiProviderRepository providers;
    private final Map<String, AssistantChatProvider> adapters;

    public ProviderRouter(AiModelProfileRepository models, AiProviderRepository providers,
                          List<AssistantChatProvider> adapters) {
        this.models=models; this.providers=providers;
        Map<String,AssistantChatProvider> map=new HashMap<>();
        adapters.forEach(a->map.put(a.providerType().toUpperCase(Locale.ROOT),a));
        this.adapters=Map.copyOf(map);
    }

    public Selection resolve(UUID tenantId, String requestedModel) {
        var candidates=models.findByTenantIdAndModelRoleAndEnabledTrue(tenantId,"CHAT");
        AiModelProfileEntity model=candidates.stream()
                .filter(m->requestedModel!=null&&requestedModel.equalsIgnoreCase(m.modelCode())).findFirst()
                .orElseGet(()->candidates.stream().filter(AiModelProfileEntity::defaultForRole).findFirst()
                        .orElseGet(()->candidates.stream().findFirst().orElse(null)));
        if(model==null) {
            var mock=adapters.get("MOCK");
            if(mock!=null&&mock.available()) return new Selection("MOCK","mock-deterministic",mock,2048);
            throw new BusinessException("AI_PROVIDER_NOT_CONFIGURED","No active chat model/provider is configured.",503);
        }
        AiProviderEntity provider=providers.findByIdAndTenantId(model.providerId(),tenantId)
                .orElseThrow(()->new BusinessException("AI_PROVIDER_NOT_CONFIGURED","Configured AI provider was not found.",503));
        var adapter=adapters.get(provider.providerType().toUpperCase(Locale.ROOT));
        if(adapter==null||!adapter.available()) throw new BusinessException("AI_PROVIDER_UNAVAILABLE","Selected AI provider is unavailable.",503);
        return new Selection(provider.providerCode(),model.modelCode(),adapter,
                model.maxOutputTokens()==null?2048:model.maxOutputTokens());
    }
    public record Selection(String providerCode,String modelCode,AssistantChatProvider adapter,int maxOutputTokens){}
}
