package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.adapter.persistence.user.UserJpaEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_mission_history",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_user_id", "fk_mission_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMissionHistoryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_mission_id", nullable = false)
    private MissionJpaEntity mission;

    @Column(name = "is_cleared", nullable = false)
    private boolean isCleared;

    @Column(nullable = false)
    private Integer score; // 0점과 미채점 구분

    private Integer currentQuestionIndex; // 현재 진행 중인 질문 인덱스
}