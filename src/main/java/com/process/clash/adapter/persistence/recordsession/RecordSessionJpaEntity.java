package com.process.clash.adapter.persistence.recordsession;

import com.process.clash.adapter.persistence.recordtask.RecordTaskJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.enums.RecordType;
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
@Table(name = "record_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecordSessionJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fk_record_task_id", nullable = true)
    private RecordTaskJpaEntity task;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType recordType;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_id", nullable = true)
    private MonitoredApp appId;

    @Column(nullable = false)
    private Instant startedAt;

    @Column(nullable = true)
    private Instant endedAt;

    public static RecordSessionJpaEntity create(
        UserJpaEntity user,
        RecordTaskJpaEntity task,
        RecordType recordType,
        MonitoredApp appId,
        Instant startedAt,
        Instant endedAt
    ) {
        return new RecordSessionJpaEntity(
            null,
            null,
            null,
            user,
            task,
            recordType,
            appId,
            startedAt,
            endedAt
        );
    }

    public static RecordSessionJpaEntity create(
        UserJpaEntity user,
        RecordTaskJpaEntity task,
        RecordType recordType,
        MonitoredApp appId,
        Instant startedAt
    ) {
        return create(user, task, recordType, appId, startedAt, null);
    }

    public static RecordSessionJpaEntity create(UserJpaEntity user, RecordTaskJpaEntity task, Instant startedAt) {
        return create(
            user,
            task,
            RecordType.TASK,
            null,
            startedAt
        );
    }

    public void changeEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
    }

    public void changeAppId(MonitoredApp appId) {
        this.appId = appId;
    }
}
