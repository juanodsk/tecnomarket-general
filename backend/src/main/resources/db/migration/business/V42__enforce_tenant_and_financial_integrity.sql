-- Parent keys used by tenant-aware foreign keys.
ALTER TABLE business_branch ADD CONSTRAINT uq_business_branch_id_business UNIQUE (id, business_id);
ALTER TABLE business_user ADD CONSTRAINT uq_business_user_id_business UNIQUE (id, business_id);
ALTER TABLE customer_device ADD CONSTRAINT uq_customer_device_id_business_customer UNIQUE (id, business_id, customer_id);
ALTER TABLE appointment ADD CONSTRAINT uq_appointment_id_business UNIQUE (id, business_id);
ALTER TABLE repair_order ADD CONSTRAINT uq_repair_order_id_business UNIQUE (id, business_id);
ALTER TABLE inventory_item ADD CONSTRAINT uq_inventory_item_id_business_branch UNIQUE (id, business_id, branch_id);
ALTER TABLE inventory_item ADD CONSTRAINT uq_inventory_item_id_branch UNIQUE (id, branch_id);
ALTER TABLE business_service ADD CONSTRAINT uq_business_service_id_business UNIQUE (id, business_id);

-- A referenced branch must belong to the row's tenant.
ALTER TABLE appointment DROP CONSTRAINT IF EXISTS fk_branch_id_appointment;
ALTER TABLE appointment ADD CONSTRAINT fk_appointment_branch_business
    FOREIGN KEY (branch_id, business_id) REFERENCES business_branch (id, business_id);

ALTER TABLE repair_order DROP CONSTRAINT IF EXISTS fk_branch_id_repair_order;
ALTER TABLE repair_order ADD CONSTRAINT fk_repair_order_branch_business
    FOREIGN KEY (branch_id, business_id) REFERENCES business_branch (id, business_id);

ALTER TABLE inventory_item DROP CONSTRAINT IF EXISTS fk_branch_id_inventory_item;
ALTER TABLE inventory_item ADD CONSTRAINT fk_inventory_item_branch_business
    FOREIGN KEY (branch_id, business_id) REFERENCES business_branch (id, business_id);

ALTER TABLE sale DROP CONSTRAINT IF EXISTS fk_branch_id_sale;
ALTER TABLE sale ADD CONSTRAINT fk_sale_branch_business
    FOREIGN KEY (branch_id, business_id) REFERENCES business_branch (id, business_id);

ALTER TABLE business_schedule DROP CONSTRAINT IF EXISTS fk_branch_id_business_schedule;
ALTER TABLE business_schedule ADD CONSTRAINT fk_schedule_branch_business
    FOREIGN KEY (branch_id, business_id) REFERENCES business_branch (id, business_id);

-- Device, customer, service and employee references must share the same tenant.
ALTER TABLE appointment DROP CONSTRAINT IF EXISTS fk_customer_device_id_appointment;
ALTER TABLE appointment ADD CONSTRAINT fk_appointment_device_business_customer
    FOREIGN KEY (customer_device_id, business_id, customer_id)
    REFERENCES customer_device (id, business_id, customer_id);
ALTER TABLE appointment DROP CONSTRAINT IF EXISTS fk_business_service_id_appointment;
ALTER TABLE appointment ADD CONSTRAINT fk_appointment_service_business
    FOREIGN KEY (business_service_id, business_id) REFERENCES business_service (id, business_id);

ALTER TABLE repair_order DROP CONSTRAINT IF EXISTS fk_customer_device_id_repair_order;
ALTER TABLE repair_order ADD CONSTRAINT fk_repair_order_device_business_customer
    FOREIGN KEY (customer_device_id, business_id, customer_id)
    REFERENCES customer_device (id, business_id, customer_id);
ALTER TABLE repair_order DROP CONSTRAINT IF EXISTS fk_appointment_id_repair_order;
ALTER TABLE repair_order ADD CONSTRAINT fk_repair_order_appointment_business
    FOREIGN KEY (appointment_id, business_id) REFERENCES appointment (id, business_id);
ALTER TABLE repair_order DROP CONSTRAINT IF EXISTS fk_business_service_id_repair_order;
ALTER TABLE repair_order ADD CONSTRAINT fk_repair_order_service_business
    FOREIGN KEY (business_service_id, business_id) REFERENCES business_service (id, business_id);
ALTER TABLE repair_order DROP CONSTRAINT IF EXISTS fk_repair_order_received_by_business_user;
ALTER TABLE repair_order ADD CONSTRAINT fk_repair_order_receiver_business
    FOREIGN KEY (received_by_business_user_id, business_id) REFERENCES business_user (id, business_id);
ALTER TABLE repair_order DROP CONSTRAINT IF EXISTS fk_repair_order_assigned_business_user;
ALTER TABLE repair_order ADD CONSTRAINT fk_repair_order_assignee_business
    FOREIGN KEY (assigned_business_user_id, business_id) REFERENCES business_user (id, business_id);

ALTER TABLE customer_device DROP CONSTRAINT IF EXISTS fk_business_user_id_customer_device;
ALTER TABLE customer_device ADD CONSTRAINT fk_customer_device_registrar_business
    FOREIGN KEY (registered_by_business_user_id, business_id) REFERENCES business_user (id, business_id);

ALTER TABLE quotation DROP CONSTRAINT IF EXISTS fk_repair_order_id_quotation;
ALTER TABLE quotation ADD CONSTRAINT fk_quotation_repair_order_business
    FOREIGN KEY (repair_order_id, business_id) REFERENCES repair_order (id, business_id);

ALTER TABLE sale DROP CONSTRAINT IF EXISTS fk_business_user_id_sale;
ALTER TABLE sale ADD CONSTRAINT fk_sale_actor_business
    FOREIGN KEY (sold_by_business_user_id, business_id) REFERENCES business_user (id, business_id);

ALTER TABLE stock_movement DROP CONSTRAINT IF EXISTS fk_inventory_item_id_stock_movement;
ALTER TABLE stock_movement ADD CONSTRAINT fk_stock_movement_inventory_branch
    FOREIGN KEY (inventory_item_id, branch_id) REFERENCES inventory_item (id, branch_id);

-- Financial and stock values cannot be negative or internally inconsistent.
ALTER TABLE inventory_item ADD CONSTRAINT chk_inventory_item_quantity_non_negative CHECK (quantity >= 0);
ALTER TABLE inventory_item ADD CONSTRAINT chk_inventory_item_minimum_stock_non_negative CHECK (minimum_stock >= 0);
ALTER TABLE inventory_item ADD CONSTRAINT chk_inventory_item_unit_cost_non_negative CHECK (unit_cost >= 0);
ALTER TABLE inventory_item ADD CONSTRAINT chk_inventory_item_sale_price_non_negative CHECK (sale_price >= 0);
ALTER TABLE repair_order ADD CONSTRAINT chk_repair_order_estimated_price_non_negative
    CHECK (estimated_price IS NULL OR estimated_price >= 0);
ALTER TABLE repair_order ADD CONSTRAINT chk_repair_order_final_price_non_negative
    CHECK (final_price IS NULL OR final_price >= 0);
ALTER TABLE repair_order ADD CONSTRAINT chk_repair_order_delivery_dates
    CHECK (estimated_delivery_at IS NULL OR received_at IS NULL OR estimated_delivery_at >= received_at);
ALTER TABLE repair_order ADD CONSTRAINT chk_repair_order_delivered_date
    CHECK (delivered_at IS NULL OR received_at IS NULL OR delivered_at >= received_at);
ALTER TABLE quotation ADD CONSTRAINT chk_quotation_total_non_negative CHECK (total_amount >= 0);
ALTER TABLE quotation ADD CONSTRAINT chk_quotation_expiration
    CHECK (expires_at IS NULL OR created_at IS NULL OR expires_at > created_at);
ALTER TABLE quotation_item ADD CONSTRAINT chk_quotation_item_quantity_positive CHECK (quantity > 0);
ALTER TABLE quotation_item ADD CONSTRAINT chk_quotation_item_prices_non_negative
    CHECK (unit_price >= 0 AND total_price >= 0);
ALTER TABLE sale ADD CONSTRAINT chk_sale_total_non_negative CHECK (total_amount >= 0);
ALTER TABLE sale_item ADD CONSTRAINT chk_sale_item_quantity_positive CHECK (quantity > 0);
ALTER TABLE sale_item ADD CONSTRAINT chk_sale_item_prices_non_negative
    CHECK (unit_cost >= 0 AND unit_price >= 0 AND total_cost >= 0 AND total_price >= 0);
ALTER TABLE stock_movement ADD CONSTRAINT chk_stock_movement_quantity_positive CHECK (quantity > 0);
