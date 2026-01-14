package com.process.clash.adapter.persistence.task;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.record.model.enums.TaskColor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserJpaEntity user;


    public static TaskJpaEntity create(String name, TaskColor color, UserJpaEntity userJpaEntity) {
        TaskJpaEntity taskJpaEntity = new TaskJpaEntity();

        taskJpaEntity.name = name;
        taskJpaEntity.color = color;
        taskJpaEntity.user = userJpaEntity;

        return taskJpaEntity;
    }

    public static TaskJpaEntity from(Long taskId, String name, TaskColor color, UserJpaEntity userJpaEntity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        TaskJpaEntity taskJpaEntity = new TaskJpaEntity();

        taskJpaEntity.id = taskId;
        taskJpaEntity.name = name;
        taskJpaEntity.color = color;
        taskJpaEntity.user = userJpaEntity;
        taskJpaEntity.createdAt = createdAt;
        taskJpaEntity.updatedAt = updatedAt;

        return taskJpaEntity;
    }
}
