create table github_daily_stats (
    id bigint auto_increment primary key,
    user_id bigint not null,
    study_date date not null,
    commit_count integer not null,
    pr_count integer not null,
    issue_count integer not null,
    review_count integer not null,
    additions bigint not null,
    deletions bigint not null,
    synced_at timestamp not null,
    constraint uq_github_daily_stats_user_date unique (user_id, study_date)
);
