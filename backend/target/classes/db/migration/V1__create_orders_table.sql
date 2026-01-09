CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(22) PRIMARY KEY,
    merchant_id UUID NOT NULL REFERENCES merchant(id),
    amount BIGINT NOT NULL CHECK (amount >= 100),
    currency VARCHAR(10) DEFAULT 'INR',
    receipt VARCHAR(255),
    notes JSONB,
    status VARCHAR(50) NOT NULL DEFAULT 'created',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);
