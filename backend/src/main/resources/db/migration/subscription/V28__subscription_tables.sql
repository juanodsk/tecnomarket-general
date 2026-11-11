CREATE TABLE IF NOT EXISTS subscription_plan (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(120) NOT NULL,
    monthly_price DECIMAL(12, 2) NOT NULL,
    yearly_price DECIMAL(12, 2) NOT NULL,
    included_users INTEGER NOT NULL,
    max_businesses INTEGER NOT NULL,
    features TEXT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_subscription_plan_code_active
    ON subscription_plan (code)
    WHERE deleted_at IS NULL;

CREATE TABLE IF NOT EXISTS business_subscription (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    plan_id INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL,
    billing_cycle VARCHAR(20) NOT NULL,
    current_period_start TIMESTAMP NOT NULL,
    current_period_end TIMESTAMP NOT NULL,
    trial_ends_at TIMESTAMP,
    cancel_at_period_end BOOLEAN NOT NULL DEFAULT FALSE,
    payment_provider VARCHAR(30) NOT NULL DEFAULT 'MANUAL',
    provider_customer_id VARCHAR(150),
    provider_subscription_id VARCHAR(150),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE business_subscription
    DROP CONSTRAINT IF EXISTS fk_business_id_business_subscription;

ALTER TABLE business_subscription
    ADD CONSTRAINT fk_business_id_business_subscription
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE business_subscription
    DROP CONSTRAINT IF EXISTS fk_plan_id_business_subscription;

ALTER TABLE business_subscription
    ADD CONSTRAINT fk_plan_id_business_subscription
        FOREIGN KEY (plan_id) REFERENCES subscription_plan (id);

CREATE UNIQUE INDEX IF NOT EXISTS uq_business_subscription_active_business
    ON business_subscription (business_id)
    WHERE active = TRUE AND deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_business_subscription_status
    ON business_subscription (status, active, current_period_end);

CREATE TABLE IF NOT EXISTS subscription_usage (
    id SERIAL PRIMARY KEY,
    business_subscription_id INTEGER NOT NULL,
    users_count INTEGER NOT NULL DEFAULT 0,
    orders_count INTEGER NOT NULL DEFAULT 0,
    storage_used BIGINT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP
);

ALTER TABLE subscription_usage
    DROP CONSTRAINT IF EXISTS fk_business_subscription_id_subscription_usage;

ALTER TABLE subscription_usage
    ADD CONSTRAINT fk_business_subscription_id_subscription_usage
        FOREIGN KEY (business_subscription_id) REFERENCES business_subscription (id);

CREATE UNIQUE INDEX IF NOT EXISTS uq_subscription_usage_subscription
    ON subscription_usage (business_subscription_id);
