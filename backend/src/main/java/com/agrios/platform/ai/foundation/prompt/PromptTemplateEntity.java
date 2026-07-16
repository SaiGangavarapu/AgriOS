package com.agrios.platform.ai.foundation.prompt;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="prompt_template", schema="ai")
public class PromptTemplateEntity {
    @Id private UUID id;
    @Column(nullable=false) private UUID tenantId;
    @Column(nullable=false) private String templateCode;
    @Column(nullable=false) private String templateName;
    @Column(nullable=false) private String assistantType;
    @Column(nullable=false) private String languageCode;
    @Column(nullable=false, columnDefinition="text") private String systemPrompt;
    @Column(columnDefinition="text") private String userPromptTemplate;
    @Column(nullable=false) private Integer versionNo;
    @Column(nullable=false) private String status;
    @Column(nullable=false) private Instant createdAt;
    protected PromptTemplateEntity() {}
    public String templateCode(){return templateCode;} public String systemPrompt(){return systemPrompt;}
    public String userPromptTemplate(){return userPromptTemplate;}
}
