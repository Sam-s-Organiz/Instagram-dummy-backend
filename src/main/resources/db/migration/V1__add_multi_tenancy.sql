-- Create the tenant table if not exists (idempotent)
CREATE TABLE IF NOT EXISTS tenant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    keycloak_realm VARCHAR(100)
);

-- Add tenant_id column to users if not exists
ALTER TABLE users
ADD COLUMN IF NOT EXISTS tenant_id VARCHAR(50) NOT NULL DEFAULT 'default';

-- Add foreign key if not exists (MySQL doesn't support IF NOT EXISTS for constraints, so drop and recreate if needed)
ALTER TABLE users DROP FOREIGN KEY IF EXISTS fk_user_tenant;
ALTER TABLE users
ADD CONSTRAINT fk_user_tenant
FOREIGN KEY (tenant_id) REFERENCES tenant(tenant_id)
ON DELETE RESTRICT;

-- Seed initial tenants (use INSERT IGNORE for idempotency)
INSERT IGNORE INTO tenant (tenant_id, name, description, keycloak_realm)
VALUES
    ('default', 'Default Tenant', 'Fallback for legacy data', 'master'),
    ('instagram-dummy', 'Instagram Dummy Tenant', 'Primary tenant for app users', 'instagram-realm');

-- Backfill existing users (update to a specific tenant if 'default' or null)
UPDATE users
SET tenant_id = 'instagram-dummy'
WHERE tenant_id IS NULL OR tenant_id = 'default';
