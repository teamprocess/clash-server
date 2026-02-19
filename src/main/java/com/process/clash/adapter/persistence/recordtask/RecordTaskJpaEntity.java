package com.process.clash.adapter.persistence.recordtask;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "record_tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordTaskJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Column(nullable = false)
    private String name;

    @JoinColumn(name = "fk_user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserJpaEntity user;


    public static RecordTaskJpaEntity create(String name, UserJpaEntity userJpaEntity) {
        RecordTaskJpaEntity recordTaskJpaEntity = new RecordTaskJpaEntity();

        recordTaskJpaEntity.name = name;
        recordTaskJpaEntity.user = userJpaEntity;

        return recordTaskJpaEntity;
    }

    public static RecordTaskJpaEntity from(Long taskId, String name, UserJpaEntity userJpaEntity, Instant createdAt, Instant updatedAt) {
        RecordTaskJpaEntity recordTaskJpaEntity = new RecordTaskJpaEntity();

        recordTaskJpaEntity.id = taskId;
        recordTaskJpaEntity.name = name;
        recordTaskJpaEntity.user = userJpaEntity;
        recordTaskJpaEntity.createdAt = createdAt;
        recordTaskJpaEntity.updatedAt = updatedAt;

        return recordTaskJpaEntity;
    }
}
