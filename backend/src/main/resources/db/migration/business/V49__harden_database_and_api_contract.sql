-- Complete tenant relationships that can be enforced with the columns already present.
ALTER TABLE repair_order
    ADD CONSTRAINT uq_repair_order_id_business_customer UNIQUE (id, business_id, customer_id);

ALTER TABLE commission_settlement
    ADD CONSTRAINT uq_commission_settlement_id_business UNIQUE (id, business_id);

ALTER TABLE quotation
    DROP CONSTRAINT IF EXISTS fk_quotation_repair_order_business;
ALTER TABLE quotation
    ADD CONSTRAINT fk_quotation_order_business_customer
        FOREIGN KEY (repair_order_id, business_id, customer_id)
        REFERENCES repair_order (id, business_id, customer_id);

ALTER TABLE business_review
    DROP CONSTRAINT IF EXISTS fk_repair_order_id_business_review;
ALTER TABLE business_review
    ADD CONSTRAINT fk_business_review_order_business_customer
        FOREIGN KEY (repair_order_id, business_id, customer_id)
        REFERENCES repair_order (id, business_id, customer_id);

ALTER TABLE repair_order_commission
    DROP CONSTRAINT IF EXISTS fk_repair_order_commission_order;
ALTER TABLE repair_order_commission
    ADD CONSTRAINT fk_repair_order_commission_order_business
        FOREIGN KEY (repair_order_id, business_id)
        REFERENCES repair_order (id, business_id);

ALTER TABLE repair_order_commission
    DROP CONSTRAINT IF EXISTS fk_repair_order_commission_business_user;
ALTER TABLE repair_order_commission
    ADD CONSTRAINT fk_repair_order_commission_user_business
        FOREIGN KEY (business_user_id, business_id)
        REFERENCES business_user (id, business_id);

ALTER TABLE commission_settlement
    DROP CONSTRAINT IF EXISTS fk_commission_settlement_business_user;
ALTER TABLE commission_settlement
    ADD CONSTRAINT fk_commission_settlement_user_business
        FOREIGN KEY (business_user_id, business_id)
        REFERENCES business_user (id, business_id);

ALTER TABLE repair_order_commission
    DROP CONSTRAINT IF EXISTS fk_repair_order_commission_settlement;
ALTER TABLE repair_order_commission
    ADD CONSTRAINT fk_repair_order_commission_settlement_business
        FOREIGN KEY (settlement_id, business_id)
        REFERENCES commission_settlement (id, business_id);

ALTER TABLE repair_order
    ADD CONSTRAINT uq_repair_order_id_branch UNIQUE (id, branch_id);

ALTER TABLE stock_movement
    DROP CONSTRAINT IF EXISTS fk_repair_order_id_stock_movement;
ALTER TABLE stock_movement
    ADD CONSTRAINT fk_stock_movement_order_branch
        FOREIGN KEY (repair_order_id, branch_id)
        REFERENCES repair_order (id, branch_id);

-- Operational and arithmetic invariants.
ALTER TABLE business_branch
    ADD CONSTRAINT chk_business_branch_number_positive CHECK (branch_number > 0);

ALTER TABLE business_schedule
    ADD CONSTRAINT chk_business_schedule_slot_positive CHECK (slot_order IS NULL OR slot_order > 0);

ALTER TABLE business_service
    ADD CONSTRAINT chk_business_service_price_non_negative CHECK (base_price >= 0),
    ADD CONSTRAINT chk_business_service_duration_positive CHECK (estimated_time_minutes > 0);

ALTER TABLE repair_order_part
    ADD CONSTRAINT chk_repair_order_part_quantity_positive CHECK (quantity > 0),
    ADD CONSTRAINT chk_repair_order_part_prices_non_negative CHECK (unit_price >= 0 AND total_price >= 0),
    ADD CONSTRAINT chk_repair_order_part_total_consistent
        CHECK (total_price = ROUND(unit_price * quantity, 2));

ALTER TABLE quotation_item
    ADD CONSTRAINT chk_quotation_item_total_consistent
        CHECK (total_price = ROUND(unit_price * quantity, 2));

ALTER TABLE sale_item
    ADD CONSTRAINT chk_sale_item_totals_consistent
        CHECK (
            total_cost = ROUND(unit_cost * quantity, 2)
            AND total_price = ROUND(unit_price * quantity, 2)
        );

ALTER TABLE business
    ADD CONSTRAINT chk_business_rating_range CHECK (rating_average >= 0 AND rating_average <= 5),
    ADD CONSTRAINT chk_business_rating_count_non_negative CHECK (rating_count >= 0);

-- Indexes supporting the newly paginated operational endpoints.
CREATE INDEX IF NOT EXISTS idx_business_request_requester_created
    ON business_request (requested_by_user_id, created_at DESC, id DESC)
    WHERE active = TRUE;

CREATE INDEX IF NOT EXISTS idx_business_request_pending_created
    ON business_request (status, created_at ASC, id ASC)
    WHERE active = TRUE;

CREATE INDEX IF NOT EXISTS idx_business_user_business_id
    ON business_user (business_id, id ASC)
    WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_customer_device_business_registered
    ON customer_device (business_id, registered_at DESC, id DESC)
    WHERE deleted_at IS NULL;
