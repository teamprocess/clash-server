package com.process.clash.domain.roadmap.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserMissionHistory {

    private Long id;

    private Long userId;

    private Long missionId;

    private boolean isCleared;

    private Integer correctCount;

    private Integer totalCount;

    private Integer currentQuestionIndex;

    public static UserMissionHistory create(Long userId, Long missionId) {
        return new UserMissionHistory(null, userId, missionId, false, 0, 0, 0);
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public void recordCorrectAnswer() {
        this.correctCount = this.correctCount + 1;
        this.currentQuestionIndex = this.currentQuestionIndex + 1;
        checkCleared();
    }

    public void checkCleared() {
        this.isCleared = this.correctCount.equals(this.totalCount);
    }
}