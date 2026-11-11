CREATE TABLE IF NOT EXISTS inventory_item (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    name VARCHAR(150) NOT NULL,
    item_category_id INTEGER NOT NULL,
    description VARCHAR(500),
    sku VARCHAR(80) NOT NULL,
    quantity INTEGER NOT NULL,
    minimum_stock INTEGER NOT NULL,
    unit_cost NUMERIC(12, 2) NOT NULL,
    sale_price NUMERIC(12, 2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE inventory_item
    DROP CONSTRAINT IF EXISTS fk_business_id_inventory_item;

ALTER TABLE inventory_item
    ADD CONSTRAINT fk_business_id_inventory_item
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE inventory_item
    DROP CONSTRAINT IF EXISTS fk_item_category_id_inventory_item;

ALTER TABLE inventory_item
    ADD CONSTRAINT fk_item_category_id_inventory_item
        FOREIGN KEY (item_category_id) REFERENCES item_category (id);

CREATE UNIQUE INDEX IF NOT EXISTS uq_inventory_item_business_sku
    ON inventory_item (business_id, sku)
    WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_inventory_item_business_created
    ON inventory_item (business_id, created_at DESC, id DESC);

CREATE TABLE IF NOT EXISTS sale (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    customer_id INTEGER,
    sold_by_business_user_id INTEGER NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    payment_method_id INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE sale
    DROP CONSTRAINT IF EXISTS fk_business_id_sale;

ALTER TABLE sale
    ADD CONSTRAINT fk_business_id_sale
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE sale
    DROP CONSTRAINT IF EXISTS fk_customer_id_sale;

ALTER TABLE sale
    ADD CONSTRAINT fk_customer_id_sale
        FOREIGN KEY (customer_id) REFERENCES app_user (id);

ALTER TABLE sale
    DROP CONSTRAINT IF EXISTS fk_business_user_id_sale;

ALTER TABLE sale
    ADD CONSTRAINT fk_business_user_id_sale
        FOREIGN KEY (sold_by_business_user_id) REFERENCES business_user (id);

ALTER TABLE sale
    DROP CONSTRAINT IF EXISTS fk_payment_method_id_sale;

ALTER TABLE sale
    ADD CONSTRAINT fk_payment_method_id_sale
        FOREIGN KEY (payment_method_id) REFERENCES payment_method (id);

CREATE INDEX IF NOT EXISTS idx_sale_business_created
    ON sale (business_id, created_at DESC, id DESC);

CREATE TABLE IF NOT EXISTS sale_item (
    id SERIAL PRIMARY KEY,
    sale_id INTEGER NOT NULL,
    inventory_item_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    unit_cost NUMERIC(12, 2) NOT NULL,
    unit_price NUMERIC(12, 2) NOT NULL,
    total_cost NUMERIC(12, 2) NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE sale_item
    DROP CONSTRAINT IF EXISTS fk_sale_id_sale_item;

ALTER TABLE sale_item
    ADD CONSTRAINT fk_sale_id_sale_item
        FOREIGN KEY (sale_id) REFERENCES sale (id);

ALTER TABLE sale_item
    DROP CONSTRAINT IF EXISTS fk_inventory_item_id_sale_item;

ALTER TABLE sale_item
    ADD CONSTRAINT fk_inventory_item_id_sale_item
        FOREIGN KEY (inventory_item_id) REFERENCES inventory_item (id);

CREATE INDEX IF NOT EXISTS idx_sale_item_sale_created
    ON sale_item (sale_id, created_at ASC, id ASC);

CREATE TABLE IF NOT EXISTS stock_movement (
    id SERIAL PRIMARY KEY,
    inventory_item_id INTEGER NOT NULL,
    repair_order_id INTEGER,
    business_user_id INTEGER NOT NULL,
    movement_type_id INTEGER NOT NULL,
    sale_item_id INTEGER,
    quantity INTEGER NOT NULL,
    notes VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE stock_movement
    DROP CONSTRAINT IF EXISTS fk_inventory_item_id_stock_movement;

ALTER TABLE stock_movement
    ADD CONSTRAINT fk_inventory_item_id_stock_movement
        FOREIGN KEY (inventory_item_id) REFERENCES inventory_item (id);

ALTER TABLE stock_movement
    DROP CONSTRAINT IF EXISTS fk_repair_order_id_stock_movement;

ALTER TABLE stock_movement
    ADD CONSTRAINT fk_repair_order_id_stock_movement
        FOREIGN KEY (repair_order_id) REFERENCES repair_order (id);

ALTER TABLE stock_movement
    DROP CONSTRAINT IF EXISTS fk_business_user_id_stock_movement;

ALTER TABLE stock_movement
    ADD CONSTRAINT fk_business_user_id_stock_movement
        FOREIGN KEY (business_user_id) REFERENCES business_user (id);

ALTER TABLE stock_movement
    DROP CONSTRAINT IF EXISTS fk_movement_type_id_stock_movement;

ALTER TABLE stock_movement
    ADD CONSTRAINT fk_movement_type_id_stock_movement
        FOREIGN KEY (movement_type_id) REFERENCES movement_type (id);

ALTER TABLE stock_movement
    DROP CONSTRAINT IF EXISTS fk_sale_item_id_stock_movement;

ALTER TABLE stock_movement
    ADD CONSTRAINT fk_sale_item_id_stock_movement
        FOREIGN KEY (sale_item_id) REFERENCES sale_item (id);

CREATE INDEX IF NOT EXISTS idx_stock_movement_inventory_created
    ON stock_movement (inventory_item_id, created_at DESC, id DESC);
