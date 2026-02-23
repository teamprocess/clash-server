ALTER TABLE github_daily_stats
    ADD COLUMN top_commit_repo VARCHAR(255),
    ADD COLUMN top_pr_repo VARCHAR(255),
    ADD COLUMN first_commit_at TIMESTAMPTZ,
    ADD COLUMN last_commit_at TIMESTAMPTZ,
    ADD COLUMN pr_merged_count INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN pr_open_count INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN pr_closed_count INTEGER NOT NULL DEFAULT 0;
