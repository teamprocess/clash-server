package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.adapter.persistence.user.UserJpaEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_mission_history",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_user_id", "fk_mission_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Column(name = "current_question_index")
    private Integer currentQuestionIndex; // 현재 진행 중인 질문 인덱스

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }

    public void setMission(MissionJpaEntity mission) {
        this.mission = mission;
    }
    public static UserMissionHistoryJpaEntity ofId(Long id) {
        UserMissionHistoryJpaEntity e = new UserMissionHistoryJpaEntity();
        try {
            java.lang.reflect.Field f = UserMissionHistoryJpaEntity.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(e, id);
        } catch (Exception ex) {}
        return e;
    }

    public void setIsCleared(boolean isCleared) { this.isCleared = isCleared; }
    public void setScore(Integer score) { this.score = score; }
    public void setCurrentQuestionIndex(Integer index) { this.currentQuestionIndex = index; }
}