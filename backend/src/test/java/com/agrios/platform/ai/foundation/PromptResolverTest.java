package com.agrios.platform.ai.foundation;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.agrios.platform.ai.foundation.prompt.*;
import java.util.*;
import org.junit.jupiter.api.Test;
class PromptResolverTest {
 @Test void usesSafeFallbackWhenDatabaseHasNoTemplate(){
  var repo=mock(PromptTemplateRepository.class);
  when(repo.findFirstByTenantIdAndAssistantTypeAndLanguageCodeAndStatusOrderByVersionNoDesc(any(),any(),any(),any())).thenReturn(Optional.empty());
  var result=new PromptResolver(repo).resolve(UUID.randomUUID(),"GENERAL","te","hello");
  assertTrue(result.templateCode().startsWith("SAFE_FALLBACK")); assertEquals("hello",result.userPrompt());
 }
}
