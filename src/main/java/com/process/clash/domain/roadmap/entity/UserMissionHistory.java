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

    public static UserMissionHistory create(Long userId, Long missionId, Integer totalCount) {
        return new UserMissionHistory(null, userId, missionId, false, 0, totalCount, 0);
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public void recordCorrectAnswer() {
        this.correctCount = this.correctCount + 1;
        checkCleared();
    }

    public void recordQuestionAttempt() {
        this.currentQuestionIndex = this.currentQuestionIndex + 1;
    }

    public void checkCleared() {
        this.isCleared = this.correctCount.equals(this.totalCount);
    }

    public void reset() {
        this.isCleared = false;
        this.correctCount = 0;
        this.currentQuestionIndex = 0;
    }
}