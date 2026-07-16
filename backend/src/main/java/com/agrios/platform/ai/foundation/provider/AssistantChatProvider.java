package com.agrios.platform.ai.foundation.provider;

import java.util.List;

public interface AssistantChatProvider {
    String providerType();
    boolean available();
    ChatProviderResponse chat(ChatProviderRequest request);

    record ChatProviderRequest(String modelCode, String systemPrompt, String userPrompt,
                               List<HistoryMessage> history, int maxOutputTokens) {}
    record HistoryMessage(String role, String content) {}
    record ChatProviderResponse(String text, Integer promptTokens, Integer completionTokens,
                                String finishReason) {}
}
