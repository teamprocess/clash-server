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

    public void recordQuestionAttempt(Integer questionOrderIndex) {
        // 순서대로 제출: 현재 진행도를 제출한 문제의 다음 순서로 업데이트
        // orderIndex는 0부터 시작 (문제1=0, 문제2=1, ...)
        // currentQuestionIndex는 "다음에 풀어야 할 문제의 순서"를 의미
        if (questionOrderIndex != null) {
            this.currentQuestionIndex = Math.max(this.currentQuestionIndex, questionOrderIndex + 1);
        }
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