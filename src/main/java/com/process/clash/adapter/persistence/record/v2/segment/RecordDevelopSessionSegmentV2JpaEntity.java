package com.process.clash.adapter.persistence.record.v2.segment;

import com.process.clash.adapter.persistence.record.v2.session.RecordDevelopSessionV2JpaEntity;
import com.process.clash.domain.record.enums.MonitoredApp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "record_develop_session_segments_v2")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecordDevelopSessionSegmentV2JpaEntity {

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
    @JoinColumn(name = "fk_record_develop_session_id", nullable = false)
    private RecordDevelopSessionV2JpaEntity developSession;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_id", nullable = false)
    private MonitoredApp appId;

    @Column(nullable = false)
    private Instant startedAt;

    @Column(nullable = true)
    private Instant endedAt;

    public static RecordDevelopSessionSegmentV2JpaEntity create(
        RecordDevelopSessionV2JpaEntity developSession,
        MonitoredApp appId,
        Instant startedAt
    ) {
        return create(developSession, appId, startedAt, null);
    }

    public static RecordDevelopSessionSegmentV2JpaEntity create(
        RecordDevelopSessionV2JpaEntity developSession,
        MonitoredApp appId,
        Instant startedAt,
        Instant endedAt
    ) {
        return new RecordDevelopSessionSegmentV2JpaEntity(
            null,
            null,
            null,
            developSession,
            appId,
            startedAt,
            endedAt
        );
    }

    public void changeEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
    }

    void changeDevelopSession(RecordDevelopSessionV2JpaEntity developSession) {
        this.developSession = developSession;
    }
}
