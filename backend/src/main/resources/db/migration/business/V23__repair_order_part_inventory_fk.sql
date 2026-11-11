ALTER TABLE repair_order_part
    DROP CONSTRAINT IF EXISTS fk_inventory_item_id_repair_order_part;

ALTER TABLE repair_order_part
    ADD CONSTRAINT fk_inventory_item_id_repair_order_part
        FOREIGN KEY (inventory_item_id) REFERENCES inventory_item (id);
