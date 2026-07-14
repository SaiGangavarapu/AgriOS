SELECT extname
FROM pg_extension
WHERE extname IN ('postgis', 'pgcrypto', 'citext')
ORDER BY extname;

SELECT schema_name
FROM information_schema.schemata
WHERE schema_name IN (
  'configuration','identity','farmer','household','organization',
  'farm','tenure','consent','audit','integration'
)
ORDER BY schema_name;

SELECT table_schema, table_name
FROM information_schema.tables
WHERE table_schema IN (
  'configuration','identity','farmer','farm','tenure',
  'consent','audit','integration'
)
ORDER BY table_schema, table_name;

SELECT indexname
FROM pg_indexes
WHERE schemaname IN ('farmer','farm','tenure','consent','audit','integration')
ORDER BY indexname;
