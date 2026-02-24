-- V1 FK 제약 조건에 ON DELETE CASCADE 추가

-- battles
ALTER TABLE battles DROP CONSTRAINT fk_battles_on_fk_rival;
ALTER TABLE battles
    ADD CONSTRAINT FK_BATTLES_ON_FK_RIVAL FOREIGN KEY (fk_rival_id) REFERENCES rivals (id) ON DELETE CASCADE;

ALTER TABLE battles DROP CONSTRAINT fk_battles_on_fk_winner;
ALTER TABLE battles
    ADD CONSTRAINT FK_BATTLES_ON_FK_WINNER FOREIGN KEY (fk_winner_id) REFERENCES users (id) ON DELETE CASCADE;

-- chapters
ALTER TABLE chapters DROP CONSTRAINT fk_chapters_on_section;
ALTER TABLE chapters
    ADD CONSTRAINT FK_CHAPTERS_ON_SECTION FOREIGN KEY (section_id) REFERENCES sections (id) ON DELETE CASCADE;

-- chapters_v2
ALTER TABLE chapters_v2 DROP CONSTRAINT fk_chapters_v2_on_fk_section;
ALTER TABLE chapters_v2
    ADD CONSTRAINT FK_CHAPTERS_V2_ON_FK_SECTION FOREIGN KEY (fk_section_id) REFERENCES sections (id) ON DELETE CASCADE;

-- choices
ALTER TABLE choices DROP CONSTRAINT fk_choices_on_question;
ALTER TABLE choices
    ADD CONSTRAINT FK_CHOICES_ON_QUESTION FOREIGN KEY (question_id) REFERENCES mission_questions (id) ON DELETE CASCADE;

-- choices_v2
ALTER TABLE choices_v2 DROP CONSTRAINT fk_choices_v2_on_fk_question;
ALTER TABLE choices_v2
    ADD CONSTRAINT FK_CHOICES_V2_ON_FK_QUESTION FOREIGN KEY (fk_question_id) REFERENCES questions_v2 (id) ON DELETE CASCADE;

-- github_daily_stats (FK 없이 user_id 컬럼만 존재하므로 새로 추가)
ALTER TABLE github_daily_stats
    ADD CONSTRAINT fk_github_daily_stats_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

-- groups
ALTER TABLE groups DROP CONSTRAINT fk_groups_on_fk_user;
ALTER TABLE groups
    ADD CONSTRAINT FK_GROUPS_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- group_members
ALTER TABLE group_members DROP CONSTRAINT fk_group_members_on_fk_group;
ALTER TABLE group_members
    ADD CONSTRAINT FK_GROUP_MEMBERS_ON_FK_GROUP FOREIGN KEY (fk_group_id) REFERENCES groups (id) ON DELETE CASCADE;

ALTER TABLE group_members DROP CONSTRAINT fk_group_members_on_fk_user;
ALTER TABLE group_members
    ADD CONSTRAINT FK_GROUP_MEMBERS_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- missions
ALTER TABLE missions DROP CONSTRAINT fk_missions_on_fk_chapter;
ALTER TABLE missions
    ADD CONSTRAINT FK_MISSIONS_ON_FK_CHAPTER FOREIGN KEY (fk_chapter_id) REFERENCES chapters (id) ON DELETE CASCADE;

-- mission_questions
ALTER TABLE mission_questions DROP CONSTRAINT fk_mission_questions_on_fk_mission;
ALTER TABLE mission_questions
    ADD CONSTRAINT FK_MISSION_QUESTIONS_ON_FK_MISSION FOREIGN KEY (fk_mission_id) REFERENCES missions (id) ON DELETE CASCADE;

-- products
ALTER TABLE products DROP CONSTRAINT fk_products_on_fk_season;
ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_FK_SEASON FOREIGN KEY (fk_season_id) REFERENCES seasons (id) ON DELETE CASCADE;

-- purchases
ALTER TABLE purchases DROP CONSTRAINT fk_purchases_on_fk_product;
ALTER TABLE purchases
    ADD CONSTRAINT FK_PURCHASES_ON_FK_PRODUCT FOREIGN KEY (fk_product_id) REFERENCES products (id) ON DELETE CASCADE;

ALTER TABLE purchases DROP CONSTRAINT fk_purchases_on_fk_user;
ALTER TABLE purchases
    ADD CONSTRAINT FK_PURCHASES_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- questions_v2
ALTER TABLE questions_v2 DROP CONSTRAINT fk_questions_v2_on_fk_chapter;
ALTER TABLE questions_v2
    ADD CONSTRAINT FK_QUESTIONS_V2_ON_FK_CHAPTER FOREIGN KEY (fk_chapter_id) REFERENCES chapters_v2 (id) ON DELETE CASCADE;

-- recommended_products
ALTER TABLE recommended_products DROP CONSTRAINT fk_recommended_products_on_fk_product;
ALTER TABLE recommended_products
    ADD CONSTRAINT FK_RECOMMENDED_PRODUCTS_ON_FK_PRODUCT FOREIGN KEY (fk_product_id) REFERENCES products (id) ON DELETE CASCADE;

-- record_session_segments
ALTER TABLE record_session_segments DROP CONSTRAINT fk_record_session_segments_record_session;
ALTER TABLE record_session_segments
    ADD CONSTRAINT fk_record_session_segments_record_session FOREIGN KEY (fk_record_session_id) REFERENCES record_sessions (id) ON DELETE CASCADE;

-- rivals
ALTER TABLE rivals DROP CONSTRAINT fk_rivals_on_fk_first_user;
ALTER TABLE rivals
    ADD CONSTRAINT FK_RIVALS_ON_FK_FIRST_USER FOREIGN KEY (fk_first_user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE rivals DROP CONSTRAINT fk_rivals_on_fk_second_user;
ALTER TABLE rivals
    ADD CONSTRAINT FK_RIVALS_ON_FK_SECOND_USER FOREIGN KEY (fk_second_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- sections
ALTER TABLE sections DROP CONSTRAINT fk_sections_on_category;
ALTER TABLE sections
    ADD CONSTRAINT FK_SECTIONS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE;

-- section_key_points
ALTER TABLE section_key_points DROP CONSTRAINT fk_section_key_points_on_fk_section;
ALTER TABLE section_key_points
    ADD CONSTRAINT FK_SECTION_KEY_POINTS_ON_FK_SECTION FOREIGN KEY (fk_section_id) REFERENCES sections (id) ON DELETE CASCADE;

-- section_prerequisites
ALTER TABLE section_prerequisites DROP CONSTRAINT fk_secpre_on_prerequisite;
ALTER TABLE section_prerequisites
    ADD CONSTRAINT fk_secpre_on_prerequisite FOREIGN KEY (prerequisite_id) REFERENCES sections (id) ON DELETE CASCADE;

ALTER TABLE section_prerequisites DROP CONSTRAINT fk_secpre_on_section;
ALTER TABLE section_prerequisites
    ADD CONSTRAINT fk_secpre_on_section FOREIGN KEY (section_id) REFERENCES sections (id) ON DELETE CASCADE;

-- record_sessions
ALTER TABLE record_sessions DROP CONSTRAINT fk_record_sessions_on_fk_record_task;
ALTER TABLE record_sessions
    ADD CONSTRAINT FK_RECORD_SESSIONS_ON_FK_RECORD_TASK FOREIGN KEY (fk_record_task_id) REFERENCES record_tasks (id) ON DELETE CASCADE;

ALTER TABLE record_sessions DROP CONSTRAINT fk_record_sessions_on_fk_user;
ALTER TABLE record_sessions
    ADD CONSTRAINT FK_RECORD_SESSIONS_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- record_tasks
ALTER TABLE record_tasks DROP CONSTRAINT fk_record_tasks_on_fk_user;
ALTER TABLE record_tasks
    ADD CONSTRAINT FK_RECORD_TASKS_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- user_exp_history
ALTER TABLE user_exp_history DROP CONSTRAINT fk_user_exp_history_on_fk_user;
ALTER TABLE user_exp_history
    ADD CONSTRAINT FK_USER_EXP_HISTORY_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- user_github
ALTER TABLE user_github DROP CONSTRAINT fk_user_github_on_fk_user;
ALTER TABLE user_github
    ADD CONSTRAINT FK_USER_GITHUB_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- user_goods_history
ALTER TABLE user_goods_history DROP CONSTRAINT fk_user_goods_history_on_fk_product;
ALTER TABLE user_goods_history
    ADD CONSTRAINT FK_USER_GOODS_HISTORY_ON_FK_PRODUCT FOREIGN KEY (fk_product_id) REFERENCES products (id) ON DELETE CASCADE;

ALTER TABLE user_goods_history DROP CONSTRAINT fk_user_goods_history_on_fk_user;
ALTER TABLE user_goods_history
    ADD CONSTRAINT FK_USER_GOODS_HISTORY_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- user_items
ALTER TABLE user_items DROP CONSTRAINT fk_user_items_on_fk_product;
ALTER TABLE user_items
    ADD CONSTRAINT FK_USER_ITEMS_ON_FK_PRODUCT FOREIGN KEY (fk_product_id) REFERENCES products (id) ON DELETE CASCADE;

ALTER TABLE user_items DROP CONSTRAINT fk_user_items_on_fk_user;
ALTER TABLE user_items
    ADD CONSTRAINT FK_USER_ITEMS_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- user_mission_history
ALTER TABLE user_mission_history DROP CONSTRAINT fk_user_mission_history_on_fk_mission;
ALTER TABLE user_mission_history
    ADD CONSTRAINT FK_USER_MISSION_HISTORY_ON_FK_MISSION FOREIGN KEY (fk_mission_id) REFERENCES missions (id) ON DELETE CASCADE;

ALTER TABLE user_mission_history DROP CONSTRAINT fk_user_mission_history_on_fk_user;
ALTER TABLE user_mission_history
    ADD CONSTRAINT FK_USER_MISSION_HISTORY_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- user_notices
ALTER TABLE user_notices DROP CONSTRAINT fk_user_notices_on_fk_receiver;
ALTER TABLE user_notices
    ADD CONSTRAINT FK_USER_NOTICES_ON_FK_RECEIVER FOREIGN KEY (fk_receiver_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE user_notices DROP CONSTRAINT fk_user_notices_on_fk_sender;
ALTER TABLE user_notices
    ADD CONSTRAINT FK_USER_NOTICES_ON_FK_SENDER FOREIGN KEY (fk_sender_id) REFERENCES users (id) ON DELETE CASCADE;

-- user_notices (V4에서 FK 없이 추가된 nullable 컬럼)
ALTER TABLE user_notices
    ADD CONSTRAINT fk_user_notices_on_rival FOREIGN KEY (rival_id) REFERENCES rivals (id) ON DELETE SET NULL;

ALTER TABLE user_notices
    ADD CONSTRAINT fk_user_notices_on_battle FOREIGN KEY (battle_id) REFERENCES battles (id) ON DELETE SET NULL;

-- user_question_history_v2
ALTER TABLE user_question_history_v2 DROP CONSTRAINT fk_user_question_history_v2_on_fk_chapter;
ALTER TABLE user_question_history_v2
    ADD CONSTRAINT FK_USER_QUESTION_HISTORY_V2_ON_FK_CHAPTER FOREIGN KEY (fk_chapter_id) REFERENCES chapters_v2 (id) ON DELETE CASCADE;

ALTER TABLE user_question_history_v2 DROP CONSTRAINT fk_user_question_history_v2_on_fk_user;
ALTER TABLE user_question_history_v2
    ADD CONSTRAINT FK_USER_QUESTION_HISTORY_V2_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- user_rank_history
ALTER TABLE user_rank_history DROP CONSTRAINT fk_user_rank_history_on_fk_season;
ALTER TABLE user_rank_history
    ADD CONSTRAINT FK_USER_RANK_HISTORY_ON_FK_SEASON FOREIGN KEY (fk_season_id) REFERENCES seasons (id) ON DELETE CASCADE;

ALTER TABLE user_rank_history DROP CONSTRAINT fk_user_rank_history_on_fk_user;
ALTER TABLE user_rank_history
    ADD CONSTRAINT FK_USER_RANK_HISTORY_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- user_section_progress
ALTER TABLE user_section_progress DROP CONSTRAINT fk_user_section_progress_on_fk_current_chapter;
ALTER TABLE user_section_progress
    ADD CONSTRAINT FK_USER_SECTION_PROGRESS_ON_FK_CURRENT_CHAPTER FOREIGN KEY (fk_current_chapter_id) REFERENCES chapters (id) ON DELETE SET NULL;

ALTER TABLE user_section_progress DROP CONSTRAINT fk_user_section_progress_on_fk_section;
ALTER TABLE user_section_progress
    ADD CONSTRAINT FK_USER_SECTION_PROGRESS_ON_FK_SECTION FOREIGN KEY (fk_section_id) REFERENCES sections (id) ON DELETE CASCADE;

ALTER TABLE user_section_progress DROP CONSTRAINT fk_user_section_progress_on_fk_user;
ALTER TABLE user_section_progress
    ADD CONSTRAINT FK_USER_SECTION_PROGRESS_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- user_study_times
ALTER TABLE user_study_times DROP CONSTRAINT fk_user_study_times_on_fk_user;
ALTER TABLE user_study_times
    ADD CONSTRAINT FK_USER_STUDY_TIMES_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- V5 FK 제약 조건에 ON DELETE CASCADE 추가

-- record_subjects_v2
ALTER TABLE record_subjects_v2 DROP CONSTRAINT fk_record_subjects_v2_user;
ALTER TABLE record_subjects_v2
    ADD CONSTRAINT fk_record_subjects_v2_user FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- record_tasks_v2
ALTER TABLE record_tasks_v2 DROP CONSTRAINT fk_record_tasks_v2_subject;
ALTER TABLE record_tasks_v2
    ADD CONSTRAINT fk_record_tasks_v2_subject FOREIGN KEY (fk_record_subject_id) REFERENCES record_subjects_v2 (id) ON DELETE CASCADE;

-- record_active_sessions_v2
ALTER TABLE record_active_sessions_v2 DROP CONSTRAINT fk_record_active_sessions_v2_user;
ALTER TABLE record_active_sessions_v2
    ADD CONSTRAINT fk_record_active_sessions_v2_user FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

-- record_develop_sessions_v2
ALTER TABLE record_develop_sessions_v2 DROP CONSTRAINT fk_record_develop_sessions_v2_active_session;
ALTER TABLE record_develop_sessions_v2
    ADD CONSTRAINT fk_record_develop_sessions_v2_active_session FOREIGN KEY (id) REFERENCES record_active_sessions_v2 (id) ON DELETE CASCADE;

-- record_task_sessions_v2
ALTER TABLE record_task_sessions_v2 DROP CONSTRAINT fk_record_task_sessions_v2_active_session;
ALTER TABLE record_task_sessions_v2
    ADD CONSTRAINT fk_record_task_sessions_v2_active_session FOREIGN KEY (id) REFERENCES record_active_sessions_v2 (id) ON DELETE CASCADE;

ALTER TABLE record_task_sessions_v2 DROP CONSTRAINT fk_record_task_sessions_v2_subject;
ALTER TABLE record_task_sessions_v2
    ADD CONSTRAINT fk_record_task_sessions_v2_subject FOREIGN KEY (fk_record_subject_id) REFERENCES record_subjects_v2 (id) ON DELETE CASCADE;

ALTER TABLE record_task_sessions_v2 DROP CONSTRAINT fk_record_task_sessions_v2_task;
ALTER TABLE record_task_sessions_v2
    ADD CONSTRAINT fk_record_task_sessions_v2_task FOREIGN KEY (fk_record_task_id) REFERENCES record_tasks_v2 (id) ON DELETE SET NULL;

-- record_develop_session_segments_v2
ALTER TABLE record_develop_session_segments_v2 DROP CONSTRAINT fk_record_develop_session_segments_v2_develop_session;
ALTER TABLE record_develop_session_segments_v2
    ADD CONSTRAINT fk_record_develop_session_segments_v2_develop_session FOREIGN KEY (fk_record_develop_session_id) REFERENCES record_develop_sessions_v2 (id) ON DELETE CASCADE;
