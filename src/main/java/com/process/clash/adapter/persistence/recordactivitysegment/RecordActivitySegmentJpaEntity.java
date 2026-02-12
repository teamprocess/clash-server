package com.process.clash.adapter.persistence.recordactivitysegment;

import com.process.clash.adapter.persistence.studysession.StudySessionJpaEntity;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "record_activity_segments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecordActivitySegmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_study_session_id", nullable = false)
    private StudySessionJpaEntity session;

    @Column(nullable = false)
    private String appName;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = true)
    private LocalDateTime endedAt;

    public static RecordActivitySegmentJpaEntity create(
        StudySessionJpaEntity session,
        String appName,
        LocalDateTime startedAt
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

    public void changeEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }
}
