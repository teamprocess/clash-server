package com.process.clash.adapter.persistence.recordactivitysegment;

import com.process.clash.adapter.persistence.studysession.StudySessionJpaEntity;
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
@Table(name = "record_activity_segments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecordActivitySegmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_study_session_id", nullable = false)
    private StudySessionJpaEntity session;

    @Column(nullable = false)
    private String appName;

    @Column(nullable = false)
    private Instant startedAt;

    @Column(nullable = true)
    private Instant endedAt;

    public static RecordActivitySegmentJpaEntity create(
        StudySessionJpaEntity session,
        String appName,
        Instant startedAt
    ) {
        return new RecordActivitySegmentJpaEntity(
            null,
            null,
            null,
            session,
            appName,
            startedAt,
            null
        );
    }

    public void changeEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
    }
}
