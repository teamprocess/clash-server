package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.adapter.persistence.user.UserJpaEntity;
import com.process.clash.domain.common.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_chapter_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "chapter_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}