package com.process.clash.adapter.persistence.roadmap.missionhistory;

import com.process.clash.adapter.persistence.roadmap.mission.MissionJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import jakarta.persistence.*;
import jakarta.persistence.EntityListeners;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_mission_history",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_user_id", "fk_mission_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserMissionHistoryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_mission_id", nullable = false)
    private MissionJpaEntity mission;

    @Column(name = "is_cleared", nullable = false)
    private boolean isCleared;

    @Column(name = "correct_count", nullable = false)
    private Integer correctCount;

    @Column(name = "total_count", nullable = false)
    private Integer totalCount;

    @Column(name = "current_question_index")
    private Integer currentQuestionIndex; // 현재 진행 중인 질문 인덱스

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

}
