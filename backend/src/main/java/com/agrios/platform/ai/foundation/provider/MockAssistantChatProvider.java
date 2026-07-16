package com.agrios.platform.ai.foundation.provider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix="agrios.ai.mock", name="enabled", havingValue="true")
public class MockAssistantChatProvider implements AssistantChatProvider {
    @Override public String providerType() { return "MOCK"; }
    @Override public boolean available() { return true; }
    @Override public ChatProviderResponse chat(ChatProviderRequest request) {
        return new ChatProviderResponse("Mock assistant response: " + request.userPrompt(), 12, 8, "STOP");
    }
}
