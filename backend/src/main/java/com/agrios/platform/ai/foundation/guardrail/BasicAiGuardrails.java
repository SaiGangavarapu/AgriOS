package com.agrios.platform.ai.foundation.guardrail;

import com.agrios.platform.ai.foundation.config.AiFoundationProperties;
import com.agrios.platform.common.exception.BusinessException;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class BasicAiGuardrails {
    private static final Pattern INJECTION=Pattern.compile("(?i)(ignore|override).{0,30}(previous|system|developer).{0,20}(instruction|prompt)");
    private static final Pattern RESTRICTED=Pattern.compile("(?i)\\b(delete all|drop table|bypass authorization|execute without confirmation)\\b");
    private final AiFoundationProperties properties;
    public BasicAiGuardrails(AiFoundationProperties properties){this.properties=properties;}
    public void validateInput(String text){
        if(text==null||text.isBlank()) throw new BusinessException("AI_MESSAGE_REQUIRED","Message is required.",400);
        if(text.length()>properties.maxInputCharacters()) throw new BusinessException("AI_MESSAGE_TOO_LARGE","Message exceeds the configured input limit.",400);
        if(INJECTION.matcher(text).find()) throw new BusinessException("AI_PROMPT_INJECTION_SUSPECTED","The message contains instruction-override language.",422);
        if(RESTRICTED.matcher(text).find()) throw new BusinessException("AI_RESTRICTED_ACTION_LANGUAGE","The message contains restricted action language.",422);
    }
    public String validateOutput(String text){
        if(text==null||text.isBlank()) throw new BusinessException("AI_EMPTY_RESPONSE","AI provider returned an empty response.",502);
        if(text.length()>properties.maxOutputCharacters()) return text.substring(0,properties.maxOutputCharacters());
        return text;
    }
}
