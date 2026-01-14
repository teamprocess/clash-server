package com.process.clash.adapter.persistence.session;

import com.process.clash.adapter.persistence.task.TaskJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SessionJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "fk_user_id", nullable = false)
    private Long userId;

    @JoinColumn(name = "fk_user_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserJpaEntity user;

    @Column(name = "fk_task_id", nullable = false)
    private Long taskId;

    @JoinColumn(name = "fk_task_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TaskJpaEntity task;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = true)
    private LocalDateTime endedAt;

    @Builder
    public static SessionJpaEntity create(Long userId, Long taskId, LocalDateTime startedAt) {
        return SessionJpaEntity.builder()
            .userId(userId)
            .taskId(taskId)
            .startedAt(startedAt)
            .build();
    }

    public void changeEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }
}