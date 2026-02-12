package com.process.clash.adapter.persistence.studysession;

import com.process.clash.adapter.persistence.task.TaskJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.record.enums.RecordType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "study_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudySessionJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fk_task_id", nullable = true)
    private TaskJpaEntity task;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType recordType;

    @Column(nullable = true)
    private String appName;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = true)
    private LocalDateTime endedAt;

    public static StudySessionJpaEntity create(
        UserJpaEntity user,
        TaskJpaEntity task,
        RecordType recordType,
        String appName,
        LocalDateTime startedAt
    ) {
        return new StudySessionJpaEntity(
            null,
            null,
            null,
            user,
            task,
            recordType,
            appName,
            startedAt,
            null
        );
    }

    public static StudySessionJpaEntity create(UserJpaEntity user, TaskJpaEntity task, LocalDateTime startedAt) {
        return create(
            user,
            task,
            RecordType.TASK,
            null,
            startedAt
        );
    }

    public void changeEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }
}
