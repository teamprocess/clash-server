package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.adapter.persistence.user.UserJpaEntity;
import com.process.clash.domain.common.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_chapter_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_user_id", "fk_chapter_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserChapterProgressJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_chapter_id", nullable = false)
    private ChapterJpaEntity chapter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProgressStatus status; // LOCKED, IN_PROGRESS, COMPLETED

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }

    public void setChapter(ChapterJpaEntity chapter) {
        this.chapter = chapter;
    }
    public static UserChapterProgressJpaEntity ofId(Long id) {
        UserChapterProgressJpaEntity e = new UserChapterProgressJpaEntity();
        try {
            java.lang.reflect.Field f = UserChapterProgressJpaEntity.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(e, id);
        } catch (Exception ex) {}
        return e;
    }

    public void setStatus(com.process.clash.domain.common.enums.ProgressStatus status) { this.status = status; }
}