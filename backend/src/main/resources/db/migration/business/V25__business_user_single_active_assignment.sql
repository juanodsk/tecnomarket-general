-- One active assignment per user within the same business.
WITH ranked_relations AS (
    SELECT id,
           ROW_NUMBER() OVER (PARTITION BY business_id, user_id ORDER BY id ASC) AS rn
    FROM business_user
    WHERE deleted_at IS NULL
),
duplicated_relations AS (
    SELECT id
    FROM ranked_relations
    WHERE rn > 1
)
UPDATE business_user bu
SET active = false,
    deleted_at = COALESCE(bu.deleted_at, NOW())
WHERE bu.id IN (SELECT id FROM duplicated_relations);

CREATE UNIQUE INDEX IF NOT EXISTS uq_business_user_active_pair
    ON business_user (business_id, user_id)
    WHERE deleted_at IS NULL;
