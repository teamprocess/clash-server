package com.process.clash.adapter.persistence.task;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.record.model.enums.TaskColor;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private TaskColor color;

    @Column(name = "fk_user_id", nullable = false)
    private Long userId;

    @JoinColumn(name = "fk_user_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserJpaEntity user;


    public static TaskJpaEntity create(String name, TaskColor color, Long userId) {
        TaskJpaEntity taskJpaEntity = new TaskJpaEntity();

        taskJpaEntity.name = name;
        taskJpaEntity.color = color;
        taskJpaEntity.userId = userId;

        return taskJpaEntity;
    }

    public static TaskJpaEntity from(Long taskId, String name, TaskColor color, Long userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        TaskJpaEntity taskJpaEntity = new TaskJpaEntity();

        taskJpaEntity.id = taskId;
        taskJpaEntity.name = name;
        taskJpaEntity.color = color;
        taskJpaEntity.userId = userId;
        taskJpaEntity.createdAt = createdAt;
        taskJpaEntity.updatedAt = updatedAt;

        return taskJpaEntity;
    }
}
