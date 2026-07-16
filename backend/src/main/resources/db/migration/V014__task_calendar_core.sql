CREATE SCHEMA IF NOT EXISTS tasks;

CREATE TABLE tasks.task (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id uuid NOT NULL REFERENCES configuration.tenant(id),
  owner_context varchar(80) NOT NULL,
  owner_entity_id uuid NOT NULL,
  crop_cycle_id uuid NULL REFERENCES cropcycle.crop_cycle(id),
  task_type varchar(80) NOT NULL,
  title varchar(240) NOT NULL,
  description text NULL,
  priority varchar(20) NOT NULL DEFAULT 'NORMAL'
    CHECK (priority IN ('CRITICAL','HIGH','NORMAL','LOW')),
  due_at timestamptz NULL,
  assigned_to uuid NULL,
  status varchar(30) NOT NULL DEFAULT 'OPEN'
    CHECK (status IN ('OPEN','ASSIGNED','IN_PROGRESS','COMPLETED','DEFERRED','CANCELLED','EXPIRED')),
  completed_at timestamptz NULL,
  completion_reference_type varchar(80) NULL,
  completion_reference_id uuid NULL,
  version bigint NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now(),
  created_by uuid NULL,
  updated_at timestamptz NOT NULL DEFAULT now(),
  updated_by uuid NULL
);

CREATE TABLE tasks.task_dependency (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  task_id uuid NOT NULL REFERENCES tasks.task(id) ON DELETE CASCADE,
  depends_on_task_id uuid NOT NULL REFERENCES tasks.task(id),
  dependency_type varchar(30) NOT NULL DEFAULT 'FINISH_TO_START',
  created_at timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT uq_task_dependency UNIQUE (task_id, depends_on_task_id),
  CONSTRAINT ck_task_no_self_dependency CHECK (task_id <> depends_on_task_id)
);

CREATE TABLE tasks.task_event (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  task_id uuid NOT NULL REFERENCES tasks.task(id) ON DELETE CASCADE,
  event_type varchar(40) NOT NULL,
  occurred_at timestamptz NOT NULL DEFAULT now(),
  actor_id uuid NULL,
  reason text NULL,
  metadata jsonb NOT NULL DEFAULT '{}'::jsonb
);

CREATE INDEX ix_task_assignee_status_due
  ON tasks.task(assigned_to, status, due_at);

CREATE INDEX ix_task_cycle_status_due
  ON tasks.task(crop_cycle_id, status, due_at);

CREATE INDEX ix_task_owner
  ON tasks.task(owner_context, owner_entity_id);

CREATE TRIGGER trg_task_updated_at
BEFORE UPDATE ON tasks.task
FOR EACH ROW EXECUTE FUNCTION configuration.set_updated_at();
