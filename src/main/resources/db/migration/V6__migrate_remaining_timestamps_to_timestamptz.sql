/*
 * Remaining timestamp normalization to UTC timestamptz
 *
 * This migration converts remaining timestamp without time zone columns
 * (excluding study_sessions / record_activity_segments handled in V5)
 * to timestamptz with UTC normalization.
 *
 * Cutover timestamp is injected via Flyway placeholder:
 *   ${global_cutover_timestamp}
 *
 * Assumption:
 * - rows before cutover are interpreted as Asia/Seoul local time
 * - rows on/after cutover are interpreted as UTC local time
 */
DO $$
DECLARE
    rec RECORD;
    v_cutover TIMESTAMP := '${global_cutover_timestamp}'::timestamp;
    v_data_type TEXT;
    v_is_nullable TEXT;
    v_default TEXT;
BEGIN
    CREATE TEMP TABLE tz_targets (
        table_name TEXT NOT NULL,
        column_name TEXT NOT NULL,
        reference_column TEXT NOT NULL
    ) ON COMMIT DROP;

    INSERT INTO tz_targets (table_name, column_name, reference_column) VALUES
        ('auth_events', 'occurred_at', 'occurred_at'),
        ('battles', 'created_at', 'created_at'),
        ('battles', 'updated_at', 'created_at'),
        ('categories', 'created_at', 'created_at'),
        ('categories', 'updated_at', 'created_at'),
        ('chapters', 'created_at', 'created_at'),
        ('chapters', 'updated_at', 'created_at'),
        ('chapters_v2', 'created_at', 'created_at'),
        ('chapters_v2', 'updated_at', 'created_at'),
        ('choices', 'created_at', 'created_at'),
        ('choices', 'updated_at', 'created_at'),
        ('choices_v2', 'created_at', 'created_at'),
        ('choices_v2', 'updated_at', 'created_at'),
        ('github_daily_stats', 'synced_at', 'synced_at'),
        ('groups', 'created_at', 'created_at'),
        ('groups', 'updated_at', 'created_at'),
        ('major_questions', 'created_at', 'created_at'),
        ('major_questions', 'updated_at', 'created_at'),
        ('mission_questions', 'created_at', 'created_at'),
        ('mission_questions', 'updated_at', 'created_at'),
        ('missions', 'created_at', 'created_at'),
        ('missions', 'updated_at', 'created_at'),
        ('products', 'created_at', 'created_at'),
        ('products', 'updated_at', 'created_at'),
        ('purchases', 'created_at', 'created_at'),
        ('purchases', 'updated_at', 'created_at'),
        ('questions_v2', 'created_at', 'created_at'),
        ('questions_v2', 'updated_at', 'created_at'),
        ('recommended_products', 'created_at', 'created_at'),
        ('recommended_products', 'updated_at', 'created_at'),
        ('rivals', 'created_at', 'created_at'),
        ('rivals', 'updated_at', 'created_at'),
        ('seasons', 'created_at', 'created_at'),
        ('seasons', 'updated_at', 'created_at'),
        ('section_key_points', 'created_at', 'created_at'),
        ('section_key_points', 'updated_at', 'created_at'),
        ('sections', 'created_at', 'created_at'),
        ('sections', 'updated_at', 'created_at'),
        ('tasks', 'created_at', 'created_at'),
        ('tasks', 'updated_at', 'created_at'),
        ('user_exp_history', 'created_at', 'created_at'),
        ('user_goods_history', 'created_at', 'created_at'),
        ('user_items', 'created_at', 'created_at'),
        ('user_items', 'updated_at', 'created_at'),
        ('user_mission_history', 'created_at', 'created_at'),
        ('user_mission_history', 'updated_at', 'created_at'),
        ('user_notices', 'created_at', 'created_at'),
        ('user_notices', 'updated_at', 'created_at'),
        ('user_pomodoro_setting', 'created_at', 'created_at'),
        ('user_pomodoro_setting', 'updated_at', 'created_at'),
        ('user_question_history_v2', 'created_at', 'created_at'),
        ('user_question_history_v2', 'updated_at', 'created_at'),
        ('user_rank_history', 'created_at', 'created_at'),
        ('user_section_progress', 'created_at', 'created_at'),
        ('user_section_progress', 'updated_at', 'created_at'),
        ('users', 'created_at', 'created_at'),
        ('users', 'updated_at', 'created_at');

    -- 1) Add shadow timestamptz columns
    FOR rec IN SELECT * FROM tz_targets LOOP
        SELECT c.data_type
          INTO v_data_type
          FROM information_schema.columns c
         WHERE c.table_schema = 'public'
           AND c.table_name = rec.table_name
           AND c.column_name = rec.column_name;

        IF v_data_type IS NULL THEN
            RAISE NOTICE 'Skip %.% (column not found)', rec.table_name, rec.column_name;
            CONTINUE;
        END IF;

        IF v_data_type = 'timestamp with time zone' THEN
            RAISE NOTICE 'Skip %.% (already timestamptz)', rec.table_name, rec.column_name;
            CONTINUE;
        END IF;

        IF v_data_type <> 'timestamp without time zone' THEN
            RAISE NOTICE 'Skip %.% (unsupported type: %)', rec.table_name, rec.column_name, v_data_type;
            CONTINUE;
        END IF;

        EXECUTE format(
            'ALTER TABLE public.%I ADD COLUMN IF NOT EXISTS %I timestamptz',
            rec.table_name,
            rec.column_name || '_new'
        );
    END LOOP;

    -- 2) Backfill normalized UTC values
    FOR rec IN SELECT * FROM tz_targets LOOP
        SELECT c.data_type
          INTO v_data_type
          FROM information_schema.columns c
         WHERE c.table_schema = 'public'
           AND c.table_name = rec.table_name
           AND c.column_name = rec.column_name;

        IF v_data_type <> 'timestamp without time zone' THEN
            CONTINUE;
        END IF;

        EXECUTE format(
            'UPDATE public.%I ' ||
            'SET %I = CASE ' ||
            '    WHEN %I IS NULL THEN NULL ' ||
            '    WHEN %I < $1 THEN %I AT TIME ZONE ''Asia/Seoul'' ' ||
            '    ELSE %I AT TIME ZONE ''UTC'' ' ||
            'END',
            rec.table_name,
            rec.column_name || '_new',
            rec.column_name,
            rec.reference_column,
            rec.column_name,
            rec.column_name
        ) USING v_cutover;
    END LOOP;

    -- 3) Preserve nullability/defaults on new columns
    FOR rec IN SELECT * FROM tz_targets LOOP
        SELECT c.data_type, c.is_nullable, c.column_default
          INTO v_data_type, v_is_nullable, v_default
          FROM information_schema.columns c
         WHERE c.table_schema = 'public'
           AND c.table_name = rec.table_name
           AND c.column_name = rec.column_name;

        IF v_data_type <> 'timestamp without time zone' THEN
            CONTINUE;
        END IF;

        IF v_default IS NOT NULL THEN
            EXECUTE format(
                'ALTER TABLE public.%I ALTER COLUMN %I SET DEFAULT %s',
                rec.table_name,
                rec.column_name || '_new',
                v_default
            );
        END IF;

        IF v_is_nullable = 'NO' THEN
            EXECUTE format(
                'ALTER TABLE public.%I ALTER COLUMN %I SET NOT NULL',
                rec.table_name,
                rec.column_name || '_new'
            );
        ELSE
            EXECUTE format(
                'ALTER TABLE public.%I ALTER COLUMN %I DROP NOT NULL',
                rec.table_name,
                rec.column_name || '_new'
            );
        END IF;
    END LOOP;

    -- 4) Swap columns and drop legacy columns
    FOR rec IN SELECT * FROM tz_targets LOOP
        SELECT c.data_type
          INTO v_data_type
          FROM information_schema.columns c
         WHERE c.table_schema = 'public'
           AND c.table_name = rec.table_name
           AND c.column_name = rec.column_name;

        IF v_data_type <> 'timestamp without time zone' THEN
            CONTINUE;
        END IF;

        EXECUTE format(
            'ALTER TABLE public.%I RENAME COLUMN %I TO %I',
            rec.table_name,
            rec.column_name,
            rec.column_name || '_legacy'
        );

        EXECUTE format(
            'ALTER TABLE public.%I RENAME COLUMN %I TO %I',
            rec.table_name,
            rec.column_name || '_new',
            rec.column_name
        );

        EXECUTE format(
            'ALTER TABLE public.%I DROP COLUMN %I',
            rec.table_name,
            rec.column_name || '_legacy'
        );
    END LOOP;
END $$;

-- Validation queries (run manually after migration)
-- 1) Remaining timestamp without time zone columns should be 0 for target set
-- SELECT table_name, column_name, data_type
-- FROM information_schema.columns
-- WHERE table_schema = 'public'
--   AND table_name IN (
--     'auth_events','battles','categories','chapters','chapters_v2','choices','choices_v2',
--     'github_daily_stats','groups','major_questions','mission_questions','missions','products','purchases',
--     'questions_v2','recommended_products','rivals','seasons','section_key_points','sections','tasks',
--     'user_exp_history','user_goods_history','user_items','user_mission_history','user_notices',
--     'user_pomodoro_setting','user_question_history_v2','user_rank_history','user_section_progress','users'
--   )
--   AND data_type = 'timestamp without time zone';
--
-- 2) Sample UTC/KST checks
-- SELECT id, created_at, created_at AT TIME ZONE 'Asia/Seoul' AS created_at_kst
-- FROM users
-- ORDER BY id DESC
-- LIMIT 20;
--
-- 3) Row count comparison template (run before/after and diff)
-- SELECT format('SELECT ''%s'' AS table_name, count(*) AS row_count FROM public.%I;', table_name, table_name)
-- FROM (VALUES
--   ('auth_events'), ('battles'), ('categories'), ('chapters'), ('chapters_v2'),
--   ('choices'), ('choices_v2'), ('github_daily_stats'), ('groups'), ('major_questions'),
--   ('mission_questions'), ('missions'), ('products'), ('purchases'), ('questions_v2'),
--   ('recommended_products'), ('rivals'), ('seasons'), ('section_key_points'), ('sections'),
--   ('tasks'), ('user_exp_history'), ('user_goods_history'), ('user_items'), ('user_mission_history'),
--   ('user_notices'), ('user_pomodoro_setting'), ('user_question_history_v2'), ('user_rank_history'),
--   ('user_section_progress'), ('users')
-- ) AS t(table_name);
