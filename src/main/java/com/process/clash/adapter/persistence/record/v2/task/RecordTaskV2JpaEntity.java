package com.process.clash.adapter.persistence.record.v2.task;

import com.process.clash.adapter.persistence.record.v2.subject.RecordSubjectV2JpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "record_tasks_v2")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecordTaskV2JpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fk_record_subject_id", nullable = true)
    private RecordSubjectV2JpaEntity subject;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserJpaEntity user;

    public static RecordTaskV2JpaEntity create(
        String name,
        boolean completed,
        RecordSubjectV2JpaEntity subject,
        UserJpaEntity user
    ) {
        return new RecordTaskV2JpaEntity(
            null,
            null,
            null,
            name,
            completed,
            subject,
            user
        );
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeCompleted(boolean completed) {
        this.completed = completed;
    }

    public void changeSubject(RecordSubjectV2JpaEntity subject) {
        this.subject = subject;
    }
}
