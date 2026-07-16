package com.agrios.platform.ai.foundation.provider;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(ChatModel.class)
@ConditionalOnProperty(prefix="agrios.ai.ollama", name="enabled", havingValue="true")
public class OllamaAssistantChatProvider implements AssistantChatProvider {
    private final ChatClient chatClient;

    public OllamaAssistantChatProvider(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    @Override public String providerType() { return "OLLAMA"; }
    @Override public boolean available() { return true; }

    @Override
    public ChatProviderResponse chat(ChatProviderRequest request) {
        String content = chatClient.prompt()
                .system(request.systemPrompt())
                .user(request.userPrompt())
                .call()
                .content();
        return new ChatProviderResponse(content, null, null, "UNKNOWN");
    }
}
