-- Rollback script for multi-tenancy migration V1

-- Drop foreign key constraint if exists
ALTER TABLE users DROP FOREIGN KEY IF EXISTS fk_user_tenant;

-- Drop tenant_id column from users table if exists
ALTER TABLE users DROP COLUMN IF EXISTS tenant_id;

-- Drop tenant table if exists
DROP TABLE IF EXISTS tenant;
