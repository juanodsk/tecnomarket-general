DO $$
DECLARE
    col_type TEXT;
BEGIN
    SELECT c.data_type
      INTO col_type
      FROM information_schema.columns c
     WHERE c.table_schema = 'public'
       AND c.table_name = 'business'
       AND c.column_name = 'description';

    IF col_type = 'bytea' THEN
        ALTER TABLE public.business
            ALTER COLUMN description TYPE VARCHAR(255)
            USING encode(description, 'escape');
    END IF;
END $$;
