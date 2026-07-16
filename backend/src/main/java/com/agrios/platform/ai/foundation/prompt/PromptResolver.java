package com.agrios.platform.ai.foundation.prompt;

import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class PromptResolver {
    private final PromptTemplateRepository repository;
    private static final String SAFE = "You are the AgriOS %s assistant. Provide practical, cautious agricultural guidance in language %s. Do not claim to have executed tools. Ask for expert verification for high-risk decisions.";
    public PromptResolver(PromptTemplateRepository repository){this.repository=repository;}
    public ResolvedPrompt resolve(UUID tenantId, String type, String language, String userText){
        var value = repository.findFirstByTenantIdAndAssistantTypeAndLanguageCodeAndStatusOrderByVersionNoDesc(tenantId,type,language,"ACTIVE")
                .or(() -> repository.findFirstByTenantIdAndAssistantTypeAndLanguageCodeAndStatusOrderByVersionNoDesc(tenantId,type,"en","ACTIVE"));
        if(value.isPresent()){
            var p=value.get();
            String user=p.userPromptTemplate()==null?userText:p.userPromptTemplate().replace("{message}",userText);
            return new ResolvedPrompt(p.templateCode(),p.systemPrompt(),user);
        }
        return new ResolvedPrompt("SAFE_FALLBACK_"+type,SAFE.formatted(type,language),userText);
    }
    public record ResolvedPrompt(String templateCode,String systemPrompt,String userPrompt){}
}
