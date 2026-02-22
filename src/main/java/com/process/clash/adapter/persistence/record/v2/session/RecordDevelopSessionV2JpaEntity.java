package com.process.clash.adapter.persistence.record.v2.session;

import com.process.clash.domain.record.enums.MonitoredApp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "record_develop_sessions_v2")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecordDevelopSessionV2JpaEntity {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id", nullable = false)
    private RecordActiveSessionV2JpaEntity activeSession;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_id", nullable = false)
    private MonitoredApp appId;

    public static RecordDevelopSessionV2JpaEntity create(
        RecordActiveSessionV2JpaEntity activeSession,
        MonitoredApp appId
    ) {
        return new RecordDevelopSessionV2JpaEntity(
            null,
            activeSession,
            appId
        );
    }

    public void changeAppId(MonitoredApp appId) {
        this.appId = appId;
    }
}
