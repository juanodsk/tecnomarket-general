CREATE TABLE IF NOT EXISTS department (
    id SERIAL PRIMARY KEY,
    country_id INTEGER NOT NULL,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(120) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_department_country FOREIGN KEY (country_id) REFERENCES country(id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_department_country_code_active
    ON department(country_id, lower(code))
    WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uk_department_country_name_active
    ON department(country_id, lower(name))
    WHERE deleted_at IS NULL;

INSERT INTO department (country_id, code, name, active)
SELECT c.id, 'ANT', 'Antioquia', TRUE
FROM country c
WHERE c.code = 'CO'
  AND NOT EXISTS (
      SELECT 1 FROM department d WHERE d.country_id = c.id AND lower(d.code) = 'ant' AND d.deleted_at IS NULL
  );

INSERT INTO department (country_id, code, name, active)
SELECT c.id, 'CUN', 'Cundinamarca', TRUE
FROM country c
WHERE c.code = 'CO'
  AND NOT EXISTS (
      SELECT 1 FROM department d WHERE d.country_id = c.id AND lower(d.code) = 'cun' AND d.deleted_at IS NULL
  );

INSERT INTO department (country_id, code, name, active)
SELECT c.id, 'VAC', 'Valle del Cauca', TRUE
FROM country c
WHERE c.code = 'CO'
  AND NOT EXISTS (
      SELECT 1 FROM department d WHERE d.country_id = c.id AND lower(d.code) = 'vac' AND d.deleted_at IS NULL
  );

INSERT INTO department (country_id, code, name, active)
SELECT c.id, 'ATL', 'Atlantico', TRUE
FROM country c
WHERE c.code = 'CO'
  AND NOT EXISTS (
      SELECT 1 FROM department d WHERE d.country_id = c.id AND lower(d.code) = 'atl' AND d.deleted_at IS NULL
  );

INSERT INTO department (country_id, code, name, active)
SELECT c.id, 'SAN', 'Santander', TRUE
FROM country c
WHERE c.code = 'CO'
  AND NOT EXISTS (
      SELECT 1 FROM department d WHERE d.country_id = c.id AND lower(d.code) = 'san' AND d.deleted_at IS NULL
  );

INSERT INTO department (country_id, code, name, active)
SELECT c.id, 'GEN', 'General', TRUE
FROM country c
WHERE EXISTS (SELECT 1 FROM city ci WHERE ci.country_id = c.id)
  AND NOT EXISTS (
      SELECT 1 FROM department d WHERE d.country_id = c.id AND lower(d.code) = 'gen' AND d.deleted_at IS NULL
  );

ALTER TABLE city ADD COLUMN IF NOT EXISTS department_id INTEGER;

UPDATE city ci
SET department_id = d.id
FROM department d
JOIN country c ON c.id = d.country_id
WHERE ci.country_id = c.id
  AND c.code = 'CO'
  AND lower(d.code) = 'ant'
  AND lower(ci.name) IN ('medellin');

UPDATE city ci
SET department_id = d.id
FROM department d
JOIN country c ON c.id = d.country_id
WHERE ci.country_id = c.id
  AND c.code = 'CO'
  AND lower(d.code) = 'cun'
  AND lower(ci.name) IN ('bogota');

UPDATE city ci
SET department_id = d.id
FROM department d
JOIN country c ON c.id = d.country_id
WHERE ci.country_id = c.id
  AND c.code = 'CO'
  AND lower(d.code) = 'vac'
  AND lower(ci.name) IN ('cali');

UPDATE city ci
SET department_id = d.id
FROM department d
JOIN country c ON c.id = d.country_id
WHERE ci.country_id = c.id
  AND c.code = 'CO'
  AND lower(d.code) = 'atl'
  AND lower(ci.name) IN ('barranquilla');

UPDATE city ci
SET department_id = d.id
FROM department d
JOIN country c ON c.id = d.country_id
WHERE ci.country_id = c.id
  AND c.code = 'CO'
  AND lower(d.code) = 'san'
  AND lower(ci.name) IN ('bucaramanga');

UPDATE city ci
SET department_id = d.id
FROM department d
WHERE d.country_id = ci.country_id
  AND lower(d.code) = 'gen'
  AND ci.department_id IS NULL;

ALTER TABLE city ALTER COLUMN department_id SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_city_department'
          AND table_name = 'city'
    ) THEN
        ALTER TABLE city
            ADD CONSTRAINT fk_city_department FOREIGN KEY (department_id) REFERENCES department(id);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_city_department_id ON city(department_id);

ALTER TABLE app_user ADD COLUMN IF NOT EXISTS country_id INTEGER;
ALTER TABLE app_user ADD COLUMN IF NOT EXISTS department_id INTEGER;
ALTER TABLE app_user ADD COLUMN IF NOT EXISTS city_id INTEGER;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_app_user_country'
          AND table_name = 'app_user'
    ) THEN
        ALTER TABLE app_user
            ADD CONSTRAINT fk_app_user_country FOREIGN KEY (country_id) REFERENCES country(id);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_app_user_department'
          AND table_name = 'app_user'
    ) THEN
        ALTER TABLE app_user
            ADD CONSTRAINT fk_app_user_department FOREIGN KEY (department_id) REFERENCES department(id);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_app_user_city'
          AND table_name = 'app_user'
    ) THEN
        ALTER TABLE app_user
            ADD CONSTRAINT fk_app_user_city FOREIGN KEY (city_id) REFERENCES city(id);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_app_user_country_id ON app_user(country_id);
CREATE INDEX IF NOT EXISTS idx_app_user_department_id ON app_user(department_id);
CREATE INDEX IF NOT EXISTS idx_app_user_city_id ON app_user(city_id);
