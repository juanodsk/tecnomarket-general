-- Repoint operational tables from global catalogs to business-owned catalog items.
-- Old foreign keys must be removed before updating IDs to business_catalog_item.
ALTER TABLE business_service DROP CONSTRAINT IF EXISTS fk_service_category_id_business_service;
ALTER TABLE business_service DROP CONSTRAINT IF EXISTS fk_device_type_id_business_service;
ALTER TABLE business_quality_test DROP CONSTRAINT IF EXISTS fk_device_type_id_business_quality_test;
ALTER TABLE customer_device DROP CONSTRAINT IF EXISTS fk_device_type_id_customer_device;
ALTER TABLE customer_device DROP CONSTRAINT IF EXISTS fk_brand_id_customer_device;
ALTER TABLE appointment DROP CONSTRAINT IF EXISTS fk_appointment_status_id_appointment;
ALTER TABLE repair_order DROP CONSTRAINT IF EXISTS fk_repair_order_status_id_repair_order;
ALTER TABLE repair_order_history DROP CONSTRAINT IF EXISTS fk_repair_order_status_id_repair_order_history;
ALTER TABLE quotation DROP CONSTRAINT IF EXISTS fk_quotation_status_id_quotation;
ALTER TABLE inventory_item DROP CONSTRAINT IF EXISTS fk_item_category_id_inventory_item;
ALTER TABLE sale DROP CONSTRAINT IF EXISTS fk_payment_method_id_sale;
ALTER TABLE stock_movement DROP CONSTRAINT IF EXISTS fk_movement_type_id_stock_movement;

UPDATE business_service bs
SET service_category_id = item.id
FROM service_category sc, business_catalog_item item
WHERE bs.service_category_id = sc.id
  AND item.business_id = bs.business_id
  AND item.catalog_type = 'SERVICE_CATEGORY'
  AND item.code = UPPER(REPLACE(sc.code, '-', '_'))
  AND item.deleted_at IS NULL;

UPDATE business_service bs
SET device_type_id = item.id
FROM device_type dt, business_catalog_item item
WHERE bs.device_type_id = dt.id
  AND item.business_id = bs.business_id
  AND item.catalog_type = 'DEVICE_TYPE'
  AND item.code = UPPER(REPLACE(dt.code, '-', '_'))
  AND item.deleted_at IS NULL;

UPDATE business_quality_test bqt
SET device_type_id = item.id
FROM device_type dt, business_catalog_item item
WHERE bqt.device_type_id = dt.id
  AND item.business_id = bqt.business_id
  AND item.catalog_type = 'DEVICE_TYPE'
  AND item.code = UPPER(REPLACE(dt.code, '-', '_'))
  AND item.deleted_at IS NULL;

UPDATE customer_device cd
SET device_type_id = item.id
FROM device_type dt, business_catalog_item item
WHERE cd.device_type_id = dt.id
  AND item.business_id = cd.business_id
  AND item.catalog_type = 'DEVICE_TYPE'
  AND item.code = UPPER(REPLACE(dt.code, '-', '_'))
  AND item.deleted_at IS NULL;

UPDATE customer_device cd
SET brand_id = item.id
FROM brand br, business_catalog_item item
WHERE cd.brand_id = br.id
  AND item.business_id = cd.business_id
  AND item.catalog_type = 'BRAND'
  AND item.code = UPPER(REGEXP_REPLACE(REGEXP_REPLACE(br.name, '[^a-zA-Z0-9]+', '_', 'g'), '(^_|_$)', '', 'g'))
  AND item.deleted_at IS NULL;

UPDATE appointment ap
SET status_id = item.id
FROM appointment_status ast, business_catalog_item item
WHERE ap.status_id = ast.id
  AND item.business_id = ap.business_id
  AND item.catalog_type = 'APPOINTMENT_STATUS'
  AND item.code = UPPER(REPLACE(ast.code, '-', '_'))
  AND item.deleted_at IS NULL;

UPDATE repair_order ro
SET status_id = item.id
FROM repair_order_status ros, business_catalog_item item
WHERE ro.status_id = ros.id
  AND item.business_id = ro.business_id
  AND item.catalog_type = 'REPAIR_ORDER_STATUS'
  AND item.code = UPPER(REPLACE(ros.code, '-', '_'))
  AND item.deleted_at IS NULL;

UPDATE repair_order_history roh
SET status_id = item.id
FROM repair_order ro, repair_order_status ros, business_catalog_item item
WHERE roh.repair_order_id = ro.id
  AND roh.status_id = ros.id
  AND item.business_id = ro.business_id
  AND item.catalog_type = 'REPAIR_ORDER_STATUS'
  AND item.code = UPPER(REPLACE(ros.code, '-', '_'))
  AND item.deleted_at IS NULL;

UPDATE quotation q
SET quotation_status_id = item.id
FROM quotation_status qs, business_catalog_item item
WHERE q.quotation_status_id = qs.id
  AND item.business_id = q.business_id
  AND item.catalog_type = 'QUOTATION_STATUS'
  AND item.code = UPPER(REPLACE(qs.code, '-', '_'))
  AND item.deleted_at IS NULL;

UPDATE inventory_item ii
SET item_category_id = item.id
FROM item_category ic, business_catalog_item item
WHERE ii.item_category_id = ic.id
  AND item.business_id = ii.business_id
  AND item.catalog_type = 'ITEM_CATEGORY'
  AND item.code = UPPER(REPLACE(ic.code, '-', '_'))
  AND item.deleted_at IS NULL;

UPDATE sale s
SET payment_method_id = item.id
FROM payment_method pm, business_catalog_item item
WHERE s.payment_method_id = pm.id
  AND item.business_id = s.business_id
  AND item.catalog_type = 'PAYMENT_METHOD'
  AND item.code = UPPER(REPLACE(pm.code, '-', '_'))
  AND item.deleted_at IS NULL;

UPDATE stock_movement sm
SET movement_type_id = item.id
FROM inventory_item ii, movement_type mt, business_catalog_item item
WHERE sm.inventory_item_id = ii.id
  AND sm.movement_type_id = mt.id
  AND item.business_id = ii.business_id
  AND item.catalog_type = 'MOVEMENT_TYPE'
  AND item.code = UPPER(REPLACE(mt.code, '-', '_'))
  AND item.deleted_at IS NULL;

ALTER TABLE business_service DROP CONSTRAINT IF EXISTS fk_service_category_id_business_service;
ALTER TABLE business_service
    ADD CONSTRAINT fk_service_category_id_business_service
        FOREIGN KEY (service_category_id) REFERENCES business_catalog_item (id);

ALTER TABLE business_service DROP CONSTRAINT IF EXISTS fk_device_type_id_business_service;
ALTER TABLE business_service
    ADD CONSTRAINT fk_device_type_id_business_service
        FOREIGN KEY (device_type_id) REFERENCES business_catalog_item (id);

ALTER TABLE business_quality_test DROP CONSTRAINT IF EXISTS fk_device_type_id_business_quality_test;
ALTER TABLE business_quality_test
    ADD CONSTRAINT fk_device_type_id_business_quality_test
        FOREIGN KEY (device_type_id) REFERENCES business_catalog_item (id);

ALTER TABLE customer_device DROP CONSTRAINT IF EXISTS fk_device_type_id_customer_device;
ALTER TABLE customer_device
    ADD CONSTRAINT fk_device_type_id_customer_device
        FOREIGN KEY (device_type_id) REFERENCES business_catalog_item (id);

ALTER TABLE customer_device DROP CONSTRAINT IF EXISTS fk_brand_id_customer_device;
ALTER TABLE customer_device
    ADD CONSTRAINT fk_brand_id_customer_device
        FOREIGN KEY (brand_id) REFERENCES business_catalog_item (id);

ALTER TABLE appointment DROP CONSTRAINT IF EXISTS fk_appointment_status_id_appointment;
ALTER TABLE appointment
    ADD CONSTRAINT fk_appointment_status_id_appointment
        FOREIGN KEY (status_id) REFERENCES business_catalog_item (id);

ALTER TABLE repair_order DROP CONSTRAINT IF EXISTS fk_repair_order_status_id_repair_order;
ALTER TABLE repair_order
    ADD CONSTRAINT fk_repair_order_status_id_repair_order
        FOREIGN KEY (status_id) REFERENCES business_catalog_item (id);

ALTER TABLE repair_order_history DROP CONSTRAINT IF EXISTS fk_repair_order_status_id_repair_order_history;
ALTER TABLE repair_order_history
    ADD CONSTRAINT fk_repair_order_status_id_repair_order_history
        FOREIGN KEY (status_id) REFERENCES business_catalog_item (id);

ALTER TABLE quotation DROP CONSTRAINT IF EXISTS fk_quotation_status_id_quotation;
ALTER TABLE quotation
    ADD CONSTRAINT fk_quotation_status_id_quotation
        FOREIGN KEY (quotation_status_id) REFERENCES business_catalog_item (id);

ALTER TABLE inventory_item DROP CONSTRAINT IF EXISTS fk_item_category_id_inventory_item;
ALTER TABLE inventory_item
    ADD CONSTRAINT fk_item_category_id_inventory_item
        FOREIGN KEY (item_category_id) REFERENCES business_catalog_item (id);

ALTER TABLE sale DROP CONSTRAINT IF EXISTS fk_payment_method_id_sale;
ALTER TABLE sale
    ADD CONSTRAINT fk_payment_method_id_sale
        FOREIGN KEY (payment_method_id) REFERENCES business_catalog_item (id);

ALTER TABLE stock_movement DROP CONSTRAINT IF EXISTS fk_movement_type_id_stock_movement;
ALTER TABLE stock_movement
    ADD CONSTRAINT fk_movement_type_id_stock_movement
        FOREIGN KEY (movement_type_id) REFERENCES business_catalog_item (id);
