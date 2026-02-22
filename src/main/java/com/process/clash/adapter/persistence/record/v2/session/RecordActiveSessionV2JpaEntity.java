package com.process.clash.adapter.persistence.record.v2.session;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
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
@Table(name = "record_active_sessions_v2")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecordActiveSessionV2JpaEntity {

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
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserJpaEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "session_type", nullable = false)
    private RecordSessionTypeV2 sessionType;

    @Column(nullable = false)
    private Instant startedAt;

    @Column(nullable = true)
    private Instant endedAt;

    @OneToOne(mappedBy = "activeSession", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private RecordDevelopSessionV2JpaEntity developSession;

    @OneToOne(mappedBy = "activeSession", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private RecordTaskSessionV2JpaEntity taskSession;

    public static RecordActiveSessionV2JpaEntity create(
        UserJpaEntity user,
        RecordSessionTypeV2 sessionType,
        Instant startedAt
    ) {
        return new RecordActiveSessionV2JpaEntity(
            null,
            null,
            null,
            user,
            sessionType,
            startedAt,
            null,
            null,
            null
        );
    }

    public void changeEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
    }

    public void attachDevelopSession(RecordDevelopSessionV2JpaEntity developSession) {
        this.developSession = developSession;
        this.taskSession = null;
    }

    public void attachTaskSession(RecordTaskSessionV2JpaEntity taskSession) {
        this.taskSession = taskSession;
        this.developSession = null;
    }
}
