package com.process.clash.adapter.persistence.record.v2.session;

import com.process.clash.adapter.persistence.record.v2.subject.RecordSubjectV2JpaEntity;
import com.process.clash.adapter.persistence.record.v2.task.RecordTaskV2JpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "record_task_sessions_v2")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecordTaskSessionV2JpaEntity {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id", nullable = false)
    private RecordActiveSessionV2JpaEntity activeSession;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fk_record_subject_id", nullable = true)
    private RecordSubjectV2JpaEntity subject;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fk_record_task_id", nullable = true)
    private RecordTaskV2JpaEntity task;

    public static RecordTaskSessionV2JpaEntity create(
        RecordActiveSessionV2JpaEntity activeSession,
        RecordSubjectV2JpaEntity subject,
        RecordTaskV2JpaEntity task
    ) {
        return new RecordTaskSessionV2JpaEntity(
            null,
            activeSession,
            subject,
            task
        );
    }
}
