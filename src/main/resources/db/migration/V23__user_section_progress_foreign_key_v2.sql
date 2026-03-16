alter table user_section_progress
drop constraint fk_user_section_progress_on_fk_current_chapter;

alter table user_section_progress
    add constraint fk_user_section_progress_on_fk_current_chapter
        foreign key (fk_current_chapter_id) references chapters_v2
            on delete set null;