package com.process.clash.domain.roadmap.v2.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserQuestionHistoryV2 {

    private Long id;

    private Long userId;

    private Long chapterId;

    private boolean isCleared;

    private Integer correctCount;

    private Integer totalCount;

    private Integer currentQuestionIndex;

    public static UserQuestionHistoryV2 create(Long userId, Long chapterId, Integer totalCount) {
        return new UserQuestionHistoryV2(null, userId, chapterId, false, 0, totalCount, 0);
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
