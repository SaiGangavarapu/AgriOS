CREATE TABLE ai.knowledge_source (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  source_code varchar(120) NOT NULL,
  source_name varchar(240) NOT NULL,
  source_type varchar(60) NOT NULL
    CHECK (source_type IN ('DOCUMENT','DATABASE_VIEW','API','EXPERT_NOTE','ADVISORY','WEATHER','MARKET','COMPLIANCE','FARM_RECORD')),
  ownership_scope varchar(40) NOT NULL
    CHECK (ownership_scope IN ('PLATFORM','TENANT','FARMER','FARM','FIELD','CROP_CYCLE','ORGANIZATION')),
  source_reference varchar(500) NULL,
  language_code varchar(10) NOT NULL DEFAULT 'en',
  status varchar(30) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','INACTIVE','INDEXING','FAILED','ARCHIVED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_knowledge_source_code UNIQUE (tenant_id, source_code)
);

CREATE TABLE ai.knowledge_document (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  knowledge_source_id uuid NOT NULL REFERENCES ai.knowledge_source(id),
  external_document_id varchar(240) NULL,
  title varchar(500) NOT NULL,
  content_hash varchar(128) NOT NULL,
  mime_type varchar(120) NULL,
  metadata jsonb NOT NULL DEFAULT '{}'::jsonb,
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  farm_id uuid NULL REFERENCES farm.farm(id),
  field_id uuid NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  status varchar(30) NOT NULL DEFAULT 'READY'
    CHECK (status IN ('UPLOADED','PARSING','READY','INDEXED','FAILED','ARCHIVED')),
  indexed_at timestamptz NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_knowledge_document_hash UNIQUE (tenant_id, knowledge_source_id, content_hash)
);

CREATE TABLE ai.knowledge_chunk (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  knowledge_document_id uuid NOT NULL REFERENCES ai.knowledge_document(id) ON DELETE CASCADE,
  chunk_index integer NOT NULL,
  chunk_text text NOT NULL,
  token_count integer NULL,
  metadata jsonb NOT NULL DEFAULT '{}'::jsonb,
  embedding_model_code varchar(120) NULL,
  embedding_vector jsonb NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_knowledge_chunk_index UNIQUE (knowledge_document_id, chunk_index)
);

CREATE TABLE ai.retrieval_request (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  query_text text NOT NULL,
  query_language varchar(10) NOT NULL DEFAULT 'en',
  farmer_id uuid NULL REFERENCES farmer.farmer(id),
  farm_id uuid NULL REFERENCES farm.farm(id),
  field_id uuid NULL REFERENCES farm.field(id),
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  top_k integer NOT NULL DEFAULT 5,
  filters_json jsonb NOT NULL DEFAULT '{}'::jsonb,
  requested_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE ai.retrieval_result (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  retrieval_request_id uuid NOT NULL REFERENCES ai.retrieval_request(id) ON DELETE CASCADE,
  knowledge_chunk_id uuid NOT NULL REFERENCES ai.knowledge_chunk(id),
  rank_no integer NOT NULL,
  similarity_score numeric(12,8) NULL,
  rerank_score numeric(12,8) NULL,
  citation_label varchar(160) NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_retrieval_result_rank UNIQUE (retrieval_request_id, rank_no)
);

CREATE INDEX ix_knowledge_document_scope
  ON ai.knowledge_document(tenant_id, farmer_id, farm_id, field_id, crop_cycle_id, status);

CREATE INDEX ix_knowledge_chunk_document
  ON ai.knowledge_chunk(knowledge_document_id, chunk_index);

CREATE INDEX ix_retrieval_request_scope
  ON ai.retrieval_request(tenant_id, farmer_id, farm_id, field_id, crop_cycle_id, requested_at DESC);

CREATE TRIGGER trg_knowledge_source_updated_at
BEFORE UPDATE ON ai.knowledge_source
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
