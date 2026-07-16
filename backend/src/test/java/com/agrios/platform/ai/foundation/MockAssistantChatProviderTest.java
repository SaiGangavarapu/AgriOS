package com.agrios.platform.ai.foundation;
import static org.junit.jupiter.api.Assertions.*;
import com.agrios.platform.ai.foundation.provider.*;
import java.util.List;
import org.junit.jupiter.api.Test;
class MockAssistantChatProviderTest {
 @Test void isDeterministic(){
  var provider=new MockAssistantChatProvider();
  var request=new AssistantChatProvider.ChatProviderRequest("mock","system","water now",List.of(),100);
  assertEquals(provider.chat(request).text(),provider.chat(request).text());
 }
}
